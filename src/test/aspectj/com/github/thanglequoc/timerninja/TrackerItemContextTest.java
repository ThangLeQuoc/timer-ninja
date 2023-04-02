package com.github.thanglequoc.timerninja;

import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrackerItemContextTest {

    @Test
    public void testToString() {
        TrackerItemContext itemContext = new TrackerItemContext(12, "public boolean deductAmount(User user, int amount)");
        itemContext.setTimeUnit(ChronoUnit.MILLIS);
        itemContext.setExecutionTime(260);
        String expectedResult = "TrackerItemContext{pointerDepth=12, methodName='public boolean deductAmount(User user, int amount)', executionTime=260, timeUnit=Millis}";
        assertEquals(expectedResult, itemContext.toString());
    }
}
