package conversion.bittransfer.serialisation.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Interface to mark variables to be serialized. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Serialize {}
