/**
 * Copyright 2008-2016 Juho Jeong
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
package com.aspectran.core.activity;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import com.aspectran.core.activity.aspect.result.AspectAdviceResult;
import com.aspectran.core.activity.process.result.ProcessResult;
import com.aspectran.core.activity.request.parameter.FileParameter;
import com.aspectran.core.activity.response.ForwardResponse;
import com.aspectran.core.activity.response.RedirectResponse;
import com.aspectran.core.activity.response.Response;
import com.aspectran.core.activity.response.TransformResponseFactory;
import com.aspectran.core.adapter.ApplicationAdapter;
import com.aspectran.core.adapter.RequestAdapter;
import com.aspectran.core.adapter.ResponseAdapter;
import com.aspectran.core.adapter.SessionAdapter;
import com.aspectran.core.context.message.NoSuchMessageException;
import com.aspectran.core.context.rule.AspectAdviceRule;
import com.aspectran.core.context.rule.ForwardResponseRule;
import com.aspectran.core.context.rule.RedirectResponseRule;
import com.aspectran.core.context.rule.TransformRule;
import com.aspectran.core.context.rule.type.MethodType;
import com.aspectran.core.context.rule.type.ResponseType;

/**
 * The Class CoreTranslet.
 */
public class CoreTranslet implements Translet {

	private final Activity activity;

	private ProcessResult processResult;
	
	private AspectAdviceResult aspectAdviceResult;

	private ActivityDataMap activityDataMap;
	
	/**
	 * Instantiates a new GenericTranslet.
	 *
	 * @param activity the current Activity
	 */
	protected CoreTranslet(Activity activity) {
		this.activity = activity;
	}

	@Override
	public String getTransletName() {
		return activity.getTransletName();
	}

	@Override
	public MethodType getRequestMethod() {
		return activity.getRequestMethod();
	}

	@Override
	public ApplicationAdapter getApplicationAdapter() {
		return activity.getActivityContext().getApplicationAdapter();
	}

	@Override
	public SessionAdapter getSessionAdapter() {
		return activity.getSessionAdapter();
	}

	@Override
	public RequestAdapter getRequestAdapter() {
		return activity.getRequestAdapter();
	}

	@Override
	public ResponseAdapter getResponseAdapter() {
		return activity.getResponseAdapter();
	}

	@Override
	public <T> T getRequestAdaptee() {
		return getRequestAdapter().getAdaptee();
	}

	@Override
	public <T> T getResponseAdaptee() {
		return getResponseAdapter().getAdaptee();
	}

	@Override
	public <T> T getSessionAdaptee() {
		SessionAdapter sessionAdapter = getSessionAdapter();
		return (sessionAdapter != null) ? sessionAdapter.getAdaptee() : null;
	}

	@Override
	public <T> T getApplicationAdaptee() {
		return activity.getActivityContext().getApplicationAdapter().getAdaptee();
	}

	@Override
	public ProcessResult getProcessResult() {
		return processResult;
	}

	@Override
	public Object getProcessResult(String actionId) {
		return (processResult != null) ? processResult.getResultValue(actionId) : null;
	}

	@Override
	public void setProcessResult(ProcessResult processResult) {
		this.processResult = processResult;
	}

	@Override
	public ProcessResult touchProcessResult() {
		return touchProcessResult(null);
	}

	@Override
	public ProcessResult touchProcessResult(String contentsName) {
		return touchProcessResult(contentsName, 3);
	}

	@Override
	public ProcessResult touchProcessResult(String contentsName, int initialCapacity) {
		if(processResult == null) {
			processResult = new ProcessResult(initialCapacity);
			if(contentsName != null)
				processResult.setName(contentsName);
		}
		return processResult;
	}

	@Override
	public ActivityDataMap getActivityDataMap() {
		return getActivityDataMap(false);
	}

	@Override
	public ActivityDataMap getActivityDataMap(boolean prefill) {
		if(activityDataMap == null) {
			activityDataMap = new ActivityDataMap(activity, prefill);
		}
		return activityDataMap;
	}

