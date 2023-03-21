package com.github.thanglequoc.timerninja;

class TrackerItemContext {

    private int pointerDepth;
    private String methodExecutionResult;

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
