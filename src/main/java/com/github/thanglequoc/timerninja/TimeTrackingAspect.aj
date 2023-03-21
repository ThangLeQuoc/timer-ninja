package com.github.thanglequoc.timerninja;

import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

public aspect TimeTrackingAspect {

    private static ThreadLocal<TimerNinjaThreadContext> localTrackingCtx = initTrackingContext();

    /**
     * Point cut is any method annotated with @TimerNinjaTracker
     * */
    pointcut methodAnnotatedWithTimerNinjaTracker(): execution(@TimerNinjaTracker * * (..));

    Object around(): methodAnnotatedWithTimerNinjaTracker() {
        StaticPart staticPart = thisJoinPointStaticPart;
        Signature signature = staticPart.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        TimerNinjaThreadContext trackingCtx = localTrackingCtx.get();
        trackingCtx.increasePointerDepth(); // TODO @tle 21/3/2023: Watch out for bug, the pointer depth might increase even if the tracker is disable

        long startTime = System.currentTimeMillis();
        Object object = proceed();


        long endTime = System.currentTimeMillis();

        if (TimerNinjaAspectUtil.isTimeNinjaTrackerEnabled(methodSignature)) {
            trackingCtx.addItemContext(
                new TrackerItemContext(
                    trackingCtx.getPointerDepth(),
                    String.format("%s - %d ms%n", TimerNinjaAspectUtil.prettyGetMethodSignature(methodSignature), endTime - startTime))
            );
            trackingCtx.decreasePointerDepth();
        }

        if (trackingCtx.getPointerDepth() == -1) {
            System.out.println("(Process to print the local thread context stack....)");
            TimerNinjaAspectUtil.prettyPrintTheTimerContextStack(trackingCtx);
        }

        return object;
    }

    private static ThreadLocal<TimerNinjaThreadContext> initTrackingContext() {
        ThreadLocal<TimerNinjaThreadContext> timerNinjaLocalThreadContext = new ThreadLocal<>();
        timerNinjaLocalThreadContext.set(new TimerNinjaThreadContext());
        return timerNinjaLocalThreadContext;
    }
}