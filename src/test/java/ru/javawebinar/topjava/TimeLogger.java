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
        super.finished(n, d);
        String e = String.format("%s.%s done in %dms",
                d.getClassName().substring(d.getClassName().lastIndexOf('.') + 1),
                d.getMethodName(), runtime(TimeUnit.MILLISECONDS));
        logger.debug(e);
        logString.append('\n').append(e);
    }
}
