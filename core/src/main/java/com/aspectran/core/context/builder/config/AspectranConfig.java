/*
 * Copyright 2008-2017 Juho Jeong
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
package com.aspectran.core.context.builder.config;

import com.aspectran.core.util.apon.AbstractParameters;
import com.aspectran.core.util.apon.ParameterDefinition;
import com.aspectran.core.util.apon.Parameters;

public class AspectranConfig extends AbstractParameters {

    public static final ParameterDefinition context;
    public static final ParameterDefinition session;
    public static final ParameterDefinition scheduler;
    public static final ParameterDefinition console;
    public static final ParameterDefinition web;

    private static final ParameterDefinition[] parameterDefinitions;

    static {
        context = new ParameterDefinition("context", ContextConfig.class);
        session = new ParameterDefinition("session", SessionConfig.class);
        scheduler = new ParameterDefinition("scheduler", SchedulerConfig.class);
        console = new ParameterDefinition("console", ConsoleConfig.class);
        web = new ParameterDefinition("web", WebConfig.class);

        parameterDefinitions = new ParameterDefinition[] {
            context,
            session,
            scheduler,
            console,
            web
        };
    }

    public AspectranConfig() {
        super(parameterDefinitions);
    }

    public AspectranConfig(String text) {
        super(parameterDefinitions, text);
    }

    public ContextConfig newContextConfig() {
        return newParameters(context);
    }

    public ContextConfig touchContextConfig() {
        return touchParameters(context);
    }

    public ContextConfig getContextConfig() {
        return getParameters(context);
    }

    public void putContextConfig(ContextConfig contextConfig) {
        putValue(context, contextConfig);
    }

    public SessionConfig newSessionConfig() {
        return newParameters(session);
    }

    public SessionConfig touchSessionConfig() {
        return touchParameters(session);
    }

    public SessionConfig getSessionConfig() {
        return getParameters(session);
    }

    public void putSessionConfig(SessionConfig sessionConfig) {
        putValue(session, sessionConfig);
    }

    public SchedulerConfig newSchedulerConfig() {
        return newParameters(scheduler);
    }

    public SchedulerConfig touchSchedulerConfig() {
        return touchParameters(scheduler);
    }

    public SchedulerConfig getSchedulerConfig() {
        return getParameters(scheduler);
    }

    public void putSchedulerConfig(SchedulerConfig schedulerConfig) {
        putValue(scheduler, schedulerConfig);
    }

    public ConsoleConfig newConsoleConfig() {
        return newParameters(console);
    }

    public ConsoleConfig touchConsoleConfig() {
        return touchParameters(console);
    }

    public ConsoleConfig getConsoleConfig() {
        return getParameters(console);
    }

    public void putConsoleConfig(ConsoleConfig consoleConfig) {
        putValue(console, consoleConfig);
    }

    public WebConfig newWebConfig() {
        return newParameters(web);
    }

    public WebConfig touchWebConfig() {
        return touchParameters(web);
    }

    public WebConfig getWebConfig() {
        return getParameters(web);
    }

    public void putWebConfig(WebConfig webConfig) {
        putValue(web, webConfig);
    }

    public void updateRootConfigLocation(String rootConfigLocation) {
        Parameters contextParameters = touchParameters(context);
        contextParameters.putValue(ContextConfig.root, rootConfigLocation);
    }

    public void updateSchedulerConfig(int startDelaySeconds, boolean waitOnShutdown, boolean startup) {
        Parameters schedulerParameters = touchParameters(scheduler);
        schedulerParameters.putValue(SchedulerConfig.startDelaySeconds, startDelaySeconds);
        schedulerParameters.putValue(SchedulerConfig.waitOnShutdown, waitOnShutdown);
        schedulerParameters.putValue(SchedulerConfig.startup, startup);
    }

}