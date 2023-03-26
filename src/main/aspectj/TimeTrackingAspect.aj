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

        System.out.println("Ninja - Current Thread Name: " + Thread.currentThread().getName());
        System.out.println("Ninja - Current Thread ID: " + Thread.currentThread().getId());

        StaticPart staticPart = thisJoinPointStaticPart;
        Signature signature = staticPart.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        if (isTrackingContextNull()) {
            localTrackingCtx = initTrackingContext();
        }
        TimerNinjaThreadContext trackingCtx = localTrackingCtx.get();

        boolean isTrackerEnabled = TimerNinjaUtil.isTimerNinjaTrackerEnabled(methodSignature);

        String methodSignatureString = TimerNinjaUtil.prettyGetMethodSignature(methodSignature);
        TrackerItemContext trackerItemContext = new TrackerItemContext(trackingCtx.getPointerDepth(), methodSignatureString);
        String uuid = UUID.randomUUID().toString();

        if (isTrackerEnabled) {
            trackingCtx.addItemContext(uuid, trackerItemContext);
            trackingCtx.increasePointerDepth();
        }

        // Method invocation
        long startTime = System.currentTimeMillis();
        Object object = proceed();
        long endTime = System.currentTimeMillis();

        if (isTrackerEnabled) {
            ChronoUnit trackingTimeUnit = TimerNinjaUtil.getTrackingTimeUnit(methodSignature);
            trackerItemContext.setExecutionTime(TimerNinjaUtil.convertFromMillis(endTime - startTime, trackingTimeUnit));
            trackerItemContext.setTimeUnit(trackingTimeUnit);
            trackingCtx.decreasePointerDepth();
        }

        if (trackingCtx.getPointerDepth() == 0) {
            TimerNinjaUtil.logTimerContextTrace(trackingCtx);
            localTrackingCtx.remove();
        }

        return object;
    }

    Object around(): constructorAnnotatedWithTimerNinjaTracker() {
        StaticPart staticPart = thisJoinPointStaticPart;
        Signature signature = staticPart.getSignature();
        ConstructorSignature constructorSignature = (ConstructorSignature) signature;

        if (isTrackingContextNull()) {
            localTrackingCtx = initTrackingContext();
        }
        TimerNinjaThreadContext trackingCtx = localTrackingCtx.get();
        boolean isTrackerEnabled = TimerNinjaUtil.isTimerNinjaTrackerEnabled(constructorSignature);

        String constructorSignatureString = TimerNinjaUtil.prettyGetConstructorSignature(constructorSignature);
        TrackerItemContext trackerItemContext = new TrackerItemContext(trackingCtx.getPointerDepth(), constructorSignatureString);
        String uuid = UUID.randomUUID().toString();

        if (isTrackerEnabled) {
            trackingCtx.addItemContext(uuid, trackerItemContext);
            trackingCtx.increasePointerDepth();
        }

        // Method invocation
        long startTime = System.currentTimeMillis();
        Object object = proceed();
        long endTime = System.currentTimeMillis();

        if (isTrackerEnabled) {
            ChronoUnit trackingTimeUnit = TimerNinjaUtil.getTrackingTimeUnit(constructorSignature);
            trackerItemContext.setExecutionTime(TimerNinjaUtil.convertFromMillis(endTime - startTime, trackingTimeUnit));
            trackerItemContext.setTimeUnit(trackingTimeUnit);
            trackingCtx.decreasePointerDepth();
        }

        if (trackingCtx.getPointerDepth() == 0) {
            TimerNinjaUtil.logTimerContextTrace(trackingCtx);
            localTrackingCtx.remove();
        }

        return object;
    }

    private static ThreadLocal<TimerNinjaThreadContext> initTrackingContext() {
        System.out.println("Initing tracking context by thread: " + Thread.currentThread().getName());
        ThreadLocal<TimerNinjaThreadContext> timerNinjaLocalThreadContext = new ThreadLocal<>();
        TimerNinjaThreadContext timerNinjaThreadContext = new TimerNinjaThreadContext();
        timerNinjaLocalThreadContext.set(timerNinjaThreadContext);
        return timerNinjaLocalThreadContext;
    }

    private static boolean isTrackingContextNull() {
        return localTrackingCtx.get() == null;
    }
}