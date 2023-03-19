package com.github.thanglequoc.timerninja;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimerNinjaTracker {

    /**
     * The default time unit to use
     * */
    ChronoUnit timeUnit() default ChronoUnit.MILLIS;

    /**
     * Determine if this tracker should be active
     * */
    boolean isEnabled() default true;

}
