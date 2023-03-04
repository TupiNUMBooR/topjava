package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

import static org.slf4j.LoggerFactory.getLogger;

public class TimeLoggerTestWatcher extends TestWatcher {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("ss.SSS");
    private final Logger log = getLogger(getClass());
    private long start;

    @Override
    protected void starting(Description description) {
        start = System.nanoTime();
    }

    @Override
    protected void finished(Description description) {
        Temporal time = LocalTime.ofNanoOfDay(System.nanoTime() - start);
        log.info("Finished {} in {}", description.getMethodName(), FORMATTER.format(time));
    }
}
