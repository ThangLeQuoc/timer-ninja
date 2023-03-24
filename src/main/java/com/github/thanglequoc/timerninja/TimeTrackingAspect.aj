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
        }

        return object;
    }

    private static ThreadLocal<TimerNinjaThreadContext> initTrackingContext() { // TODO @tle 22/3/2023: Ideally this initialization should only run once
        ThreadLocal<TimerNinjaThreadContext> timerNinjaLocalThreadContext = new ThreadLocal<>();
        TimerNinjaThreadContext timerNinjaThreadContext = new TimerNinjaThreadContext();

        timerNinjaLocalThreadContext.set(timerNinjaThreadContext);
        return timerNinjaLocalThreadContext;
    }
}