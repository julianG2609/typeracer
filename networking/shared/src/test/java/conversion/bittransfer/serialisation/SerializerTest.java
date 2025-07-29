package conversion.bittransfer.serialisation;

import static org.junit.jupiter.api.Assertions.*;

import conversion.bittransfer.serialisation.types.Serializable;
import conversion.bittransfer.serialisation.types.Serialize;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class SerializerTest {
  Serializer ser = new Serializer();

  @Test
  public void serializeInt() {
    test(Integer.MAX_VALUE);
    test(Integer.MIN_VALUE);
    test(0);
    test(101);
  }

  @Test
  public void serializeShort() {
    test(Short.MAX_VALUE);
    test(Short.MIN_VALUE);
    short s = 0;
    test(s);
    s = 101;
    test(s);
  }

  @Test
  public void serializeByte() {
    test(Byte.MAX_VALUE);
    test(Byte.MIN_VALUE);
    byte s = 0;
    test(s);
    s = 101;
    test(s);
  }

  @Test
  public void serializeLong() {
    test(Long.MAX_VALUE);
    test(Long.MIN_VALUE);
    test(0L);
    test(101L);
  }

  @Test
  public void serializeFloat() {
    test(Float.MAX_VALUE);
    test(Float.MIN_VALUE);
    test(0F);
    test(101F);
    test(101.101F);
  }

  @Test
  public void serializeDouble() {
    test(Double.MAX_VALUE);
    test(Double.MIN_VALUE);
    test(0d);
    test(101d);
    test(101.101d);
  }

  @Test
  public void serializeBool() {
    test(true);
    test(false);
  }

  private void test(Object obj) {
    Field srr = ser.serialize(obj);
    Object out = ser.deserialize(srr);
    assertEquals(obj, out);
  }

  @Test
  public void serializeString() {
    String test = "test";
    Field srr = ser.serialize(test);
    Object out = ser.deserialize(srr);
    assertEquals(test, out);
  }

  @Serializable
  public static class TestClass {
    @Serialize public Integer i = 100;
    @Serialize public Double d = 101.101;
    @Serialize public String s = "test";

    public boolean equalsT(TestClass t) {
      return Objects.equals(i, t.i) && Objects.equals(d, t.d) && Objects.equals(s, t.s);
    }
  }

  @Test
  public void testClass() {
    TestClass test = new TestClass();
    ParentField field = (ParentField) ser.serialize(test);
    TestClass test2 = (TestClass) ser.deserialize(field);
    assertTrue(test.equalsT(test2));
  }
}
