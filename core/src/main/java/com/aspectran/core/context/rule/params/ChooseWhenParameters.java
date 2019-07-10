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
package com.aspectran.core.context.rule.params;

import com.aspectran.core.util.apon.AbstractParameters;
import com.aspectran.core.util.apon.ParameterKey;
import com.aspectran.core.util.apon.ValueType;

public class ChooseWhenParameters extends AbstractParameters {

    public static final ParameterKey caseNo;
    public static final ParameterKey test;
    public static final ParameterKey transform;
    public static final ParameterKey dispatch;
    public static final ParameterKey redirect;
    public static final ParameterKey forward;

    private static final ParameterKey[] parameterKeys;

    static {
        caseNo = new ParameterKey("caseNo", ValueType.INT);
        test = new ParameterKey("test", ValueType.STRING);
        transform = new ParameterKey("transform", TransformParameters.class);
        dispatch = new ParameterKey("dispatch", DispatchParameters.class);
        redirect = new ParameterKey("redirect", RedirectParameters.class);
        forward = new ParameterKey("forward", ForwardParameters.class);

        parameterKeys = new ParameterKey[] {
                caseNo,
                test,
                transform,
                dispatch,
                redirect,
                forward
        };
    }

    public ChooseWhenParameters() {
        super(parameterKeys);
    }

}
