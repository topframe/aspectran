/*
 * Copyright (c) 2008-2020 The Aspectran Project
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
package com.aspectran.shell.adapter;

import com.aspectran.core.adapter.DefaultRequestAdapter;
import com.aspectran.core.adapter.RequestAdapter;
import com.aspectran.core.context.rule.type.MethodType;

/**
 * Adapt Shell Request to Core {@link RequestAdapter}.
 */
public class ShellRequestAdapter extends DefaultRequestAdapter {

    /**
     * Instantiates a new ShellRequestAdapter.
     *
     * @param requestMethod the request method
     */
    public ShellRequestAdapter(MethodType requestMethod) {
        super(requestMethod, null);
    }

}
