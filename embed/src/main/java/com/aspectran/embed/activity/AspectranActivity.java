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
package com.aspectran.embed.activity;

import com.aspectran.core.activity.ActivityTerminatedException;
import com.aspectran.core.activity.AdapterException;
import com.aspectran.core.activity.CoreActivity;
import com.aspectran.core.activity.request.ParameterMap;
import com.aspectran.core.activity.request.RequestParseException;
import com.aspectran.core.adapter.DefaultSessionAdapter;
import com.aspectran.core.util.OutputStringWriter;
import com.aspectran.embed.adapter.AspectranRequestAdapter;
import com.aspectran.embed.adapter.AspectranResponseAdapter;
import com.aspectran.embed.service.AbstractEmbeddedAspectran;
import com.aspectran.embed.service.EmbeddedAspectran;

import java.io.Writer;
import java.util.Map;

/**
 * The Class AspectranActivity.
 */
public class AspectranActivity extends CoreActivity {

    private final EmbeddedAspectran aspectran;

    private Writer outputWriter;

    private ParameterMap parameterMap;

    private Map<String, Object> attributeMap;

    private String body;

    /**
     * Instantiates a new embedded aspectran activity.
     * @param aspectran the embedded aspectran
     */
    public AspectranActivity(AbstractEmbeddedAspectran aspectran) {
        this(aspectran, null);
    }

    /**
     * Instantiates a new embedded aspectran activity.
     * @param aspectran the embedded aspectran
     * @param outputWriter the output writer
     */
    public AspectranActivity(AbstractEmbeddedAspectran aspectran, Writer outputWriter) {
        super(aspectran.getActivityContext());

        this.aspectran = aspectran;
        this.outputWriter = outputWriter;
    }

    public void setParameterMap(ParameterMap parameterMap) {
        this.parameterMap = parameterMap;
    }

    public void setAttributeMap(Map<String, Object> attributeMap) {
        this.attributeMap = attributeMap;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    protected void adapt() throws AdapterException {
        setSessionAdapter(aspectran.newSessionAdapter());

        AspectranRequestAdapter requestAdapter = new AspectranRequestAdapter(getTranslet().getRequestMethod());
        if (body != null) {
            requestAdapter.setBody(body);
        }
        setRequestAdapter(requestAdapter);

        if (outputWriter == null) {
            outputWriter = new OutputStringWriter();
        }
        AspectranResponseAdapter responseAdapter = new AspectranResponseAdapter(outputWriter);
        setResponseAdapter(responseAdapter);

        if (!hasParentActivity() && getSessionAdapter() instanceof DefaultSessionAdapter) {
            ((DefaultSessionAdapter)getSessionAdapter()).getSessionAgent().access();
        }

        super.adapt();
    }

    @Override
    protected void parseRequest() throws ActivityTerminatedException, RequestParseException {
        if (parameterMap != null) {
            ((AspectranRequestAdapter)getRequestAdapter()).setParameterMap(parameterMap);
        }
        if (attributeMap != null) {
            ((AspectranRequestAdapter)getRequestAdapter()).setAttributeMap(attributeMap);
        }

        super.parseRequest();
    }

    @Override
    protected void release() {
        if (!hasParentActivity() && getSessionAdapter() instanceof DefaultSessionAdapter) {
            ((DefaultSessionAdapter)getSessionAdapter()).getSessionAgent().complete();
        }

        super.release();
    }

}
