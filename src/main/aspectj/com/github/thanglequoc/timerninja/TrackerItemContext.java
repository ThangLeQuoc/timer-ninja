package com.github.thanglequoc.timerninja;

import java.time.temporal.ChronoUnit;

public class TrackerItemContext {

    private final int pointerDepth;

    private String methodName;
    private long executionTime;
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
}
