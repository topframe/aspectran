/*
 * Copyright (c) 2008-2021 The Aspectran Project
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
package com.aspectran.shell.command.builtins;

import com.aspectran.shell.command.AbstractCommand;
import com.aspectran.shell.command.CommandRegistry;
import com.aspectran.shell.command.option.Arguments;
import com.aspectran.shell.command.option.Option;
import com.aspectran.shell.command.option.ParsedOptions;
import com.aspectran.shell.console.Console;

/**
 * Turns on or off the mode that displays a description of the translet before it is executed.
 */
public class VerboseCommand extends AbstractCommand {

    private static final String NAMESPACE = "builtins";

    private static final String COMMAND_NAME = "verbose";

    private final CommandDescriptor descriptor = new CommandDescriptor();

    public VerboseCommand(CommandRegistry registry) {
        super(registry);

        addOption(Option.builder("h")
                .longName("help")
                .desc("Display help for this command")
                .build());

        Arguments arguments = touchArguments();
        arguments.setTitle("Commands:");
        arguments.put("on", "Enable verbose mode");
        arguments.put("off", "Disable verbose mode");
    }

    @Override
    public void execute(ParsedOptions options, Console console) throws Exception {
        if (options.hasOption("help")) {
            printHelp(console);
        } else if (options.hasArgs()) {
            String arg = options.getFirstArg();
            if ("on".equals(arg)) {
                getService().setVerbose(true);
                console.writeLine("Verbose mode is enabled.");
                console.writeLine("Displays a description of the translet before it is executed.");
            } else if ("off".equals(arg)) {
                getService().setVerbose(false);
                console.writeLine("Verbose mode is disabled.");
                console.writeLine("Do not displays a description of the translet before it is executed.");
            } else {
                console.writeError("Unknown command '" + String.join(" ", options.getArgs()) + "'");
                printQuickHelp(console);
            }
        } else {
            printQuickHelp(console);
        }
    }

    @Override
    public Descriptor getDescriptor() {
        return descriptor;
    }

    private static class CommandDescriptor implements Descriptor {

        @Override
        public String getNamespace() {
            return NAMESPACE;
        }

        @Override
        public String getName() {
            return COMMAND_NAME;
        }

        @Override
        public String getDescription() {
            return "Turns on or off the translet description display feature";
        }

        @Override
        public String getUsage() {
            return null;
        }

    }

}
