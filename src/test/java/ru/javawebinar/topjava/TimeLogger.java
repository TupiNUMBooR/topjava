package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class TimeLogger extends Stopwatch {
    private final Logger logger = getLogger(getClass());
    private final StringBuilder logString;

    public TimeLogger(StringBuilder logString) {
        super();
        this.logString = logString;
    }

    @Override
    protected void finished(long n, Description d) {
        long ms = runtime(TimeUnit.MILLISECONDS);
        String name = d.getMethodName();
        logString.append('\n').append(String.format("%-25s %4d ms", name, ms));
        logger.debug("{}.{} {} ms", d.getTestClass().getSimpleName(), name, ms);
    }
}
