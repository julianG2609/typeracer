package conversion.json;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class JSONTest {

  @Test
  public void test() {
    JSONConverter conv = new JSONConverter();
    TestClass test = new TestClass("This is a test!");
    byte[] rep = conv.convert(test);
    TestClass test2 = conv.convert(rep, new TestClass());
    assertTrue(test.equalsT(test2));
  }

  static class TestClass {
    public String test;

    public TestClass() {}

    public TestClass(String test) {
      this.test = test;
    }

    public boolean equalsT(TestClass testClass) {
      return this.test.equals(testClass.test);
    }
  }
}
