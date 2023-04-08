package io.github.thanglequoc.timerninja;

import java.time.temporal.ChronoUnit;
import java.util.List;

import io.github.thanglequoc.timerninja.extension.LogCaptureExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimerNinjaUtilTest {

    @RegisterExtension
    private LogCaptureExtension logCaptureExtension = new LogCaptureExtension();

    @Test
    public void testLogTimerContextTrace() {
        TimerNinjaThreadContext timerNinjaThreadContext = createTimerNinjaThreadContext();
        TimerNinjaUtil.logTimerContextTrace(timerNinjaThreadContext);
        assertFalse(logCaptureExtension.getLoggingEvent().isEmpty());

        List<String> loggingMessages = logCaptureExtension.getFormattedMessages();
        assertFalse(loggingMessages.isEmpty());
        assertEquals(7, loggingMessages.size());
        String contextMessage = loggingMessages.get(0);

        String traceContextId = timerNinjaThreadContext.getTraceContextId();

        assertEquals(String.format("Timer Ninja trace context id: %s", traceContextId), contextMessage);
        assertTrue(loggingMessages.get(1).contains("Trace timestamp"));
        assertEquals(
            String.format("{===== Start of trace context id: %s =====}", traceContextId),
            loggingMessages.get(2)
        );
        assertEquals("public void processPayment(User user, int amount) - 500 ms", loggingMessages.get(3));
        assertEquals("  |-- public boolean deductAmount(User user, int amount) - 100 ms", loggingMessages.get(4));
        assertEquals("  |-- public void notify(User user) - 150 ms", loggingMessages.get(5));
        assertEquals(
            String.format("{====== End of trace context id: %s ======}", traceContextId),
            loggingMessages.get(6)
        );
    }

    @Test
    public void testLogTimerContextTrace_EmptyContextItems() {
        TimerNinjaThreadContext timerNinjaThreadContext = createEmptyContext();
        TimerNinjaUtil.logTimerContextTrace(timerNinjaThreadContext);

        List<String> loggingMessages = logCaptureExtension.getFormattedMessages();
        assertFalse(loggingMessages.isEmpty());

        String traceContextId = timerNinjaThreadContext.getTraceContextId();

        assertEquals(String.format("Timer Ninja trace context id: %s", traceContextId), loggingMessages.get(0));
        assertTrue(loggingMessages.get(1).contains("Trace timestamp"));
        assertTrue(loggingMessages.get(2).contains("There isn't any tracker enabled in the tracking context"));
    }

    @Test
    public void testConvertFromMillis() {
        assertEquals(8, TimerNinjaUtil.convertFromMillis(8000, ChronoUnit.SECONDS));
        assertEquals(8000000, TimerNinjaUtil.convertFromMillis(8000, ChronoUnit.MICROS));
        assertEquals(8000, TimerNinjaUtil.convertFromMillis(8000, ChronoUnit.MILLIS));
    }

    @Test
    public void testConvertFromMillis_NotSupportedUnit() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> TimerNinjaUtil.convertFromMillis(8, ChronoUnit.DAYS));
        assertEquals("Time unit not supported", exception.getMessage());
    }

    private TimerNinjaThreadContext createTimerNinjaThreadContext() {
        TimerNinjaThreadContext threadContext = new TimerNinjaThreadContext();

        TrackerItemContext processPaymentItem = new TrackerItemContext(0, "public void processPayment(User user, int amount)");
        processPaymentItem.setTimeUnit(ChronoUnit.MILLIS);
        processPaymentItem.setExecutionTime(500);

        TrackerItemContext deductItem = new TrackerItemContext(1, "public boolean deductAmount(User user, int amount)");
        deductItem.setTimeUnit(ChronoUnit.MILLIS);
        deductItem.setExecutionTime(100);

        TrackerItemContext notifyTracker = new TrackerItemContext(1, "public void notify(User user)");
        notifyTracker.setTimeUnit(ChronoUnit.MILLIS);
        notifyTracker.setExecutionTime(150);

        threadContext.addItemContext("123-aaa-dd", processPaymentItem);
        threadContext.addItemContext("123-aaa-ee", deductItem);
        threadContext.addItemContext("123-aaa-ff", notifyTracker);
        return threadContext;
    }

    private TimerNinjaThreadContext createEmptyContext() {
        TimerNinjaThreadContext threadContext = new TimerNinjaThreadContext();
        return threadContext;
    }
}
