package com.github.thanglequoc.timerninja;

import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

public aspect TimeTrackingAspect {

    /**
     * Point cut is any method annotated with @TimerNinjaTracker
     * */
    pointcut methodAnnotatedWithTimerNinjaTracker(): execution(@TimerNinjaTracker * *(..));


    void around(): methodAnnotatedWithTimerNinjaTracker() {
        StaticPart staticPart = thisJoinPointStaticPart;
        Signature signature = staticPart.getSignature();

        MethodSignature methodSignature = (MethodSignature) signature;
        String methodName = signature.getName();
        String typeName = signature.getDeclaringTypeName();
        String returnType = methodSignature.getReturnType().getSimpleName();

//        String[] parameterTypes = signature.getParameterTypes(); // from the MethodSignature
//        System.out.println("Method Signature: " + returnType + " " + methodName + "(" + String.join(",", parameterTypes) + ")");

        System.out.println("Method name: " + methodName);
        System.out.println("Type name: " + typeName);
        System.out.println("Return type: " + returnType);
        long startTime = System.currentTimeMillis();
        proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Method took " + (endTime - startTime) + " ms to execute.");
    }
}