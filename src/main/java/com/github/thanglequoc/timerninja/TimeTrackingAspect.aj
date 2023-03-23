package com.github.thanglequoc.timerninja;

import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

public aspect TimeTrackingAspect {

    private static ThreadLocal<TimerNinjaThreadContext> localTrackingCtx = initTrackingContext();

    /**
     * Point cut is any method, or constructor annotated with @TimerNinjaTracker
     * */
    pointcut methodAnnotatedWithTimerNinjaTracker(): execution(@TimerNinjaTracker * * (..));

    pointcut constructorAnnotatedWithTimerNinjaTracker(): execution(@TimerNinjaTracker *.new(..));

    Object around(): methodAnnotatedWithTimerNinjaTracker() {
        StaticPart staticPart = thisJoinPointStaticPart;
        Signature signature = staticPart.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        TimerNinjaThreadContext trackingCtx = localTrackingCtx.get();
        boolean isTrackerEnabled = TimerNinjaUtil.isTimerNinjaTrackerEnabled(methodSignature);
        if (isTrackerEnabled) {
            trackingCtx.increasePointerDepth();
        }

        // Method invocation
        long startTime = System.currentTimeMillis();
        Object object = proceed();
        long endTime = System.currentTimeMillis();

        if (isTrackerEnabled) {
            trackingCtx.decreasePointerDepth();
            trackingCtx.addItemContext(
                new TrackerItemContext(
                    trackingCtx.getPointerDepth(),
                    String.format("%s - %d ms", TimerNinjaUtil.prettyGetMethodSignature(methodSignature), endTime - startTime))
            );
        }

        if (trackingCtx.getPointerDepth() == 0) {
            System.out.println("(Process to print the local thread context stack....)");
            TimerNinjaUtil.prettyPrintTheTimerContextStack(trackingCtx);
        }

        return object;
    }

    Object around(): constructorAnnotatedWithTimerNinjaTracker() {
        StaticPart staticPart = thisJoinPointStaticPart;
        Signature signature = staticPart.getSignature();
        ConstructorSignature constructorSignature = (ConstructorSignature) signature;

        TimerNinjaThreadContext trackingCtx = localTrackingCtx.get();
        boolean isTrackerEnabled = TimerNinjaUtil.isTimerNinjaTrackerEnabled(constructorSignature);
        if (isTrackerEnabled) {
            trackingCtx.increasePointerDepth();
        }

        // Method invocation
        long startTime = System.currentTimeMillis();
        Object object = proceed();
        long endTime = System.currentTimeMillis();

        if (isTrackerEnabled) {
            trackingCtx.decreasePointerDepth();
            trackingCtx.addItemContext(
                new TrackerItemContext(
                    trackingCtx.getPointerDepth(),
                    String.format("%s - %d ms", TimerNinjaUtil.prettyGetConstructorSignature(constructorSignature), endTime - startTime))
            );
        }

        if (trackingCtx.getPointerDepth() == 0) {
            System.out.println("(Process to print the local thread context stack....)");
            TimerNinjaUtil.prettyPrintTheTimerContextStack(trackingCtx);
        }

        return object;
    }

    private static ThreadLocal<TimerNinjaThreadContext> initTrackingContext() { // TODO @tle 22/3/2023: Ideally this initialization should only run once
        ThreadLocal<TimerNinjaThreadContext> timerNinjaLocalThreadContext = new ThreadLocal<>();
        timerNinjaLocalThreadContext.set(new TimerNinjaThreadContext());
        return timerNinjaLocalThreadContext;
    }
}