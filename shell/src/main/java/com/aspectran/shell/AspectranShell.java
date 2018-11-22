/*
 * Copyright (c) 2008-2018 The Aspectran Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aspectran.shell;

import com.aspectran.core.context.InsufficientEnvironmentException;
import com.aspectran.core.context.config.AspectranConfig;
import com.aspectran.core.util.ExceptionUtils;
import com.aspectran.shell.command.ShellCommander;
import com.aspectran.shell.console.Console;
import com.aspectran.shell.console.DefaultConsole;
import com.aspectran.shell.service.ShellService;

import java.io.File;

/**
 * Main entry point for the Aspectran Shell.
 *
 * @since 2016. 1. 17.
 */
public class AspectranShell {

    public static void main(String[] args) {
        String basePath = AspectranConfig.determineBasePath(args);
        File aspectranConfigFile = AspectranConfig.determineAspectranConfigFile(args);
        Console console = new DefaultConsole(basePath);
        bootstrap(aspectranConfigFile, console);
    }

    public static void bootstrap(File aspectranConfigFile, Console console) {
        ShellService shellService = null;
        int exitStatus = 0;

        try {
            shellService = ShellService.run(aspectranConfigFile, console);
            ShellCommander commander = new ShellCommander(shellService);
            commander.perform();
        } catch (Exception e) {
            Throwable t = ExceptionUtils.getRootCause(e);
            if (t instanceof InsufficientEnvironmentException) {
                System.err.println(((InsufficientEnvironmentException)t).getPrettyMessage());
            } else {
                e.printStackTrace(System.err);
            }
            exitStatus = 1;
        } finally {
            if (shellService != null) {
                shellService.release();
            }
        }

        System.exit(exitStatus);
    }

}
