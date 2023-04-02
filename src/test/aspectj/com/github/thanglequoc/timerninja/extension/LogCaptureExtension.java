package com.github.thanglequoc.timerninja.extension;

import java.util.List;
import java.util.stream.Collectors;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.LoggerFactory;

public class LogCaptureExtension implements BeforeEachCallback, AfterEachCallback {

    private final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    private final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final Logger logger = loggerContext.exists(org.slf4j.Logger.ROOT_LOGGER_NAME);
        final Level newLevel = Level.toLevel("INFO", null);
        logger.setLevel(newLevel);

        logger.addAppender(listAppender);
        listAppender.start();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        listAppender.stop();
        listAppender.list.clear();
        logger.detachAppender(listAppender);
    }

    public List<String> getMessages() {
        return listAppender.list.stream().map(e -> e.getMessage()).collect(Collectors.toList());
    }

    public List<String> getFormattedMessages() {
        return listAppender.list.stream().map(e -> e.getFormattedMessage()).collect(Collectors.toList());
    }

    public List<ILoggingEvent> getLoggingEvent() {
        return listAppender.list;
    }
}
