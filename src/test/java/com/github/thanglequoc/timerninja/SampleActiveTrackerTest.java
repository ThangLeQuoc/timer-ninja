package com.github.thanglequoc.timerninja;

import org.junit.jupiter.api.Test;

public class SampleActiveTrackerTest {

    @Test
    public void testMethodWithReturnType() {
        SampleActiveTracker activeTracker = new SampleActiveTracker();
        activeTracker.getAccountNumber("tle", 12);
    }

    @Test
    public void testMethodWithVoidType() {
        SampleActiveTracker activeTracker = new SampleActiveTracker();
        activeTracker.doTransferAccount(12, "ABCEA1", new Bank());
    }
}
