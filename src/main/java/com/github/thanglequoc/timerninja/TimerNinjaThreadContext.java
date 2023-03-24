package com.github.thanglequoc.timerninja;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Timer Ninja Thread context
 * */
public class TimerNinjaThreadContext {

    private String traceContextId;

    private Instant creationTime;

    private int pointerDepth;
    private Map<String, TrackerItemContext> itemContextMap;

    public TimerNinjaThreadContext() {
        traceContextId = UUID.randomUUID().toString();
        creationTime = Instant.now();
        pointerDepth = 0;
        itemContextMap = new LinkedHashMap<>();
    }

    public int getPointerDepth() {
        return pointerDepth;
    }

    public String getTraceContextId() {
        return traceContextId;
    }

    public Instant getCreationTime() {
        return creationTime;
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
