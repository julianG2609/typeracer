package conversion.bittransfer.serialisation.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Interface to mark classes that should be serialized. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Serializable {}
