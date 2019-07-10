/*
 * Copyright (c) 2008-2019 The Aspectran Project
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
package com.aspectran.core.context.config;

import com.aspectran.core.util.SystemUtils;
import com.aspectran.core.util.apon.AbstractParameters;
import com.aspectran.core.util.apon.AponReader;
import com.aspectran.core.util.apon.ParameterKey;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AspectranConfig extends AbstractParameters {

    public static final String BASE_PATH_PROPERTY_NAME = "aspectran.basePath";
    public static final String DEFAULT_ASPECTRAN_CONFIG_FILE = "aspectran-config.apon";
    public static final String DEFAULT_APP_CONFIG_ROOT_FILE = "classpath:app-config.xml";

    private static final ParameterKey context;
    private static final ParameterKey session;
    private static final ParameterKey scheduler;
    private static final ParameterKey daemon;
    private static final ParameterKey shell;
    private static final ParameterKey web;

    private static final ParameterKey[] parameterKeys;

    static {
        context = new ParameterKey("context", ContextConfig.class);
        session = new ParameterKey("session", SessionConfig.class);
        scheduler = new ParameterKey("scheduler", SchedulerConfig.class);
        daemon = new ParameterKey("daemon", DaemonConfig.class);
        shell = new ParameterKey("shell", ShellConfig.class);
        web = new ParameterKey("web", WebConfig.class);

        parameterKeys = new ParameterKey[] {
                context,
                session,
                scheduler,
                daemon,
                shell,
                web
        };
    }

    public AspectranConfig() {
        super(parameterKeys);
    }

    public AspectranConfig(String text) throws IOException {
        this();
        readFrom(text);
    }

    public AspectranConfig(File configFile) throws IOException {
        this();
        AponReader.parse(configFile, this);
    }

    public AspectranConfig(Reader reader) throws IOException {
        this();
        AponReader.parse(reader, this);
    }

    public ContextConfig getContextConfig() {
        return getParameters(context);
    }

    public ContextConfig newContextConfig() {
        return newParameters(context);
    }

    public ContextConfig touchContextConfig() {
        return touchParameters(context);
    }

    public boolean hasContextConfig() {
        return hasValue(context);
    }

    public SessionConfig getSessionConfig() {
        return getParameters(session);
    }

    public SessionConfig newSessionConfig() {
        return newParameters(session);
    }

    public SessionConfig touchSessionConfig() {
        return touchParameters(session);
    }

    public SchedulerConfig getSchedulerConfig() {
        return getParameters(scheduler);
    }

    public SchedulerConfig newSchedulerConfig() {
        return newParameters(scheduler);
    }

    public SchedulerConfig touchSchedulerConfig() {
        return touchParameters(scheduler);
    }

    public DaemonConfig getDaemonConfig() {
        return getParameters(daemon);
    }

    public DaemonConfig newDaemonConfig() {
        return newParameters(daemon);
    }

    public DaemonConfig touchDaemonConfig() {
        return touchParameters(daemon);
    }

    public ShellConfig getShellConfig() {
        return getParameters(shell);
    }

    public ShellConfig newShellConfig() {
        return newParameters(shell);
    }

    public ShellConfig touchShellConfig() {
        return touchParameters(shell);
    }

    public WebConfig getWebConfig() {
        return getParameters(web);
    }

    public WebConfig newWebConfig() {
        return newParameters(web);
    }

    public WebConfig touchWebConfig() {
        return touchParameters(web);
    }

    public static String determineBasePath(String[] args) {
        if (args == null || args.length < 2) {
            return SystemUtils.getProperty(BASE_PATH_PROPERTY_NAME);
        } else {
            return args[0];
        }
    }

    public static File determineAspectranConfigFile(String[] args) {
        File file;
        if (args == null || args.length == 0) {
            String baseDir = SystemUtils.getProperty(BASE_PATH_PROPERTY_NAME);
            if (baseDir != null) {
                file = new File(baseDir, DEFAULT_ASPECTRAN_CONFIG_FILE);
            } else {
                file = new File(DEFAULT_ASPECTRAN_CONFIG_FILE);
            }
        } else if (args.length == 1) {
            String baseDir = SystemUtils.getProperty(BASE_PATH_PROPERTY_NAME);
            if (baseDir != null) {
                Path basePath = Paths.get(baseDir);
                Path filePath = Paths.get(args[0]);
                if (filePath.startsWith(basePath) && filePath.isAbsolute()) {
                    file = filePath.toFile();
                } else {
                    file = new File(baseDir, args[0]);
                }
            } else {
                file = new File(args[0]);
            }
        } else {
            file = new File(args[0], args[1]);
        }
        return file;
    }

}
