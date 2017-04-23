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
import com.aspectran.core.util.apon.ParameterValueType;

public class AspectranContextAutoReloadConfig extends AbstractParameters {

    /**
     * The reload mode, which is either "hard" or "soft".
     */
    public static final ParameterDefinition reloadMode;

    /**
     * The interval in seconds between scanning the specified resouces for file changes.
     * If file changes are detected, the activity context is reloaded.
     */
    public static final ParameterDefinition scanIntervalSeconds;

    /**
     *  Defaults to {@code false}, which disables automatic reloading.
     */
    public static final ParameterDefinition startup;

    private final static ParameterDefinition[] parameterDefinitions;

    static {
        reloadMode = new ParameterDefinition("reloadMode", ParameterValueType.STRING);
        scanIntervalSeconds = new ParameterDefinition("scanIntervalSeconds", ParameterValueType.INT);
        startup = new ParameterDefinition("startup", ParameterValueType.BOOLEAN);

        parameterDefinitions = new ParameterDefinition[] {
            reloadMode,
            scanIntervalSeconds,
            startup
        };
    }

    public AspectranContextAutoReloadConfig() {
        super(parameterDefinitions);
    }

    public AspectranContextAutoReloadConfig(String text) {
        super(parameterDefinitions, text);
    }

}