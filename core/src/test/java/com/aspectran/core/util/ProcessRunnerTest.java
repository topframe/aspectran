package com.aspectran.core.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>Created: 2019/12/02</p>
 */
class ProcessRunnerTest {

    @Test
    void testEcho() throws IOException, InterruptedException {
        ProcessRunner runner = new ProcessRunner();
        StringWriter writer = new StringWriter();
        PrintWriter errOut = new PrintWriter(writer);
        runner.run(new String[] {"echo", "hello"}, errOut);
    }

}
