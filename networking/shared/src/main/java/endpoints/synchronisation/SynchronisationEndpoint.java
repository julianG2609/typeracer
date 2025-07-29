package endpoints.synchronisation;

import static util.Util.convertByteArrayToInt;
import static util.Util.convertIntToByteArray;

import endpoints.Endpoint;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import transport.exceptions.TransportIOException;

/** Endpoint for synchronizing objects across the network. */
public class SynchronisationEndpoint extends Endpoint implements Synchronizer {

  /** Default Constructor. */
  public SynchronisationEndpoint() {}

  private final BlockingQueue<byte[]> queue = new ArrayBlockingQueue<>(25);

  private byte id = 0;

  private synchronized byte nextId() {
    return id++;
  }

  private synchronized void resetId() {
    id = 0;
  }

  private final Map<Ids, Object> pending = new HashMap<>();

  private final Map<Byte, Object> objects = new HashMap<>();
  private final Map<Ids, Byte> Ids = new ConcurrentHashMap<>();

  private final Sender sender = new Sender();

  @Override
  protected void handleReceived(byte[] data) {
    if (data[0] == Byte.MIN_VALUE && (data.length == 6 || data.length == 10)) {
      byte id = data[1];
      byte[] uidBytes = Arrays.copyOfRange(data, 2, 7);
      int uid = convertByteArrayToInt(uidBytes);
      Integer oid;
      if (data.length == 10) {
        byte[] oidBytes = Arrays.copyOfRange(data, 6, data.length);
        oid = convertByteArrayToInt(oidBytes);
      } else {
        oid = null;
      }
      Ids ids = new Ids(uid, oid);
      Ids.put(ids, id);
      synchronized (objects) {
        synchronized (pending) {
          synchronized (creators) {
            if (creators.containsKey(uid)) {
              SynchronizedCreator<Object> creator = creators.get(uid);
              Object obj = creator.createObject();
              setId(obj, oid);
              objects.put(id, obj);
              creator.handleObject(obj);
            } else if (pending.containsKey(ids)) {
              objects.put(id, pending.get(ids));
              pending.remove(ids);
            }
          }
        }
      }
    } else {
      byte id = data[0];
      synchronized (objects) {
        Object object = objects.get(id);
        if (object == null) return;
        byte[] payload = Arrays.copyOfRange(data, 1, data.length);
        Object dataObject = converter.convert(payload, object);
        for (Field field : getFields(object)) {
          if (!Modifier.isTransient(field.getModifiers())) {
            field.setAccessible(true);
            try {
              field.set(object, field.get(dataObject));
            } catch (IllegalAccessException e) {
              throw new RuntimeException(e);
            }
          }
        }
        if (object instanceof Updatable) {
          try {
            Method method = object.getClass().getMethod("update");
            method.setAccessible(true);
            method.invoke(object);
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
          } catch (NoSuchMethodException ignored) {
            System.out.println("No such method.");
          }
        }
      }
    }
  }

  @Override
  protected void registeredHandler() {
    sender.setDaemon(true);
    sender.start();
  }

  @Override
  public void destroy() {
    sender.interrupt();
  }

  @Override
  public <T> void synchronize(T obj) {
    byte[] data = converter.convert(obj);
    byte[] message = new byte[data.length + 1];
    Ids ids = new Ids(getUid(obj), getId(obj));
    // while (Ids.get(ids) == null) {
    // Thread.onSpinWait();
    // }
    if (Ids.get(ids) == null) {
      return;
    }
    message[0] = Ids.get(ids);
    System.arraycopy(data, 0, message, 1, data.length);
    try {
      queue.put(message);
    } catch (InterruptedException ignored) {

    }
  }

