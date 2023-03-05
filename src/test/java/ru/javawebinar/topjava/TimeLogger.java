package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class TimeLogger extends Stopwatch {
    private final Logger log = getLogger(getClass());

    @Override
    protected void finished(long n, Description d) {
        super.finished(n, d);
        log.info("Finished {} in {}ms", d.getMethodName(), runtime(TimeUnit.MILLISECONDS));
    }
}
