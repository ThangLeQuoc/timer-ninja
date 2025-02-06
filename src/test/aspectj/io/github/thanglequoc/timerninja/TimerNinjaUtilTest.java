package io.github.thanglequoc.timerninja;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.temporal.ChronoUnit;
import java.util.List;

import io.github.thanglequoc.timerninja.extension.LogCaptureExtension;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TimerNinjaUtilTest {

    @RegisterExtension
    private LogCaptureExtension logCaptureExtension = new LogCaptureExtension();

    @Test
    public void testIsTimerNinjaTrackerEnabled_MethodSignature_InvalidMethodSignature() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                TimerNinjaUtil.isTimerNinjaTrackerEnabled((MethodSignature) null));
        assertEquals("MethodSignature must be present", exception.getMessage());
    }

    @Test
    public void testIsTimerNinjaTrackerEnabled_MethodSignature() {
        MethodSignature mockMethodSgnt = mock(MethodSignature.class);
        Method mockMethod = mock(Method.class);
        when(mockMethodSgnt.getMethod()).thenReturn(mockMethod);
        TimerNinjaTracker mockTracker = mock(TimerNinjaTracker.class);

        // when the tracker is enabled
        when(mockTracker.enabled()).thenReturn(true);
        when(mockMethod.getAnnotation(TimerNinjaTracker.class)).thenReturn(mockTracker);
        assertTrue(TimerNinjaUtil.isTimerNinjaTrackerEnabled(mockMethodSgnt));

        // when the tracker is disabled
        when(mockTracker.enabled()).thenReturn(false);
        when(mockMethod.getAnnotation(TimerNinjaTracker.class)).thenReturn(mockTracker);
        assertFalse(TimerNinjaUtil.isTimerNinjaTrackerEnabled(mockMethodSgnt));
    }

    @Test
    public void testIsTimerNinjaTrackerEnabled_ConstructorSignature_InvalidMethodSignature() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                TimerNinjaUtil.isTimerNinjaTrackerEnabled((ConstructorSignature) null));
        assertEquals("ConstructorSignature must be present", exception.getMessage());
    }

    @Test
    public void testIsTimerNinjaTrackerEnabled_ConstructorSignature() {
        ConstructorSignature constructorSgnt = mock(ConstructorSignature.class);
        Constructor mockConstructor = mock(Constructor.class);
        when(constructorSgnt.getConstructor()).thenReturn(mockConstructor);

        TimerNinjaTracker mockTracker = mock(TimerNinjaTracker.class);

        // when the tracker is enabled
        when(mockTracker.enabled()).thenReturn(true);
        when(mockConstructor.getAnnotation(TimerNinjaTracker.class)).thenReturn(mockTracker);
        assertTrue(TimerNinjaUtil.isTimerNinjaTrackerEnabled(constructorSgnt));

        // when the tracker is disabled
        when(mockTracker.enabled()).thenReturn(false);
        when(mockConstructor.getAnnotation(TimerNinjaTracker.class)).thenReturn(mockTracker);
        assertFalse(TimerNinjaUtil.isTimerNinjaTrackerEnabled(constructorSgnt));
    }

    @Test
    public void testIsArgsIncluded_MethodSignature() {
        MethodSignature mockMethodSgnt = mock(MethodSignature.class);
        Method mockMethod = mock(Method.class);
        when(mockMethodSgnt.getMethod()).thenReturn(mockMethod);
        TimerNinjaTracker mockTracker = mock(TimerNinjaTracker.class);

        // when includeArgs is enabled
        when(mockTracker.includeArgs()).thenReturn(true);
        when(mockMethod.getAnnotation(TimerNinjaTracker.class)).thenReturn(mockTracker);
        assertTrue(TimerNinjaUtil.isArgsIncluded(mockMethodSgnt));

        // when includeArgs is disabled
        when(mockTracker.includeArgs()).thenReturn(false);
        when(mockMethod.getAnnotation(TimerNinjaTracker.class)).thenReturn(mockTracker);
        assertFalse(TimerNinjaUtil.isArgsIncluded(mockMethodSgnt));
    }

    @Test
    public void testIsArgsIncluded_ConstructorSignature() {
        ConstructorSignature constructorSgnt = mock(ConstructorSignature.class);
        Constructor mockConstructor = mock(Constructor.class);
        when(constructorSgnt.getConstructor()).thenReturn(mockConstructor);

        TimerNinjaTracker mockTracker = mock(TimerNinjaTracker.class);

        // when the tracker is enabled
        when(mockTracker.includeArgs()).thenReturn(true);
        when(mockConstructor.getAnnotation(TimerNinjaTracker.class)).thenReturn(mockTracker);
        assertTrue(TimerNinjaUtil.isArgsIncluded(constructorSgnt));

        // when the tracker is disabled
        when(mockTracker.includeArgs()).thenReturn(false);
        when(mockConstructor.getAnnotation(TimerNinjaTracker.class)).thenReturn(mockTracker);
        assertFalse(TimerNinjaUtil.isArgsIncluded(constructorSgnt));
    }

    




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
    public void testLogTimerContextTrace_WithArgsIncluded() {
        TimerNinjaThreadContext timerNinjaThreadContext = createTimerNinjaThreadContext_WithArgs();
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
        assertEquals("public void processPayment(User user, int amount) - Args: [user={name='John Doe', email=johndoe@gmail.com}, amount={500}] - 500 ms", loggingMessages.get(3));
        assertEquals("  |-- public boolean deductAmount(User user, int amount) - Args: [user={name='John Doe', email=johndoe@gmail.com}, amount={500}] - 100 ms", loggingMessages.get(4));
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
        processPaymentItem.setArguments("user={name='John Doe', email=johndoe@gmail.com}, amount={500}");

        TrackerItemContext deductItem = new TrackerItemContext(1, "public boolean deductAmount(User user, int amount)");
        deductItem.setTimeUnit(ChronoUnit.MILLIS);
        deductItem.setExecutionTime(100);
        deductItem.setArguments("user={name='John Doe', email=johndoe@gmail.com}, amount={500}");

        TrackerItemContext notifyTracker = new TrackerItemContext(1, "public void notify(User user)");
        notifyTracker.setTimeUnit(ChronoUnit.MILLIS);
        notifyTracker.setExecutionTime(150);
        notifyTracker.setArguments("user={name='Mary Jane', email=mary@gmail.com}");

        threadContext.addItemContext("123-aaa-dd", processPaymentItem);
        threadContext.addItemContext("123-aaa-ee", deductItem);
        threadContext.addItemContext("123-aaa-ff", notifyTracker);
        return threadContext;
    }

    private TimerNinjaThreadContext createTimerNinjaThreadContext_WithArgs() {
        TimerNinjaThreadContext threadContext = new TimerNinjaThreadContext();

        TrackerItemContext processPaymentItem = new TrackerItemContext(0,
                "public void processPayment(User user, int amount)",
                "user={name='John Doe', email=johndoe@gmail.com}, amount={500}",
                true);
        processPaymentItem.setTimeUnit(ChronoUnit.MILLIS);
        processPaymentItem.setExecutionTime(500);

        TrackerItemContext deductItem = new TrackerItemContext(1, "public boolean deductAmount(User user, int amount)", "user={name='John Doe', email=johndoe@gmail.com}, amount={500}", true);
        deductItem.setTimeUnit(ChronoUnit.MILLIS);
        deductItem.setExecutionTime(100);

        TrackerItemContext notifyTracker = new TrackerItemContext(1, "public void notify(User user)", "user={name='Mary Jane', email=mary@gmail.com}", false);
        notifyTracker.setTimeUnit(ChronoUnit.MILLIS);
        notifyTracker.setExecutionTime(150);

        threadContext.addItemContext("123-bbb-dd", processPaymentItem);
        threadContext.addItemContext("123-bbb-ee", deductItem);
        threadContext.addItemContext("123-bbb-ff", notifyTracker);
        return threadContext;
    }


    private TimerNinjaThreadContext createEmptyContext() {
        TimerNinjaThreadContext threadContext = new TimerNinjaThreadContext();
        return threadContext;
    }
}
