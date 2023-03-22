package com.github.thanglequoc.timerninja;

import java.util.Stack;

/**
 * Timer Ninja Thread context
 * */
public class TimerNinjaThreadContext {

    private int pointerDepth = 0; // TODO @tle 21/3/2023: Better move this to constructor?
    private Stack<TrackerItemContext> itemContexts = new Stack<>();

    public int getPointerDepth() {
        return pointerDepth;
    }

    public int increasePointerDepth() {
        return ++pointerDepth;
    }

    public int decreasePointerDepth() {
        return --pointerDepth;
    }

    public void addItemContext(TrackerItemContext trackerItemContext) {
        itemContexts.add(trackerItemContext);
    }

    public TrackerItemContext popItemContext() {
        return itemContexts.pop();
    }

    public boolean isItemContextEmpty() {
        return itemContexts.isEmpty();
    }
}
