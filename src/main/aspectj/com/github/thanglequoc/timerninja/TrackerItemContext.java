package com.github.thanglequoc.timerninja;

import java.time.temporal.ChronoUnit;

/**
 * Class to store a single tracker item context of the method/constructor that is annotated with {@code @TimerNinjaTracker} and is being
 * evaluated in the AspectJ advice invocation.
 * */
public class TrackerItemContext {

    /**
     * The relative pointer depth of this method compare to the root tracker method
     * */
    private final int pointerDepth;

    /**
     * The tracking method name
     * */
    private String methodName;

    /**
     * Total execution time of this method
     * */
    private long executionTime;

    /**
     * Time unit
     * */
    private ChronoUnit timeUnit;

    TrackerItemContext(int pointerDepth, String methodName) {
        this.pointerDepth = pointerDepth;
        this.methodName = methodName;
    }

    public int getPointerDepth() {
        return pointerDepth;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setTimeUnit(ChronoUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public ChronoUnit getTimeUnit() {
        return timeUnit;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return "TrackerItemContext{" + "pointerDepth=" + pointerDepth + ", methodName='" + methodName + '\'' + ", executionTime=" + executionTime + ", timeUnit=" + timeUnit + '}';
    }
}
