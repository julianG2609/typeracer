package conversion.json;

import com.google.gson.Gson;
import conversion.Converter;
import java.nio.charset.StandardCharsets;

/** The converter for the JSON format. */
public class JSONConverter extends Converter {

  private final Gson gson = new Gson();

  /** Creates a new JSONConverter ready for conversion. */
  public JSONConverter() {}

  @Override
  public byte[] convert(Object obj) {
    return gson.toJson(obj).getBytes(StandardCharsets.UTF_8);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T convert(byte[] obj, T type) {
    System.out.println(new String(obj, StandardCharsets.UTF_8));
    System.out.println(obj.length);
    return (T) gson.fromJson(new String(obj, StandardCharsets.UTF_8), type.getClass());
  }
}
