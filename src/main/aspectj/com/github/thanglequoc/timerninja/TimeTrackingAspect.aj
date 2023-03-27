package com.github.thanglequoc.timerninja;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

import static com.github.thanglequoc.timerninja.TimerNinjaThreadContext.LOGGER;

/**
 * TimeTracking aspect definition
 * */
public aspect TimeTrackingAspect {

    private static ThreadLocal<TimerNinjaThreadContext> localTrackingCtx = initTrackingContext();

    /**
     * Point cut is any method, or constructor annotated with @TimerNinjaTracker
     * */
    pointcut methodAnnotatedWithTimerNinjaTracker(): execution(@com.github.thanglequoc.timerninja.TimerNinjaTracker * * (..));
    pointcut constructorAnnotatedWithTimerNinjaTracker(): execution(@com.github.thanglequoc.timerninja.TimerNinjaTracker *.new(..));

    /**
     * Around advice for method that is annotated with {@code @TimerNinjaTracker} annotation
     * */
    Object around(): methodAnnotatedWithTimerNinjaTracker() {
        StaticPart staticPart = thisJoinPointStaticPart;
        Signature signature = staticPart.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        if (isTrackingContextNull()) {
            localTrackingCtx = initTrackingContext();
        }

        TimerNinjaThreadContext trackingCtx = localTrackingCtx.get();
        String traceContextId = trackingCtx.getTraceContextId();
        boolean isTrackerEnabled = TimerNinjaUtil.isTimerNinjaTrackerEnabled(methodSignature);

        String methodSignatureString = TimerNinjaUtil.prettyGetMethodSignature(methodSignature);
        TrackerItemContext trackerItemContext = new TrackerItemContext(trackingCtx.getPointerDepth(), methodSignatureString);
        String uuid = UUID.randomUUID().toString();

        Thread currentThread = Thread.currentThread();
        String threadName = currentThread.getName();
        long threadId = currentThread.getId();

        if (isTrackerEnabled) {
            LOGGER.debug("{} ({})|{}| TrackerItemContext {} initiated, start tracking on: {}",
                threadName, threadId, traceContextId, uuid, methodSignatureString);
            trackingCtx.addItemContext(uuid, trackerItemContext);
            trackingCtx.increasePointerDepth();
        }

        // Method invocation
        long startTime = System.currentTimeMillis();
        Object object = proceed();
        long endTime = System.currentTimeMillis();

        if (isTrackerEnabled) {
            LOGGER.debug("{} ({})|{}| TrackerItemContext {} finished tracking on: {}. Evaluating execution time...",
                threadName, threadId, traceContextId, uuid, methodSignatureString);
            ChronoUnit trackingTimeUnit = TimerNinjaUtil.getTrackingTimeUnit(methodSignature);
            trackerItemContext.setExecutionTime(TimerNinjaUtil.convertFromMillis(endTime - startTime, trackingTimeUnit));
            trackerItemContext.setTimeUnit(trackingTimeUnit);
            LOGGER.debug("{} ({})|{}| TrackerItemContext: {}", threadName, threadId, traceContextId, trackerItemContext);
            trackingCtx.decreasePointerDepth();
        }

        if (trackingCtx.getPointerDepth() == 0) {
            TimerNinjaUtil.logTimerContextTrace(trackingCtx);
            localTrackingCtx.remove();
            LOGGER.debug("{} ({})| TimerNinjaTracking context {} is completed and has been removed",
                threadName, threadId, traceContextId);
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
        String traceContextId = trackingCtx.getTraceContextId();
        boolean isTrackerEnabled = TimerNinjaUtil.isTimerNinjaTrackerEnabled(constructorSignature);

        String constructorSignatureString = TimerNinjaUtil.prettyGetConstructorSignature(constructorSignature);
        TrackerItemContext trackerItemContext = new TrackerItemContext(trackingCtx.getPointerDepth(), constructorSignatureString);
        String uuid = UUID.randomUUID().toString();

        Thread currentThread = Thread.currentThread();
        String threadName = currentThread.getName();
        long threadId = currentThread.getId();

        if (isTrackerEnabled) {
            LOGGER.debug("{} ({})|{}| TrackerItemContext {} initiated, start tracking on constructor: {}",
                threadName, threadId, traceContextId, uuid, constructorSignatureString);
            trackingCtx.addItemContext(uuid, trackerItemContext);
            trackingCtx.increasePointerDepth();
        }

        // Method invocation
        long startTime = System.currentTimeMillis();
        Object object = proceed();
        long endTime = System.currentTimeMillis();

        if (isTrackerEnabled) {
            LOGGER.debug("{} ({})|{}| TrackerItemContext {} finished tracking on constructor: {}. Evaluating execution time...",
                threadName, threadId, traceContextId, uuid, constructorSignatureString);
            ChronoUnit trackingTimeUnit = TimerNinjaUtil.getTrackingTimeUnit(constructorSignature);
            trackerItemContext.setExecutionTime(TimerNinjaUtil.convertFromMillis(endTime - startTime, trackingTimeUnit));
            trackerItemContext.setTimeUnit(trackingTimeUnit);
            LOGGER.debug("{} ({})|{}| TrackerItemContext: {}", threadName, threadId, traceContextId, trackerItemContext);
            trackingCtx.decreasePointerDepth();
        }

        if (trackingCtx.getPointerDepth() == 0) {
            TimerNinjaUtil.logTimerContextTrace(trackingCtx);
            localTrackingCtx.remove();
            LOGGER.debug("{} ({})| TimerNinjaTracking context {} is completed and has been removed",
                threadName, threadId, traceContextId);
        }

        return object;
    }

    private static ThreadLocal<TimerNinjaThreadContext> initTrackingContext() {
        Thread currentThread = Thread.currentThread();
        ThreadLocal<TimerNinjaThreadContext> timerNinjaLocalThreadContext = new ThreadLocal<>();
        TimerNinjaThreadContext timerNinjaThreadContext = new TimerNinjaThreadContext();
        timerNinjaLocalThreadContext.set(timerNinjaThreadContext);
        LOGGER.debug("{} ({})| TimerNinjaTracking context {} initiated",
            currentThread.getName(),
            currentThread.getId(),
            timerNinjaThreadContext.getTraceContextId()
        );
        return timerNinjaLocalThreadContext;
    }

    private static boolean isTrackingContextNull() {
        return localTrackingCtx.get() == null;
    }
}
