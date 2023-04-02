package com.github.thanglequoc.timerninja;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimerNinjaThreadContextTest {

    private TimerNinjaThreadContext threadContext;

    @BeforeEach
    private void beforeEach() {
        threadContext = new TimerNinjaThreadContext();
    }

    @Test
    public void testInitialization() {
        Instant beforeRun = Instant.now();
        threadContext = new TimerNinjaThreadContext();
        assertFalse(threadContext.getTraceContextId().isBlank());
        Instant creationTime = threadContext.getCreationTime();
        assertTrue(creationTime.isAfter(beforeRun));
        assertTrue(threadContext.getItemContextMap().isEmpty());
        assertEquals(0, threadContext.getPointerDepth());
    }

    @Test
    public void testIncreasePointerDepth() {
        threadContext.increasePointerDepth();
        threadContext.increasePointerDepth();
        assertEquals(2, threadContext.getPointerDepth());
    }

    @Test
    public void testDecreasePointerDepth() {
        threadContext.increasePointerDepth();
        threadContext.increasePointerDepth();
        threadContext.decreasePointerDepth();
        assertEquals(1, threadContext.getPointerDepth());
    }

    @Test
    public void testAddItemContextToMap() {
        TrackerItemContext itemContext = new TrackerItemContext(1, "public boolean deductAmount(User user, int amount)");
        threadContext.addItemContext("123-abc-456", itemContext);
        assertTrue(threadContext.getItemContextMap().containsValue(itemContext));
    }
}
