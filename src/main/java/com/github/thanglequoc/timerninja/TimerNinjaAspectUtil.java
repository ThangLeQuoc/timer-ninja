package com.github.thanglequoc.timerninja;

import java.time.temporal.ChronoUnit;

import org.aspectj.lang.reflect.MethodSignature;

public class TimerNinjaAspectUtil {

    /**
     * Determine if the TimerNinjaTracker is enabled
     * */
    public static boolean isTimeNinjaTrackerEnabled(MethodSignature methodSignature) {
        if (methodSignature == null) {
            throw new IllegalArgumentException("MethodSignature must be present");
        }

        TimerNinjaTracker annotation = methodSignature.getMethod().getAnnotation(TimerNinjaTracker.class);
        return annotation.enabled();
    }

    /**
     * Get the ChronoUnit setting
     * */
    public static ChronoUnit getChronoUnit(MethodSignature methodSignature) {
        if (methodSignature == null) {
            throw new IllegalArgumentException("MethodSignature must be present");
        }

        TimerNinjaTracker annotation = methodSignature.getMethod().getAnnotation(TimerNinjaTracker.class);
        return annotation.timeUnit();
    }

    /**
     * Pretty get the method signature
     * */
    public static String prettyGetMethodSignature(MethodSignature methodSignature) {
        StringBuilder sb = new StringBuilder();

        String returnType = methodSignature.getReturnType().getSimpleName();
        sb.append(returnType).append(" ");

        String methodName = methodSignature.getName();
        sb.append(methodName).append("(");

        // pretty print the parameter names
        String[] parameterNames = methodSignature.getParameterNames();
        Class[] parameterClasses = methodSignature.getParameterTypes();
        if (parameterNames.length > 0) {
            for (int i = 0; i < parameterNames.length; i++) {
                sb.append(parameterClasses[i].getSimpleName()).append(" ").append(parameterNames[i]);
                if (i != parameterNames.length - 1) {
                    sb.append(", ");
                };
            }
        }
        sb.append(")");

        // TODO @tle 21/3/2023: Include the method signature as well
        // String typeName = methodSignature.getDeclaringTypeName();
        // methodSignature.getModifiers(); java.lang.reflect.Modifier

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
    public static void prettyPrintTheTimerContextStack(TimerNinjaThreadContext timerNinjaThreadContext) {
        System.out.println("Timer Ninja time track trace for uuid: 123-abc-def");
        System.out.println("Trace timestamp: 2023-01-01 01:00:00:000.00Z");
        System.out.println("--------------------");
        while(!timerNinjaThreadContext.isItemContextEmpty()) {
            TrackerItemContext trackerItemCtx = timerNinjaThreadContext.popItemContext();
            System.out.println(generateIndent(trackerItemCtx.getPointerDepth()) + trackerItemCtx.getMethodExecutionResult());
        }
        System.out.println("--------------------");
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
}
