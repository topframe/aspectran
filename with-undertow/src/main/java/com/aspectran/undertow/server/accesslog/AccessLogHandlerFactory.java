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
package com.aspectran.undertow.server.accesslog;

import com.aspectran.core.component.bean.aware.ClassLoaderAware;
import com.aspectran.core.util.StringUtils;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;

/**
 * <p>Created: 2019-08-18</p>
 */
public class AccessLogHandlerFactory implements ClassLoaderAware {

    private ClassLoader classLoader;

    private HttpHandler handler;

    private String formatString;

    private String category;

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setHandler(HttpHandler handler) {
        this.handler = handler;
    }

    public void setFormatString(String formatString) {
        this.formatString = formatString;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public AccessLogHandler createAccessLogHandler() {
        if (handler == null) {
            throw new IllegalStateException("The next handler is not specified");
        }
        AccessLogReceiver accessLogReceiver = new AspectranAccessLogReceiver(category);
        String formatString = (StringUtils.hasText(this.formatString) ? this.formatString : "combined");
        return new AccessLogHandler(handler, accessLogReceiver, formatString, classLoader);
    }

}
