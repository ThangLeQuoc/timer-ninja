package com.github.thanglequoc.timerninja;

import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimerNinjaUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(TimerNinjaUtil.class);

    /**
     * Determine if the TimerNinjaTracker is enabled
     * */
    public static boolean isTimerNinjaTrackerEnabled(MethodSignature methodSignature) {
        if (methodSignature == null) {
            throw new IllegalArgumentException("MethodSignature must be present");
        }

        TimerNinjaTracker annotation = methodSignature.getMethod().getAnnotation(TimerNinjaTracker.class);
        return annotation.enabled();
    }

    public static boolean isTimerNinjaTrackerEnabled(ConstructorSignature constructorSignature) {
        if (constructorSignature == null) {
            throw new IllegalArgumentException("ConstructorSignature must be present");
        }
        TimerNinjaTracker annotation = (TimerNinjaTracker) constructorSignature.getConstructor().getAnnotation(TimerNinjaTracker.class);
        return annotation.enabled();
    }

    /**
     * Get the ChronoUnit setting
     * */
    public static ChronoUnit getTrackingTimeUnit(MethodSignature methodSignature) {
        if (methodSignature == null) {
            throw new IllegalArgumentException("MethodSignature must be present");
        }

        TimerNinjaTracker annotation = methodSignature.getMethod().getAnnotation(TimerNinjaTracker.class);
        return annotation.timeUnit();
    }

    public static ChronoUnit getTrackingTimeUnit(ConstructorSignature constructorSignature) {
        if (constructorSignature == null) {
            throw new IllegalArgumentException("MethodSignature must be present");
        }

        TimerNinjaTracker annotation = (TimerNinjaTracker) constructorSignature.getConstructor().getAnnotation(TimerNinjaTracker.class);
        return annotation.timeUnit();
    }

    /**
     * Pretty get the method signature
     * */
    public static String prettyGetMethodSignature(MethodSignature methodSignature) {
        StringBuilder sb = new StringBuilder();

        String methodModifier = Modifier.toString(methodSignature.getModifiers());
        sb.append(methodModifier);
        if (!methodModifier.isEmpty()) {
            sb.append(" ");
        }

        String returnType = methodSignature.getReturnType().getSimpleName();
        sb.append(returnType).append(" ");

        String methodName = methodSignature.getName();
        sb.append(methodName).append("(");

        // pretty print the parameter names
        String[] parameterNames = methodSignature.getParameterNames();
        Class[] parameterClasses = methodSignature.getParameterTypes();
        for (int i = 0; i < parameterNames.length; i++) {
            sb.append(parameterClasses[i].getSimpleName()).append(" ").append(parameterNames[i]);
            if (i != parameterNames.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");

        return sb.toString();
    }

    public static String prettyGetConstructorSignature(ConstructorSignature constructorSignature) {
        StringBuilder sb = new StringBuilder();

        String methodModifier = Modifier.toString(constructorSignature.getModifiers());
        sb.append(methodModifier);
        if (!methodModifier.isEmpty()) {
            sb.append(" ");
        }

        Constructor constructor = constructorSignature.getConstructor();
        String constructingClassType = constructor.getDeclaringClass().getSimpleName();
        sb.append(constructingClassType).append("(");

        // pretty print the parameter names
        String[] parameterNames = constructorSignature.getParameterNames();
        Class[] parameterClasses = constructorSignature.getParameterTypes();
        for (int i = 0; i < parameterNames.length; i++) {
            sb.append(parameterClasses[i].getSimpleName()).append(" ").append(parameterNames[i]);
            if (i != parameterNames.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");

        return sb.toString();
    }

    /**
     *
     * Timer Ninja time track trace for uuid: 123-abc-def
     * Trace timestamp: 2023-01-01 01:00:00:000.00Z
     * -------------------------------------------------------
     * Food orderFood() - 12000ms
     *   |-- int subExecutionTime() - 1560ms
     *     |-- RecordType insertRecord(int x, int y) - 1100ms
     *     |-- Bank findBank(User u) - 500ms
     * -------------------------------------------------------
     *
     * */
    public static void logTimerContextTrace(TimerNinjaThreadContext timerNinjaThreadContext) {
        String traceContextId = timerNinjaThreadContext.getTraceContextId();
        boolean isSystemOutLogEnabled = TimerNinjaConfiguration.getInstance().isSystemOutLogEnabled();

        String utcTimeString = toUTCTimestampString(timerNinjaThreadContext.getCreationTime());
        LOGGER.info("Timer Ninja time track trace context id: {}", traceContextId);
        LOGGER.info("Trace timestamp: {}", utcTimeString);
        LOGGER.info("{===== Start of trace context id: {} =====}", traceContextId);

        if (isSystemOutLogEnabled) {
            System.out.printf("Timer Ninja time track trace context id: %s%n", traceContextId);
            System.out.printf("Trace timestamp: %s%n", utcTimeString);
            System.out.printf("{===== Start of trace context id: %s =====}%n", traceContextId);
        }

        timerNinjaThreadContext.getItemContextMap().values().stream().forEach(item-> {
            String indent = generateIndent(item.getPointerDepth());
            String methodName = item.getMethodName();
            long executionTime = item.getExecutionTime();
            String timeUnit = getPresentationUnit(item.getTimeUnit());

            LOGGER.info("{}{} - {} {}", indent, methodName, executionTime, timeUnit);
            if (isSystemOutLogEnabled) {
                System.out.printf("%s%s - %d %s%n",indent, methodName, executionTime, timeUnit);
            }
        });

        LOGGER.info("{====== End of trace context id: {} ======}", traceContextId);
        if (isSystemOutLogEnabled) {
            System.out.printf("{====== End of trace context id: %s ======}%n", traceContextId);
        }
    }

    private static String generateIndent(int pointerDepth) {
        if (pointerDepth == 0) {
            return "";
        }
        int spaceCount = pointerDepth * 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i<= spaceCount; i++) {
            sb.append(" ");
        }
        sb.append("|-- ");
        return sb.toString();
    }

    public static long convertFromMillis(long timeInMillis, ChronoUnit unitToConvert) {
       if (ChronoUnit.MILLIS.equals(unitToConvert)) {
           return timeInMillis;
       }
       else if (ChronoUnit.SECONDS.equals(unitToConvert)) {
           return timeInMillis / 1000;
       } else if (ChronoUnit.MICROS.equals(unitToConvert)) {
           return timeInMillis * 1000;
       }
       throw new IllegalStateException("Time unit not supported");
    }

    private static String getPresentationUnit(ChronoUnit chronoUnit) {
        if (ChronoUnit.MILLIS.equals(chronoUnit)) {
            return "ms";
        } else if (ChronoUnit.SECONDS.equals(chronoUnit)) {
            return "s";
        } else if (ChronoUnit.MICROS.equals(chronoUnit)) {
            return "µs";
        }
        throw new IllegalStateException("Time unit not supported");
    }

    private static String toUTCTimestampString(Instant instant) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                .withZone(ZoneOffset.UTC)
                                .format(instant);
    }
}
