package com.github.thanglequoc.timerninja;

import java.util.List;

import com.github.thanglequoc.timerninja.extension.LogCaptureExtension;
import com.github.thanglequoc.timerninja.servicesample.BankService;
import com.github.thanglequoc.timerninja.servicesample.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimerNinjaIntegrationTest {

    @RegisterExtension
    private LogCaptureExtension logCaptureExtension = new LogCaptureExtension();

    @Test
    public void testTrackingOnConstructor() {
        BankService bankService = new BankService();
        List<String> formattedMessages = logCaptureExtension.getFormattedMessages();
        assertFalse(formattedMessages.isEmpty());
        assertTrue(formattedMessages.get(0).contains("Timer Ninja trace context id:"));
        assertTrue(formattedMessages.get(1).contains("Trace timestamp:"));
        assertTrue(formattedMessages.get(2).contains("{===== Start of trace context id:"));

        // it should follow the constructor initialization order
        assertTrue(formattedMessages.get(3).contains("public BankService() -"));
        assertTrue(formattedMessages.get(4).contains("  |-- public CardService() -"));
        assertTrue(formattedMessages.get(5).contains("  |-- public PaymentService(CardService cardService, NotificationService notificationService) -"));
        assertTrue(formattedMessages.get(6).contains("  |-- public UserService() -"));

        assertTrue(formattedMessages.get(7).contains("{====== End of trace context id:"));
    }

    @Test
    public void testTrackingOnConstructor_TrackerDisabled() {
        // traker on notification service is disabled
        NotificationService notificationService = new NotificationService();
        List<String> formattedMessages = logCaptureExtension.getFormattedMessages();
        assertFalse(formattedMessages.isEmpty());

        assertTrue(formattedMessages.get(0).contains("Timer Ninja trace context id:"));
        assertTrue(formattedMessages.get(1).contains("Trace timestamp:"));
        assertTrue(formattedMessages.get(2).contains("There isn't any tracker enabled in the tracking context"));
    }
}
