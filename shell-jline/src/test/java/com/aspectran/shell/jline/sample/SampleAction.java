/*
 * Copyright (c) 2008-2017 The Aspectran Project
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
package com.aspectran.shell.sample;

import com.aspectran.core.util.logging.Log;
import com.aspectran.core.util.logging.LogFactory;

public class SampleAction {

    private final Log log = LogFactory.getLog(SampleAction.class);

    public String helloWorld() {
        String msg = "Hello, World.";

        log.info("The message generated by my first aciton is: " + msg);

        return msg;
    }

}
