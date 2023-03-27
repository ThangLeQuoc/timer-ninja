package com.github.thanglequoc.timerninja;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Timer Ninja Thread context, which store the current timer tracking state and tracker execution trace
 * */
public class TimerNinjaThreadContext {

    public static Logger LOGGER = LoggerFactory.getLogger(TimerNinjaThreadContext.class);

    private final String traceContextId;

    /**
     * The creation time since this thread context starts
     * */
    private final Instant creationTime;

    /**
     * The current pointer depth of the timer tracking context.<br/>
     * This depth increases for each nested annotated tracker method that is called within the current
     * method being evaluated.
     */
    private int pointerDepth;

    /**
     * Item context map to store the executing method being tracked. This contextMap will act as a stacktrace to be printed out later
     * */
    private Map<String, TrackerItemContext> itemContextMap;

    public TimerNinjaThreadContext() {
        traceContextId = UUID.randomUUID().toString();
        creationTime = Instant.now();
        pointerDepth = 0;
        itemContextMap = new LinkedHashMap<>();
    }

    /**
     * Get the current method pointer depth
     * */
    public int getPointerDepth() {
        return pointerDepth;
    }

    /**
     * Get the trace context id
     * */
    public String getTraceContextId() {
        return traceContextId;
    }

    /**
     * Get the tracking context creation time
     * */
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

    /**
     * Add the tracking method to the item context map
     * */
    public void addItemContext(String uuid, TrackerItemContext trackerItemContext) {
        this.itemContextMap.put(uuid, trackerItemContext);
    }
}
