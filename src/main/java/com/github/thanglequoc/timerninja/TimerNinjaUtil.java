package com.github.thanglequoc.timerninja;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.temporal.ChronoUnit;

import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

public class TimerNinjaUtil {

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
        System.out.println("Timer Ninja time track trace for uuid: 123-abc-def");
        System.out.println("Trace timestamp: 2023-01-01 01:00:00:000.00Z");
        System.out.println("--------------------------------------------");

        timerNinjaThreadContext.getItemContextMap().values().stream().forEach(item-> {
            System.out.printf("%s%s - %d %s%n",
                generateIndent(item.getPointerDepth()),
                item.getMethodName(),
                item.getExecutionTime(),
                getPresentationUnit(item.getTimeUnit())
            );
        });
        System.out.println("--------------------------------------------");
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
}
