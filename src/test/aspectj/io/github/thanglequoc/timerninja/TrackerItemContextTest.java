package io.github.thanglequoc.timerninja;

import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrackerItemContextTest {

    @Test
    public void testToString() {
        TrackerItemContext itemContext = new TrackerItemContext(12, "public boolean deductAmount(User user, int amount)");
        itemContext.setTimeUnit(ChronoUnit.MILLIS);
        itemContext.setExecutionTime(260);
        String expectedResult = "TrackerItemContext{pointerDepth=12, methodName='public boolean deductAmount(User user, int amount)', executionTime=260, timeUnit=Millis, args=[null]}";
        assertEquals(expectedResult, itemContext.toString());
        TimerNinjaConfiguration.getInstance().toggleSystemOutLog(true);
    }

    @Test
    public void testToString_WithArgs() {
        TrackerItemContext itemContext = new TrackerItemContext(30, "public void processPayment(User user, int amount)");
        itemContext.setTimeUnit(ChronoUnit.MILLIS);
        itemContext.setExecutionTime(100);
        itemContext.setArguments("user={name='John Doe', age=30}, amount={500}");

        String expectedResult = "TrackerItemContext{pointerDepth=30, methodName='public void processPayment(User user, int amount)', executionTime=100, timeUnit=Millis, args=[user={name='John Doe', age=30}, amount={500}]}";
        assertEquals(expectedResult, itemContext.toString());
    }
}
