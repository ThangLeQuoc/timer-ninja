package com.github.thanglequoc.timerninja;

public class TimerNinjaConfiguration {

    private static TimerNinjaConfiguration instance;

    private boolean enabledSystemOutLog;

    private TimerNinjaConfiguration() {
        enabledSystemOutLog = false;
    }

    public static TimerNinjaConfiguration getInstance() {
        if (instance == null) {
            instance = new TimerNinjaConfiguration();
        }
        return instance;
    }

    public synchronized void toggleSystemOutLog(boolean enabledSystemOutLogging) {
        this.enabledSystemOutLog = enabledSystemOutLogging;
    }

    public boolean isSystemOutLogEnabled() {
        return enabledSystemOutLog;
    }
}
