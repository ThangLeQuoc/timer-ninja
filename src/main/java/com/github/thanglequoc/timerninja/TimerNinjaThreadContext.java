package com.github.thanglequoc.timerninja;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Timer Ninja Thread context
 * */
public class TimerNinjaThreadContext {

    private int pointerDepth;
    private Map<String, TrackerItemContext> itemContextMap;

    public TimerNinjaThreadContext() {
        pointerDepth = 0;
        itemContextMap = new LinkedHashMap<>();
    }

    public int getPointerDepth() {
        return pointerDepth;
    }

    public int increasePointerDepth() {
        return ++pointerDepth;
    }

    public int decreasePointerDepth() {
        return --pointerDepth;
    }

    public Map<String, TrackerItemContext> getItemContextMap() {
        return itemContextMap;
    }

    public void addItemContext(String uuid, TrackerItemContext trackerItemContext) {
        this.itemContextMap.put(uuid, trackerItemContext);
    }
}
