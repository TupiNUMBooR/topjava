package ru.javawebinar.topjava.service;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

// http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
public class StopwatchLogger extends Stopwatch {
    private static final Logger log = getLogger("result");

    private static final StringBuilder results = new StringBuilder();

    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");

        // Поскольку классы тестов запускаются по очереди, оно работает.
        // Не уверен, что так делать правильно, но это же тесты? Костыли тут в порядке вещей?
        results.delete(0, results.length());
    }

    @Override
    protected void finished(long nanos, Description description) {
        String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        results.append(result);
        log.info(result + " ms\n");
    }
}
