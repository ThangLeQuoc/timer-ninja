package com.github.thanglequoc.timerninja;

public aspect TimeTrackingAspect {

    pointcut methodAnnotatedWithTimerNinjaTracker(): execution(@TimerNinjaTracker * *(..));

    void around(): methodAnnotatedWithTimerNinjaTracker() {
        long startTime = System.currentTimeMillis();
        proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Method took " + (endTime - startTime) + " ms to execute.");
    }
}