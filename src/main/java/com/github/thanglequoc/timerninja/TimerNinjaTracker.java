package com.github.thanglequoc.timerninja;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface TimerNinjaTracker {

    /**
     * The time unit to use for the tracker.
     * Supported time units: second, millisecond (default), nanosecond
     * */
    ChronoUnit timeUnit() default ChronoUnit.MILLIS;

    /**
     * Determine if this tracker should be active
     * Set to false will disable this tracker statistic from the overall tracking trace result
     * */
    boolean enabled() default true;

}
