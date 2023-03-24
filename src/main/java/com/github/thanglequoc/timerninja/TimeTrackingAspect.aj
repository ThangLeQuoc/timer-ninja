package com.github.thanglequoc.timerninja;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

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

        String methodSignatureString = TimerNinjaUtil.prettyGetMethodSignature(methodSignature);
        TrackerItemContext trackerItemContext = new TrackerItemContext(trackingCtx.getPointerDepth(), methodSignatureString);
        String uuid = UUID.randomUUID().toString();
        trackingCtx.addItemContext(uuid, trackerItemContext);

        if (isTrackerEnabled) {
            trackingCtx.increasePointerDepth();
        }

        // Method invocation
        long startTime = System.currentTimeMillis();
        Object object = proceed();
        long endTime = System.currentTimeMillis();

        trackerItemContext.setExecutionTime(Math.toIntExact(endTime - startTime));

        if (isTrackerEnabled) {
            trackingCtx.decreasePointerDepth();
        }

        if (trackingCtx.getPointerDepth() == 0) {
            System.out.println("(Process to print the local thread context stack....)");
            TimerNinjaUtil.prettyPrintTimerContextTrace(trackingCtx);
        }

        return object;
    }

    Object around(): constructorAnnotatedWithTimerNinjaTracker() {
        StaticPart staticPart = thisJoinPointStaticPart;
        Signature signature = staticPart.getSignature();
        ConstructorSignature constructorSignature = (ConstructorSignature) signature;

        TimerNinjaThreadContext trackingCtx = localTrackingCtx.get();
        boolean isTrackerEnabled = TimerNinjaUtil.isTimerNinjaTrackerEnabled(constructorSignature);

        String constructorSignatureString = TimerNinjaUtil.prettyGetConstructorSignature(constructorSignature);
        TrackerItemContext trackerItemContext = new TrackerItemContext(trackingCtx.getPointerDepth(), constructorSignatureString);
        String uuid = UUID.randomUUID().toString();
        trackingCtx.addItemContext(uuid, trackerItemContext);

        if (isTrackerEnabled) {
            trackingCtx.increasePointerDepth();
        }

        // Method invocation
        long startTime = System.currentTimeMillis();
        Object object = proceed();
        long endTime = System.currentTimeMillis();
        trackerItemContext.setExecutionTime(Math.toIntExact(endTime - startTime));

        if (isTrackerEnabled) {
            trackingCtx.decreasePointerDepth();
        }

        if (trackingCtx.getPointerDepth() == 0) {
            System.out.println("(Process to print the local thread context stack....)");
            TimerNinjaUtil.prettyPrintTimerContextTrace(trackingCtx);
        }

        return object;
    }

    private static ThreadLocal<TimerNinjaThreadContext> initTrackingContext() { // TODO @tle 22/3/2023: Ideally this initialization should only run once
        ThreadLocal<TimerNinjaThreadContext> timerNinjaLocalThreadContext = new ThreadLocal<>();
        timerNinjaLocalThreadContext.set(new TimerNinjaThreadContext());
        return timerNinjaLocalThreadContext;
    }
}