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
package com.aspectran.core.context.rule.params;

import com.aspectran.core.util.apon.AbstractParameters;
import com.aspectran.core.util.apon.ParameterDefinition;
import com.aspectran.core.util.apon.ParameterValueType;

public class PointcutTargetParameters extends AbstractParameters {

    public static final ParameterDefinition translet;
    public static final ParameterDefinition bean;
    public static final ParameterDefinition method;

    private static final ParameterDefinition[] parameterDefinitions;

    static {
        translet = new ParameterDefinition("translet", ParameterValueType.STRING);
        bean = new ParameterDefinition("bean", ParameterValueType.STRING);
        method = new ParameterDefinition("method", ParameterValueType.STRING);

        parameterDefinitions = new ParameterDefinition[] {
            translet,
            bean,
            method
        };
    }

    public PointcutTargetParameters() {
        super(parameterDefinitions);
    }

    public PointcutTargetParameters(String text) {
        super(parameterDefinitions, text);
    }

}