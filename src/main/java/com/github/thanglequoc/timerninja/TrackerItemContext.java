package com.github.thanglequoc.timerninja;

public class TrackerItemContext {

    private final int pointerDepth;
    private final String methodExecutionResult;

    TrackerItemContext(int pointerDepth, String methodExecutionResult) {
        this.pointerDepth = pointerDepth;
        this.methodExecutionResult = methodExecutionResult;
    }

    public int getPointerDepth() {
        return pointerDepth;
    }

    public String getMethodExecutionResult() {
        return methodExecutionResult;
    }
}
