package endpoints.synchronisation;

import java.lang.annotation.*;

/** Annotation to mark a field as synchronised. */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SynchronizedId {}
