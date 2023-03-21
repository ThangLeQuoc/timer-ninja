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

        // String typeName = methodSignature.getDeclaringTypeName();
        // methodSignature.getModifiers(); java.lang.reflect.Modifier
        return sb.toString();
    }
}
