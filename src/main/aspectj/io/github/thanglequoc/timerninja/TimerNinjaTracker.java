package io.github.thanglequoc.timerninja;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

/**
 * Annotate any Java method with this annotation to track the total execution time of the method. <br/>
 * This annotation also supports nested tracking. If the annotated method is invoked from a parent method that is also annotated with
 * {@code @TimerNinjaTracker}, then the execution time of the method will be added to the existing tracking context.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface TimerNinjaTracker {

    /**
     * The time unit to use for the tracker.
     * Supported time units: second, millisecond (default), microsecond
     * */
    ChronoUnit timeUnit() default ChronoUnit.MILLIS;

    /**
     * Determine if this tracker should be active
     * Set to false will disable this tracker from the overall tracking trace result
     * */
    boolean enabled() default true;

}
