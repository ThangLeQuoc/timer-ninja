package io.github.thanglequoc.timerninja;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.thanglequoc.timerninja.extension.LogCaptureExtension;
import io.github.thanglequoc.timerninja.servicesample.BankService;
import io.github.thanglequoc.timerninja.servicesample.constructorscenario.LocationService;
import io.github.thanglequoc.timerninja.servicesample.constructorscenario.TransportationService;
import io.github.thanglequoc.timerninja.servicesample.entities.BankRecordBook;
import io.github.thanglequoc.timerninja.servicesample.entities.User;
import io.github.thanglequoc.timerninja.servicesample.services.BalanceService;
import io.github.thanglequoc.timerninja.servicesample.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TimerNinjaIntegrationTest {

    @RegisterExtension
    private LogCaptureExtension logCaptureExtension = new LogCaptureExtension();

    @Test
    public void testTrackingOnConstructor() {
        TransportationService transportationService = new TransportationService();
        List<String> formattedMessages = logCaptureExtension.getFormattedMessages();
        assertFalse(formattedMessages.isEmpty());
        assertTrue(formattedMessages.get(0).contains("Timer Ninja trace context id:"));
        assertTrue(formattedMessages.get(0).contains("Trace timestamp:"));
        assertTrue(formattedMessages.get(1).contains("{===== Start of trace context id:"));

        // it should follow the constructor initialization order
        assertTrue(formattedMessages.get(2).contains("public TransportationService() -"));
        assertTrue(formattedMessages.get(3).contains("  |-- public ShippingService() -"));

        assertTrue(formattedMessages.get(4).contains("{====== End of trace context id:"));
    }

    @Test
    public void testTrackingOnConstructor_TrackerDisabled() {
        // traker on location service is disabled
        LocationService locationService = new LocationService();
        List<String> formattedMessages = logCaptureExtension.getFormattedMessages();
        assertFalse(formattedMessages.isEmpty());

        assertTrue(formattedMessages.get(0).contains("Timer Ninja trace context id:"));
        assertTrue(formattedMessages.get(0).contains("Trace timestamp:"));
        assertTrue(formattedMessages.get(1).contains("There isn't any tracker enabled in the tracking context"));
    }

    /* Integration testing */
    @Test
    public void testTrackingOnMethods() {
        BankService bankService = new BankService();
        bankService.requestMoneyTransfer(1, 2, 3000);

        List<String> formattedMessages = logCaptureExtension.getFormattedMessages();
        assertFalse(formattedMessages.isEmpty());

        // Trace Context 1
        assertTrue(formattedMessages.get(0).startsWith("Timer Ninja trace context id:"));
        assertTrue(formattedMessages.get(0).contains("Trace timestamp:"));
        assertTrue(formattedMessages.get(1).startsWith("{===== Start of trace context id:"));
        assertTrue(formattedMessages.get(2).contains("private void initBankRecordBook()"));
        assertTrue(formattedMessages.get(3).startsWith("{====== End of trace context id:"));

        // Trace Context 2
        assertTrue(formattedMessages.get(4).startsWith("Timer Ninja trace context id:"));
        assertTrue(formattedMessages.get(4).contains("Trace timestamp:"));
        assertTrue(formattedMessages.get(5).startsWith("{===== Start of trace context id:"));
        assertTrue(formattedMessages.get(6).startsWith("public void requestMoneyTransfer(int sourceUserId, int targetUserId, int amount)"));
        assertTrue(formattedMessages.get(6).contains("[Threshold Exceed !!:"));

        assertTrue(formattedMessages.get(7).startsWith("  |-- public User findUser(int userId) - "));
        assertTrue(formattedMessages.get(8).startsWith("    |-- public Map getUserBalance() - "));
        assertTrue(formattedMessages.get(9).startsWith("  |-- public User findUser(int userId) - "));
        assertTrue(formattedMessages.get(10).startsWith("    |-- public Map getUserBalance() - "));
        assertTrue(formattedMessages.get(11).startsWith("  |-- public void increaseAmount(User user, int amount) - Args: [user="));
        assertTrue(formattedMessages.get(12).startsWith("    |-- public Map getUserBalance() -"));
        assertTrue(formattedMessages.get(13).startsWith("    |-- public void notify(User user) -"));
        assertTrue(formattedMessages.get(14).startsWith("      |-- private void notifyViaSMS(User user) -"));
        assertTrue(formattedMessages.get(15).startsWith("      |-- private void notifyViaEmail(User user) -"));
        assertTrue(formattedMessages.get(16).startsWith("{====== End of trace context id:"));
    }

    @Test
    public void testTrackingOnMethods_MethodWithinExecutionThreshold() {
        BankRecordBook recordBook = Mockito.mock(BankRecordBook.class);
        NotificationService notificationService = Mockito.mock(NotificationService.class);

        User user = new User(1, "User A", "dummy@gmail.com");
        Map<User, Integer> balanceSheet = new HashMap<>();
        balanceSheet.put(user, 1000);

        // Mock behavior
        when(recordBook.getUserBalance()).thenReturn(balanceSheet);

        BalanceService balanceService = new BalanceService(recordBook, notificationService);
        balanceService.deductAmount(user, 200);

        List<String> formattedMessages = logCaptureExtension.getFormattedMessages();
        assertFalse(formattedMessages.isEmpty());

        // @thangle: The output format for trace context that has all method meet the threshold does not look very good and still make the output
        // look redundant and feel like something is wrong that it's not printing out. We will come back and improve the trace output
        // for this case later.
        assertTrue(formattedMessages.get(0).startsWith("Timer Ninja trace context id:"));
        assertTrue(formattedMessages.get(0).contains("Trace timestamp:"));
        assertTrue(formattedMessages.get(1).startsWith("{===== Start of trace context id:"));
        assertTrue(formattedMessages.get(2).startsWith("{====== End of trace context id:"));

        // The nofication service is called but there is no trace output printing out, this is the expected behavior
        // because its parent method met the threshold setting
        verify(notificationService, times(1)).notify(user);
    }
}
