package io.github.thanglequoc.timerninja;

import java.util.List;

import io.github.thanglequoc.timerninja.extension.LogCaptureExtension;
import io.github.thanglequoc.timerninja.servicesample.BankService;
import io.github.thanglequoc.timerninja.servicesample.constructorscenario.LocationService;
import io.github.thanglequoc.timerninja.servicesample.constructorscenario.TransportationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimerNinjaIntegrationTest {

    @RegisterExtension
    private LogCaptureExtension logCaptureExtension = new LogCaptureExtension();

    @Test
    public void testTrackingOnConstructor() {
        TransportationService transportationService = new TransportationService();
        List<String> formattedMessages = logCaptureExtension.getFormattedMessages();
        assertFalse(formattedMessages.isEmpty());
        assertTrue(formattedMessages.get(0).contains("Timer Ninja trace context id:"));
        assertTrue(formattedMessages.get(1).contains("Trace timestamp:"));
        assertTrue(formattedMessages.get(2).contains("{===== Start of trace context id:"));

        // it should follow the constructor initialization order
        assertTrue(formattedMessages.get(3).contains("public TransportationService() -"));
        assertTrue(formattedMessages.get(4).contains("  |-- public ShippingService() -"));

        assertTrue(formattedMessages.get(5).contains("{====== End of trace context id:"));
    }

    @Test
    public void testTrackingOnConstructor_TrackerDisabled() {
        // traker on location service is disabled
        LocationService locationService = new LocationService();
        List<String> formattedMessages = logCaptureExtension.getFormattedMessages();
        assertFalse(formattedMessages.isEmpty());

        assertTrue(formattedMessages.get(0).contains("Timer Ninja trace context id:"));
        assertTrue(formattedMessages.get(1).contains("Trace timestamp:"));
        assertTrue(formattedMessages.get(2).contains("There isn't any tracker enabled in the tracking context"));
    }

    /* Integration testing */
    @Test
    public void testTrackingOnMethods() {
        BankService bankService = new BankService();
        bankService.requestMoneyTransfer(1, 2, 3000);

        List<String> formattedMessages = logCaptureExtension.getFormattedMessages();
        assertFalse(formattedMessages.isEmpty());

        assertTrue(formattedMessages.get(0).contains("Timer Ninja trace context id:"));
        assertTrue(formattedMessages.get(1).contains("Trace timestamp:"));
        assertTrue(formattedMessages.get(2).contains("{===== Start of trace context id:"));
        assertTrue(formattedMessages.get(3).contains("public void requestMoneyTransfer(int sourceUserId, int targetUserId, int amount) -"));
        assertTrue(formattedMessages.get(4).contains("  |-- public User findUser(int userId) -"));
        assertTrue(formattedMessages.get(5).contains("  |-- public User findUser(int userId) -"));
        assertTrue(formattedMessages.get(6).contains("  |-- public void processPayment(User user, int amount) -"));
        assertTrue(formattedMessages.get(7).contains("    |-- public boolean changeAmount(User user, int amount) -"));
        assertTrue(formattedMessages.get(8).contains("    |-- public void notify(User user) -"));
        assertTrue(formattedMessages.get(9).contains("      |-- private void notifyViaSMS(User user) -"));
        assertTrue(formattedMessages.get(10).contains("     |-- private void notifyViaEmail(User user) -"));
        assertTrue(formattedMessages.get(11).contains(" |-- public void processPayment(User user, int amount) -"));
        assertTrue(formattedMessages.get(12).contains("   |-- public boolean changeAmount(User user, int amount) -"));
        assertTrue(formattedMessages.get(13).contains("   |-- public void notify(User user) -"));
        assertTrue(formattedMessages.get(14).contains("     |-- private void notifyViaSMS(User user) -"));
        assertTrue(formattedMessages.get(15).contains("     |-- private void notifyViaEmail(User user) -"));
        assertTrue(formattedMessages.get(16).contains("{====== End of trace context id:"));
    }
}
