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
package com.aspectran.core.context.rule;

import com.aspectran.core.activity.process.action.AnnotatedMethodAction;
import com.aspectran.core.activity.process.action.BeanMethodAction;
import com.aspectran.core.activity.process.action.EchoAction;
import com.aspectran.core.activity.process.action.Executable;
import com.aspectran.core.activity.process.action.HeaderAction;
import com.aspectran.core.activity.response.RedirectResponse;
import com.aspectran.core.activity.response.Response;
import com.aspectran.core.activity.response.ResponseMap;
import com.aspectran.core.activity.response.dispatch.DispatchResponse;
import com.aspectran.core.activity.response.transform.TransformResponseFactory;
import com.aspectran.core.context.rule.ability.ActionRuleApplicable;
import com.aspectran.core.context.rule.ability.ResponseRuleApplicable;
import com.aspectran.core.context.rule.type.ActionType;

import java.util.Collection;

/**
 * The Class ExceptionThrownRule.
 * 
 * <p>Created: 2008. 04. 01 PM 11:19:28</p>
 */
public class ExceptionThrownRule implements ActionRuleApplicable, ResponseRuleApplicable {

    private final AspectAdviceRule aspectAdviceRule;

    private String[] exceptionTypes;

    private Executable action;

    private ResponseMap responseMap;

    private Response defaultResponse;

    public ExceptionThrownRule() {
        this(null);
    }

    public ExceptionThrownRule(AspectAdviceRule aspectAdviceRule) {
        this.aspectAdviceRule = aspectAdviceRule;
    }

    public AspectAdviceRule getAspectAdviceRule() {
        return aspectAdviceRule;
    }

    public String[] getExceptionTypes() {
        return exceptionTypes;
    }

    public void setExceptionTypes(String... exceptionTypes) {
        this.exceptionTypes = exceptionTypes;
    }

    /**
     * Returns the advice action.
     *
     * @return the advice action
     */
    public Executable getAction() {
        return action;
    }

    public void setAction(AnnotatedMethodAction action) {
        this.action = action;
    }

    /**
     * Returns the action type of the executable action.
     *
     * @return the action type
     */
    public ActionType getActionType() {
        return (action != null ? action.getActionType() : null);
    }

    public Response getResponse(String contentType) {
        if (responseMap != null && contentType != null) {
            Response response = responseMap.get(contentType);
            if (response != null) {
                return response;
            }
        }
        return getDefaultResponse();
    }

    private Response getDefaultResponse() {
        if (defaultResponse == null && responseMap != null && responseMap.size() == 1) {
            return responseMap.getFirst();
        } else {
            return defaultResponse;
        }
    }

    /**
     * Gets the response map.
     *
     * @return the response map
     */
    public ResponseMap getResponseMap() {
        return responseMap;
    }

    private ResponseMap touchResponseMap() {
        if (responseMap == null) {
            responseMap = new ResponseMap();
        }
        return responseMap;
    }

    @Override
    public Executable applyActionRule(BeanMethodActionRule beanMethodActionRule) {
        BeanMethodAction action = new BeanMethodAction(beanMethodActionRule);
        if (aspectAdviceRule != null && beanMethodActionRule.getBeanId() == null) {
            action.setAspectAdviceRule(aspectAdviceRule);
        }
        this.action = action;
        return action;
    }

    @Override
    public Executable applyActionRule(AnnotatedMethodActionRule annotatedMethodActionRule) {
        throw new UnsupportedOperationException(
                "Cannot apply the annotated method action rule to the exception thrown rule");
    }

    @Override
    public Executable applyActionRule(IncludeActionRule includeActionRule) {
        throw new UnsupportedOperationException(
                "Cannot apply the include action rule to the exception thrown rule");
    }

    @Override
    public Executable applyActionRule(EchoActionRule echoActionRule) {
        Executable action = new EchoAction(echoActionRule);
        this.action = action;
        return action;
    }

    @Override
    public Executable applyActionRule(HeaderActionRule headerActionRule) {
        Executable action = new HeaderAction(headerActionRule);
        this.action = action;
        return action;
    }

    @Override
    public void applyActionRule(Executable action) {
        this.action = action;
    }

    @Override
    public void applyActionRule(Collection<Executable> actionList) {
        throw new UnsupportedOperationException("Only one action allowed");
    }

    @Override
    public Response applyResponseRule(TransformRule transformRule) {
        Response response = TransformResponseFactory.create(transformRule);
        touchResponseMap().put(transformRule.getContentType(), response);
        if (transformRule.isDefaultResponse()) {
            defaultResponse = response;
        }
        if (defaultResponse == null && transformRule.getContentType() == null) {
            defaultResponse = response;
        }
        return response;
    }

    @Override
    public Response applyResponseRule(DispatchRule dispatchRule) {
        Response response = new DispatchResponse(dispatchRule);
        touchResponseMap().put(dispatchRule.getContentType(), response);
        if (dispatchRule.isDefaultResponse()) {
            defaultResponse = response;
        }
        if (defaultResponse == null && dispatchRule.getContentType() == null) {
            defaultResponse = response;
        }
        return response;
    }

    @Override
    public Response applyResponseRule(ForwardRule forwardRule) {
        throw new UnsupportedOperationException(
                "Cannot apply the forward response rule to the exception thrown rule");
    }

    @Override
    public Response applyResponseRule(RedirectRule redirectRule) {
        Response response = new RedirectResponse(redirectRule);
        touchResponseMap().put(redirectRule.getContentType(), response);
        if (redirectRule.isDefaultResponse()) {
            defaultResponse = response;
        }
        if (defaultResponse == null && redirectRule.getContentType() == null) {
            defaultResponse = response;
        }
        return response;
    }

    public static ExceptionThrownRule newInstance(Class<? extends Throwable>[] types, AnnotatedMethodAction action) {
        ExceptionThrownRule exceptionThrownRule = new ExceptionThrownRule();
        if (types != null && types.length > 0) {
            String[] exceptionTypes = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                exceptionTypes[i] = types[0].getName();
            }
            exceptionThrownRule.setExceptionTypes(exceptionTypes);
        }
        exceptionThrownRule.setAction(action);
        return exceptionThrownRule;
    }

}
