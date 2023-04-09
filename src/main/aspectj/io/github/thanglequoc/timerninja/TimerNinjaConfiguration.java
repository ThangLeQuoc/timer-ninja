package io.github.thanglequoc.timerninja;

/**
 * The singleton to store TimerNinja configuration
 * */
public class TimerNinjaConfiguration {

    private static TimerNinjaConfiguration instance;

    private boolean enabledSystemOutLog;

    private TimerNinjaConfiguration() {
        enabledSystemOutLog = false;
    }

    /**
     * Get the singleton configuration instance of Timer Ninja
     * @return The singleton instance
     * */
    public static TimerNinjaConfiguration getInstance() {
        if (instance == null) {
            instance = new TimerNinjaConfiguration();
        }
        return instance;
    }

    /**
     * By default, TimerNinja prints the result with Slf4 logging API.<br>
     * This option is for consumer that doesn't use any java logger provider.<br>
     * Toggles the option to print timing trace results to System.out print stream in addition to the default logging using Slf4j.
     *
     * @param enabledSystemOutLogging true to enable printing to System.out, false otherwise.
     * */
    public synchronized void toggleSystemOutLog(boolean enabledSystemOutLogging) {
        this.enabledSystemOutLog = enabledSystemOutLogging;
    }

    /**
     * Check if TimerNinja will also print the log trace to System.out in addition to the default logging using Slf4j
     * @return flag indicates if System.out output is enabled
     * */
    public boolean isSystemOutLogEnabled() {
        return enabledSystemOutLog;
    }
}
