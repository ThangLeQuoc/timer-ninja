package io.github.thanglequoc.timerninja;

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

    /**
     * Constructor of tracker item context
     * @param pointerDepth The pointer depth of this item
     * @param methodName The method name of this item
     * */
    TrackerItemContext(int pointerDepth, String methodName) {
        this.pointerDepth = pointerDepth;
        this.methodName = methodName;
    }

    /**
     * Get the pointer depth of this item
     * @return The pointer depth
     * */
    public int getPointerDepth() {
        return pointerDepth;
    }

    /**
     * Set the item execution time
     * @param executionTime The execution time
     * */
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    /**
     * Get the item execution time
     * @return item execution time
     * */
    public long getExecutionTime() {
        return executionTime;
    }

    /**
     * Set the time unit
     * @param timeUnit Time unit
     * */
    public void setTimeUnit(ChronoUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    /**
     * Get the time unit of this item
     * @return The time unit
     * */
    public ChronoUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * Get the method name
     * @return method name
     * */
    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return "TrackerItemContext{" + "pointerDepth=" + pointerDepth + ", methodName='" + methodName + '\'' + ", executionTime=" + executionTime + ", timeUnit=" + timeUnit + '}';
    }
}
