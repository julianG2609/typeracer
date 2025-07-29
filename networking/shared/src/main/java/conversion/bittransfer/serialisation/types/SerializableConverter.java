package conversion.bittransfer.serialisation.types;

import conversion.bittransfer.serialisation.Converter;
import conversion.bittransfer.serialisation.Field;
import conversion.bittransfer.serialisation.Serializer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

class SerializableConverter extends Converter<Object> {

  @Override
  protected Field serialize(Object in, String name) {
    Serializer serializer = new Serializer();
    Class<?> clazz = in.getClass();
    java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
    List<Field> fieldList = new ArrayList<>();
    for (java.lang.reflect.Field field : fields) {
      if (field.isAnnotationPresent(Serialize.class)) {
        try {
          fieldList.add(serializer.serialize(field.get(in), field.getName()));
        } catch (IllegalAccessException ignored) {
        }
      }
    }
    return new SerializableField(name, fieldList.toArray(new Field[0]), in.getClass());
  }

  @Override
  protected Object deserialize(Field in) {
    Serializer serializer = new Serializer();
    if (!(in instanceof SerializableField) || in.getClazz() == null) {
      return null;
    } else {
      try {
        Object out = in.getClazz().getConstructor(new Class[] {}).newInstance();
        for (Field field : ((SerializableField) in).getChildren()) {
          in.getClazz().getDeclaredField(field.getName()).set(out, serializer.deserialize(field));
        }
        return out;
      } catch (NoSuchMethodException
          | InvocationTargetException
          | InstantiationException
          | IllegalAccessException
          | NoSuchFieldException ignored) {
      }
    }
    return null;
  }
}