  @Override
  public <T> void register(T obj) {
    while (endpointManager == null) {
      Thread.onSpinWait();
    }
    if (endpointManager.getHigh()) {
      Integer oId = getId(obj);
      byte[] message;
      if (oId == null) {
        message = new byte[6];
      } else {
        message = new byte[10];
        System.arraycopy(convertIntToByteArray(oId), 0, message, 6, 4);
      }
      message[0] = Byte.MIN_VALUE;
      byte id = nextId();
      message[1] = id;
      int uid = getUid(obj);
      System.arraycopy(convertIntToByteArray(uid), 0, message, 2, 4);
      try {
        queue.put(message);
      } catch (InterruptedException ignored) {

      }
      Ids.put(new Ids(uid, oId), id);
      synchronized (objects) {
        objects.put(id, obj);
      }
    } else {
      synchronized (objects) {
        if (Ids.containsKey(new Ids(getUid(obj), getId(obj)))) {
          objects.put(Ids.get(new Ids(getUid(obj), getId(obj))), obj);
        } else {
          pending.put(new Ids(getUid(obj), getId(obj)), obj);
        }
      }
    }
  }

  final HashMap<Integer, SynchronizedCreator<Object>> creators = new HashMap<>();

  @Override
  @SuppressWarnings("unchecked")
  public <T> void register(SynchronizedCreator<T> creator) throws TransportIOException {
    Object obj = creator.createObject();
    Optional<Field> id =
        getFields(obj).stream()
            .filter((it) -> it.isAnnotationPresent(SynchronizedId.class))
            .findFirst();
    if (id.isEmpty()) {
      throw new TransportIOException(
          "Invalid class specified. Missing id. " + Arrays.toString(obj.getClass().getFields()),
          null);
    }
    int uid = getUid(creator.createObject());
    synchronized (creators) {
      creators.put(uid, (SynchronizedCreator<Object>) creator);
    }
    synchronized (objects) {
      List<Ids> ids = Ids.keySet().stream().filter((it) -> it.uid == uid).toList();
      for (Ids cid : ids) {
        T object = creator.createObject();
        setId(object, cid.oid);
        objects.put(Ids.get(cid), object);
        creator.handleObject(object);
      }
    }
  }

  @Override
  public <T> void unregister(T obj) {
    synchronized (objects) {
      byte id = Ids.remove(new Ids(getUid(obj), getId(obj)));
      objects.remove(id);
      if (objects.isEmpty()) {
        resetId();
      }
    }
  }

  @Override
  public void clear() {
    synchronized (objects) {
      for (Ids idK : Ids.keySet()) {
        byte id = Ids.remove(idK);
        objects.remove(id);
      }
      resetId();
    }
  }

  private int getUid(Object obj) {
    String name = obj.getClass().getSimpleName();
    return name.hashCode();
  }

  private Integer getId(Object obj) {
    Optional<Field> id =
        getFields(obj).stream()
            .filter((it) -> it.isAnnotationPresent(SynchronizedId.class))
            .findFirst();
    if (id.isPresent()) {
      id.get().setAccessible(true);
      try {
        return (Integer) id.get().get(obj);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  private void setId(Object obj, int id) {
    Optional<Field> idField =
        getFields(obj).stream()
            .filter((it) -> it.isAnnotationPresent(SynchronizedId.class))
            .findFirst();
    if (idField.isPresent()) {
      idField.get().setAccessible(true);
      try {
        idField.get().set(obj, id);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private record Ids(int uid, Integer oid) {
    @Override
    public boolean equals(Object o) {
      if (o == null) return false;
      if (o instanceof Ids) {
        if (this.oid != null) {
          return this.oid.equals(((Ids) o).oid) && this.uid == ((Ids) o).uid;
        } else {
          return this.uid == ((Ids) o).uid;
        }
      } else {
        return o.equals(this);
      }
    }

    @Override
    public int hashCode() {
      if (this.oid != null) {
        return (uid + oid.toString()).hashCode();
      } else {
        return Integer.toString(uid).hashCode();
      }
    }
  }

  private List<Field> getFields(Object obj) {
    List<Field> fields = new ArrayList<>(List.of(obj.getClass().getDeclaredFields()));
    Class<?> clazz = obj.getClass();
    do {
      clazz = clazz.getSuperclass();
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
    } while (clazz != Object.class);
    return fields;
  }

  private class Sender extends Thread {
    @Override
    public void run() {
      interrupted();
      try {
        while (true) {
          byte[] message = queue.take();
          send(message);
        }
      } catch (InterruptedException ignored) {
      }
    }
  }
}
