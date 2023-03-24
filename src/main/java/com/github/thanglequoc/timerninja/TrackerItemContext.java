package com.github.thanglequoc.timerninja;

public class TrackerItemContext {

    private final int pointerDepth;

    private String methodName;
    private int executionTime;

    TrackerItemContext(int pointerDepth, String methodName) {
        this.pointerDepth = pointerDepth;
        this.methodName = methodName;
    }

    public int getPointerDepth() {
        return pointerDepth;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public String getMethodName() {
        return methodName;
    }
}