	@Override
	public String getParameter(String name) {
		return getRequestAdapter().getParameter(name);
	}

	@Override
	public String[] getParameterValues(String name) {
		return getRequestAdapter().getParameterValues(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return getRequestAdapter().getParameterNames();
	}

	@Override
	public void setParameter(String name, String value) {
		getRequestAdapter().setParameter(name, value);
	}

	@Override
	public void setParameter(String name, String[] values) {
		getRequestAdapter().setParameter(name, values);
	}

	@Override
	public FileParameter getFileParameter(String name) {
		return getRequestAdapter().getFileParameter(name);
	}

	@Override
	public FileParameter[] getFileParameterValues(String name) {
		return getRequestAdapter().getFileParameterValues(name);
	}

	@Override
	public Enumeration<String> getFileParameterNames() {
		return getRequestAdapter().getFileParameterNames();
	}

	@Override
	public void setFileParameter(String name, FileParameter fileParameter) {
		getRequestAdapter().setFileParameter(name, fileParameter);
	}

	@Override
	public void setFileParameter(String name, FileParameter[] fileParameters) {
		getRequestAdapter().setFileParameter(name, fileParameters);
	}

	@Override
	public FileParameter[] removeFileParameter(String name) {
		return getRequestAdapter().removeFileParameter(name);
	}

	@Override
	public <T> T getAttribute(String name) {
		return getRequestAdapter().getAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		getRequestAdapter().setAttribute(name, value);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return getRequestAdapter().getAttributeNames();
	}

	@Override
	public void removeAttribute(String name) {
		getRequestAdapter().removeAttribute(name);
	}

	@Override
	public Map<String, Object> getParameterMap() {
		return getRequestAdapter().getParameterMap();
	}

	@Override
	public void fillPrameterMap(Map<String, Object> parameterMap) {
		getRequestAdapter().fillPrameterMap(parameterMap);
	}

	@Override
	public Map<String, Object> getAttributeMap() {
		return getRequestAdapter().getAttributeMap();
	}

	@Override
	public void fillAttributeMap(Map<String, Object> attributeMap) {
		getRequestAdapter().fillAttributeMap(attributeMap);
	}

	@Override
	public void response() {
		activity.activityEnd();
	}

	@Override
	public void response(Response response) {
		activity.response(response);
	}

	@Override
	public void transform(TransformRule transformRule) {
		Response res = TransformResponseFactory.getResponse(transformRule);
		response(res);
	}

	@Override
	public void redirect(RedirectResponseRule redirectResponseRule) {
		Response res = new RedirectResponse(redirectResponseRule);
		response(res);
	}

	@Override
	public void redirect(String target) {
		redirect(target, false);
	}

	@Override
	public void redirect(String target, boolean immediately) {
		if(!immediately && activity.getResponse() != null) {
			Response res = activity.getResponse();
			if(res.getResponseType() == ResponseType.REDIRECT) {
				Response rr = res.replicate();
				RedirectResponseRule rrr = ((RedirectResponse)rr).getRedirectResponseRule();
				rrr.setTarget(target);
				redirect(rrr);
				return;
			}
		}
		RedirectResponseRule rrr = RedirectResponseRule.newInstance(target);
		redirect(rrr);
	}

	@Override
	public void redirect(String target, Map<String, String> parameters) {
		RedirectResponseRule rrr = new RedirectResponseRule();
		rrr.setTarget(target, null);
		rrr.setParameterMap(parameters);
		redirect(rrr);
	}

	@Override
	public void forward(ForwardResponseRule forwardResponseRule) {
		Response res = new ForwardResponse(forwardResponseRule);
		response(res);
	}

	@Override
	public void forward(String transletName) {
		forward(transletName, false);
	}

	@Override
	public void forward(String transletName, boolean immediately) {
		if(!immediately && activity.getResponse() != null) {
			Response res = activity.getResponse();
			if(res.getResponseType() == ResponseType.FORWARD) {
				Response fr = res.replicate();
				ForwardResponseRule frr = ((ForwardResponse)fr).getForwardResponseRule();
				frr.setTransletName(transletName);
				forward(frr);
				return;
			}
		}
		ForwardResponseRule frr = ForwardResponseRule.newInstance(transletName);
		forward(frr);
	}

	@Override
	public boolean isExceptionRaised() {
		return activity.isExceptionRaised();
	}

	@Override
	public Throwable getRaisedException() {
		return activity.getRaisedException();
	}

	@Override
	public Throwable getOriginRaisedException() {
		return activity.getOriginRaisedException();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAspectAdviceBean(String aspectId) {
		return (aspectAdviceResult != null) ? (T)aspectAdviceResult.getAspectAdviceBean(aspectId) : null;
	}

	@Override
	public void putAspectAdviceBean(String aspectId, Object adviceBean) {
		if(aspectAdviceResult == null) {
			aspectAdviceResult = new AspectAdviceResult();
		}
		aspectAdviceResult.putAspectAdviceBean(aspectId, adviceBean);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getBeforeAdviceResult(String aspectId) {
		return (aspectAdviceResult != null) ? (T)aspectAdviceResult.getBeforeAdviceResult(aspectId) : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAfterAdviceResult(String aspectId) {
		return (aspectAdviceResult != null) ? (T)aspectAdviceResult.getAfterAdviceResult(aspectId) : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getFinallyAdviceResult(String aspectId) {
		return (aspectAdviceResult != null) ? (T)aspectAdviceResult.getFinallyAdviceResult(aspectId) : null;
	}

	@Override
	public void putAdviceResult(AspectAdviceRule aspectAdviceRule, Object adviceActionResult) {
		if(aspectAdviceResult == null) {
			aspectAdviceResult = new AspectAdviceResult();
		}
		aspectAdviceResult.putAdviceResult(aspectAdviceRule, adviceActionResult);
	}

	@Override
	public Class<? extends Translet> getTransletInterfaceClass() {
		return activity.getTransletInterfaceClass();
	}

	@Override
	public Class<? extends CoreTranslet> getTransletImplementationClass() {
		return activity.getTransletImplementationClass();
	}

	@Override
	public boolean acceptsProfiles(String... profiles) {
		return activity.getActivityContext().getContextEnvironment().acceptsProfiles(profiles);
	}

	//---------------------------------------------------------------------
	// Implementation of BeanRegistry interface
	//---------------------------------------------------------------------

	@Override
	public <T> T getBean(String id) {
		return activity.getBean(id);
	}

	@Override
	public <T> T getBean(Class<T> requiredType) {
		return activity.getBean(requiredType);
	}

	@Override
	public <T> T getBean(String id, Class<T> requiredType) {
		return activity.getBean(id, requiredType);
	}

	@Override
	public <T> T getBean(Class<T> requiredType, String id) {
		return activity.getBean(requiredType, id);
	}

	@Override
	public <T> T getConfigBean(Class<T> classType) {
		return activity.getConfigBean(classType);
	}

	@Override
	public boolean containsBean(String id) {
		return activity.containsBean(id);
	}

	@Override
	public boolean containsBean(Class<?> requiredType) {
		return activity.containsBean(requiredType);
	}

	//---------------------------------------------------------------------
	// Implementation of MessageSource interface
	//---------------------------------------------------------------------

	@Override
	public String getMessage(String code, Object args[], String defaultMessage, Locale locale) {
		return activity.getActivityContext().getMessageSource().getMessage(code, args, defaultMessage, locale);
	}

	@Override
	public String getMessage(String code, Object args[], Locale locale) throws NoSuchMessageException {
		return activity.getActivityContext().getMessageSource().getMessage(code, args, locale);
	}

	@Override
	public String getMessage(String code, Object args[], String defaultMessage) {
		return getMessage(code, args, defaultMessage, activity.getRequestAdapter().getLocale());
	}

	@Override
	public String getMessage(String code, Object args[]) throws NoSuchMessageException {
		return getMessage(code, args, activity.getRequestAdapter().getLocale());
	}

}