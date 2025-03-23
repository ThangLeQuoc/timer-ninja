package io.github.thanglequoc.timerninja;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
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
import java.util.ArrayList;
import java.util.List;

/**
 * General utility class of TimerNinja library
 * */
public class TimerNinjaUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(TimerNinjaUtil.class);

    private static final String BASE_MSG_FORMAT = "{}{} - {} {}"; // Base format
    private static final String MSG_WITH_ARGS_FORMAT = "{}{} - Args: [{}] - {} {}"; // Format when args are included
    private static final String THRESHOLD_SUFFIX_FORMAT = " ¤ [Threshold Exceeded !!: {} ms]"; // Threshold format

    /**
     * The timer ninja util is a util class with static method, so instance creation is not allowed on this util class
     * */
    private TimerNinjaUtil() {

    }

    /**
     * Determine if the TimerNinjaTracker is enabled on the method signature annotated with {@code @TimerNinjaTracker} annotation
     * @param methodSignature The method signature
     * @return boolean check if the timer ninja tracker is enabled for the provided method
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
     * @param constructorSignature The constructor signature
     * @return boolean check if the timer ninja tracker is enabled for the provided constructor
     * */
    public static boolean isTimerNinjaTrackerEnabled(ConstructorSignature constructorSignature) {
        if (constructorSignature == null) {
            throw new IllegalArgumentException("ConstructorSignature must be present");
        }
        TimerNinjaTracker annotation = (TimerNinjaTracker) constructorSignature.getConstructor().getAnnotation(TimerNinjaTracker.class);
        return annotation.enabled();
    }

    /**
     * Get the threshold setting of the tracker
     * */
    public static int getThreshold(MethodSignature methodSignature) {
        if (methodSignature == null) {
            throw new IllegalArgumentException("MethodSignature must be present");
        }
        TimerNinjaTracker annotation = methodSignature.getMethod().getAnnotation(TimerNinjaTracker.class);
        return annotation.threshold();
    }

    /**
     * Get the threshold setting of the tracker
     * */
    public static int getThreshold(ConstructorSignature constructorSignature) {
        if (constructorSignature == null) {
            throw new IllegalArgumentException("ConstructorSignature must be present");
        }
        TimerNinjaTracker annotation = (TimerNinjaTracker) constructorSignature.getConstructor().getAnnotation(TimerNinjaTracker.class);
        return annotation.threshold();
    }

    /**
     * Determine if the method annotated with TimerNinjaTracker has the args information turn on
     * */
    public static boolean isArgsIncluded(ConstructorSignature constructorSignature) {
        if (constructorSignature == null) {
            throw new IllegalArgumentException("ConstructorSignature must be present");
        }
        TimerNinjaTracker annotation = (TimerNinjaTracker) constructorSignature.getConstructor().getAnnotation(TimerNinjaTracker.class);
        return annotation.includeArgs();
    }

    /**
     * Determine if the method annotated with TimerNinjaTracker has the args information turn on
     * */
    public static boolean isArgsIncluded(MethodSignature methodSignature) {
        if (methodSignature == null) {
            throw new IllegalArgumentException("MethodSignature must be present");
        }
        TimerNinjaTracker annotation = methodSignature.getMethod().getAnnotation(TimerNinjaTracker.class);
        return annotation.includeArgs();
    }

    /**
     * Get the ChronoUnit setting of {@code @TimerNinjaTracker} annotation on method
     * @param methodSignature The method signature being tracked
     * @return Time unit of this tracker
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
     * @param constructorSignature The constructor signature being tracked
     * @return Time unit of this tracker
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
     * @return The full method name, include method modifier, name, and parameters <br>
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
     * @return The full method name, include method modifier, name, and parameters <br>
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
     * Pretty get the argument information from the join point. <br>
     * Example output: user={name='John Doe', age=30}, amount={500}
     * */
    public static String prettyGetArguments(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        String[] names = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < args.length; i++) {
            sb.append(names[i]);
            sb.append("={");
            sb.append(args[i]);
            sb.append("}");
            if (i != args.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * Print the time tracking execution trace. <br>
     * The output include the timer ninja trace context id, creation date of the tracking context, and the detailed
     * execution time. <br>
     * The result is printed to the slf4j logger API. <br>
     * Can be toggled to also print to System.out by setting the flag in {@code TimerNinjaConfiguration} class
     *
     * @param timerNinjaThreadContext The timerNinjaThreadContext to visualize the execution time trace
     * */
    public static void logTimerContextTrace(TimerNinjaThreadContext timerNinjaThreadContext) {
        // TODO @thangle: Handle special case when the trace context has only one element that met threshold -> does not need to log
        String traceContextId = timerNinjaThreadContext.getTraceContextId();
        String utcTimeString = toUTCTimestampString(timerNinjaThreadContext.getCreationTime());

        logMessage("Timer Ninja trace context id: {}", traceContextId);
        logMessage("Trace timestamp: {}", utcTimeString);

        if (timerNinjaThreadContext.getItemContextMap().isEmpty()) {
            logMessage("There isn't any tracker enabled in the tracking context");
            return;
        }

        logMessage("{===== Start of trace context id: {} =====}", traceContextId);

        int currentMethodPointerDepthWithThresholdMeet = -1; // unassigned
        boolean withinThresholdZone = false;

        for (TrackerItemContext item : timerNinjaThreadContext.getItemContextMap().values()) {
            if (withinThresholdZone && item.getPointerDepth() == currentMethodPointerDepthWithThresholdMeet) {
                withinThresholdZone = false;
            }

            // Item has threshold & still within limit
            if (!withinThresholdZone && (item.isEnableThreshold() && item.getExecutionTime() < item.getThreshold())) {
                currentMethodPointerDepthWithThresholdMeet = item.getPointerDepth(); // TODO @thangle: Problem
                withinThresholdZone = true;
            }

            if (withinThresholdZone) {
                continue;
            }

            /*
            * Breakdown msg format
                {}{}: Indent + Method name
                - Args: [{}]: Args information (if included?)
                - {} {}: Execution time + unit
                ¤ [Threshold Exceed !!: {} ms]: If the threshold exceeded
            *  */
            List<Object> argList = new ArrayList<>();
            StringBuilder msgFormat = new StringBuilder();

            // Indent + Method name
            msgFormat.append("{}{}");
            String indent = generateIndent(item.getPointerDepth());
            String methodName = item.getMethodName();
            argList.add(indent);
            argList.add(methodName);

            // Argument information (if included?)
            if (item.isIncludeArgs()) {
                msgFormat.append(" - Args: [{}]");
                String args = item.getArguments();
                argList.add(args);
            }

            msgFormat.append(" - {} {}");
            long executionTime = item.getExecutionTime();
            String timeUnit = getPresentationUnit(item.getTimeUnit());
            argList.add(executionTime);
            argList.add(timeUnit);

            if (item.isEnableThreshold() && item.getExecutionTime() >= item.getThreshold()) {
                msgFormat.append(" ¤ [Threshold Exceed !!: {} ms]");
                argList.add(item.getThreshold());
            }

            logMessage(msgFormat.toString(), argList.toArray());
        }

        logMessage("{====== End of trace context id: {} ======}", traceContextId);
    }

    /**
     * Supportive method to log message to LOGGER slf4j, or System output (if enabled)
     * */
    private static void logMessage(String format, Object... args) {
        LOGGER.info(format, args);
        if (TimerNinjaConfiguration.getInstance().isSystemOutLogEnabled()) {
            System.out.printf(format.replace("{}", "%s") + "%n", args);
        }
    }

    /**
     * Generate the indent "|-- " with prefix empty space depends on the depth of the pointer of this tracker method
     * @param pointerDepth The pointer depth of the method/constructor
     * @return Pretty prefix "|--" with whitespaces depends on the depth of the pointer, for alignment purpose
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
     * Check if the threshold of this tracker item is exceeded
     * */
    public static boolean isThresholdExceeded(TrackerItemContext item) {
        return item.getThreshold() > 0 && item.getExecutionTime() > item.getThreshold();
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
     * @return Time string in UTC zone with pattern yyyy-MM-dd'T'HH:mm:ss.SSS'Z'<br>
     * E.g: 2023-03-27T11:24:46.948Z
     * */
    private static String toUTCTimestampString(Instant instant) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                .withZone(ZoneOffset.UTC)
                                .format(instant);
    }
}
