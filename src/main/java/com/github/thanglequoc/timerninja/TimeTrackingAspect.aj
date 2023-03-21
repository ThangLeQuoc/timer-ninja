package com.github.thanglequoc.timerninja;

import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

public aspect TimeTrackingAspect {

    /**
     * Point cut is any method annotated with @TimerNinjaTracker
     * */
    pointcut methodAnnotatedWithTimerNinjaTracker(): execution(@TimerNinjaTracker * * (..));

    Object around(): methodAnnotatedWithTimerNinjaTracker() {
        StaticPart staticPart = thisJoinPointStaticPart;
        Signature signature = staticPart.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        long startTime = System.currentTimeMillis();
        Object object = proceed();
        long endTime = System.currentTimeMillis();

        if (TimerNinjaAspectUtil.isTimeNinjaTrackerEnabled(methodSignature)) {
            System.out.printf("%s - %d ms%n", TimerNinjaAspectUtil.prettyGetMethodSignature(methodSignature), endTime - startTime);
        }
        return object;
    }
}