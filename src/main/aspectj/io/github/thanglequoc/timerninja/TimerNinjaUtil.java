package io.github.thanglequoc.timerninja;

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

/**
 * General utility class of TimerNinja library
 * */
public class TimerNinjaUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(TimerNinjaUtil.class);

    /**
     * Determine if the TimerNinjaTracker is enabled on the method signature annotated with {@code @TimerNinjaTracker} annotation
     * */
    public static boolean isTimerNinjaTrackerEnabled(MethodSignature methodSignature) {
        if (methodSignature == null) {
            throw new IllegalArgumentException("MethodSignature must be present");
        }

        TimerNinjaTracker annotation = methodSignature.getMethod().getAnnotation(TimerNinjaTracker.class);
        return annotation.enabled();
    }

    /**
     * Determine if the TimerNinjaTracker is enabled on the constructor signature annotated with {@code @TimerNinjaTracker} annotation
     * */
    public static boolean isTimerNinjaTrackerEnabled(ConstructorSignature constructorSignature) {
        if (constructorSignature == null) {
            throw new IllegalArgumentException("ConstructorSignature must be present");
        }
        TimerNinjaTracker annotation = (TimerNinjaTracker) constructorSignature.getConstructor().getAnnotation(TimerNinjaTracker.class);
        return annotation.enabled();
    }

    /**
     * Get the ChronoUnit setting of {@code @TimerNinjaTracker} annotation on method
     * */
    public static ChronoUnit getTrackingTimeUnit(MethodSignature methodSignature) {
        if (methodSignature == null) {
            throw new IllegalArgumentException("MethodSignature must be present");
        }

        TimerNinjaTracker annotation = methodSignature.getMethod().getAnnotation(TimerNinjaTracker.class);
        return annotation.timeUnit();
    }

    /**
     * Get the ChronoUnit setting of {@code @TimerNinjaTracker} annotation on constructor
     * */
    public static ChronoUnit getTrackingTimeUnit(ConstructorSignature constructorSignature) {
        if (constructorSignature == null) {
            throw new IllegalArgumentException("MethodSignature must be present");
        }

        TimerNinjaTracker annotation = (TimerNinjaTracker) constructorSignature.getConstructor().getAnnotation(TimerNinjaTracker.class);
        return annotation.timeUnit();
    }

    /**
     * Pretty get the method signature, output include method modifier, name, and parameter name
     *
     * @param methodSignature The method signature
     * @return The full method name, include method modifier, name, and parameters <br/>
     * E.g: "public static String prettyGetMethodSignature(MethodSignature methodSignature)"
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

    /**
     * Pretty get the method signature, output include method modifier, name, and parameter name
     *
     * @param constructorSignature The constructor signature
     * @return The full method name, include method modifier, name, and parameters <br/>
     * E.g: "public TrackerItemContext(String abc)"
     * */
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
     * Print the time tracking execution trace. <br/>
     * The output include the timer ninja trace context id, creation date of the tracking context, and the detailed
     * execution time. <br/>
     * The result is printed to the slf4j logger API. <br/>
     * Can be toggled to also print to System.out by setting the flag in {@code TimerNinjaConfiguration} class
     * */
    public static void logTimerContextTrace(TimerNinjaThreadContext timerNinjaThreadContext) {
        String traceContextId = timerNinjaThreadContext.getTraceContextId();
        boolean isSystemOutLogEnabled = TimerNinjaConfiguration.getInstance().isSystemOutLogEnabled();

        String utcTimeString = toUTCTimestampString(timerNinjaThreadContext.getCreationTime());
        LOGGER.info("Timer Ninja trace context id: {}", traceContextId);
        LOGGER.info("Trace timestamp: {}", utcTimeString);

        if (timerNinjaThreadContext.getItemContextMap().isEmpty()) {
            LOGGER.warn("There isn't any tracker enabled in the tracking context");
        }

        LOGGER.info("{===== Start of trace context id: {} =====}", traceContextId);

        if (isSystemOutLogEnabled) {
            System.out.printf("Timer Ninja trace context id: %s%n", traceContextId);
            System.out.printf("Trace timestamp: %s%n", utcTimeString);
            if (isSystemOutLogEnabled) {
                System.out.println("There isn't any tracker enabled in the tracking context");
                return;
            } else {
                System.out.printf("{===== Start of trace context id: %s =====}%n", traceContextId);
            }
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

    /**
     * Generate the indent "|-- " with prefix empty space depends on the depth of the pointer of this tracker method
     * */
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

    /**
     * Convert time in millisecond to
     * @param timeInMillis Time in millisecond
     * @param unitToConvert The ChronoUnit to convert the input timeInMillis into
     * @return Time in the specify {@code ChronoUnit}
     *
     * @throws IllegalStateException if the unitToConvert is not one of the supported time unit of this timer ninja library
     * */
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

    /**
     * Get the short form presentation unit of the ChronoUnit
     * @param chronoUnit The ChronoUnit
     * @return The short presentation unit display. E.g: s, ms,...
     * @throws IllegalStateException if the unitToConvert is not one of the supported time unit of this timer ninja library
     * */
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

    /**
     * Convert instance to UTC timestamp string
     * @param instant The time instant
     * @return Time string in UTC zone with pattern yyyy-MM-dd'T'HH:mm:ss.SSS'Z'<br/>
     * E.g: 2023-03-27T11:24:46.948Z
     * */
    private static String toUTCTimestampString(Instant instant) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                .withZone(ZoneOffset.UTC)
                                .format(instant);
    }
}
