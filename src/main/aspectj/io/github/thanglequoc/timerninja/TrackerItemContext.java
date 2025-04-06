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
     * Arguments passed to the method
     * */
    private String arguments;
    private boolean includeArgs = false;

    /**
     * Total execution time of this method
     * */
    private long executionTime;

    /**
     * Time unit
     * */
    private ChronoUnit timeUnit;

    /**
     * The threshold configuration of this tracker item, default is -1 (no threshold)
     * */
    private int threshold = -1;

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
     * Constructor of tracker item context
     * @param pointerDepth The pointer depth of this item
     * @param methodName The method name of this item
     * @param arguments The argument information of this item
     * */
    TrackerItemContext(int pointerDepth, String methodName, String arguments, boolean includeArgs) {
        this.pointerDepth = pointerDepth;
        this.methodName = methodName;
        this.arguments = arguments;
        this.includeArgs = includeArgs;
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

    /**
     * Set the arguments information
     * */
    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    /**
     * Get the tracker item context argument information
     * @return argument detail
     * */
    public String getArguments() {
        return arguments;
    }

    public boolean isIncludeArgs() {
        return includeArgs;
    }

    /**
     * Set the threshold of this tracker item
     * */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Get the threshold of this tracker item
     * */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Get if the threshold setting is enabled for this item
     * */
    public boolean isEnableThreshold() {
        return threshold != -1;
    }

    @Override
    public String toString() {
        return "TrackerItemContext{" + "pointerDepth=" + pointerDepth + ", methodName='" + methodName + '\'' + ", executionTime=" + executionTime + ", timeUnit=" + timeUnit + ", args=[" + arguments + "]" + '}';
    }
}
