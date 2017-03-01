/**
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
package com.aspectran.web.support.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aspectran.core.activity.Activity;
import com.aspectran.core.activity.process.result.ActionResult;
import com.aspectran.core.activity.process.result.ContentResult;
import com.aspectran.core.activity.process.result.ProcessResult;
import com.aspectran.core.activity.response.dispatch.ViewDispatchException;
import com.aspectran.core.activity.response.dispatch.ViewDispatcher;
import com.aspectran.core.adapter.RequestAdapter;
import com.aspectran.core.adapter.ResponseAdapter;
import com.aspectran.core.context.rule.DispatchResponseRule;
import com.aspectran.core.util.logging.Log;
import com.aspectran.core.util.logging.LogFactory;

/**
 * JSP or other web resource integration.
 * 
 * <p>Created: 2008. 03. 22 PM 5:51:58</p>
 */
public class JspViewDispatcher implements ViewDispatcher {

	private static final Log log = LogFactory.getLog(JspViewDispatcher.class);

	private static final boolean debugEnabled = log.isDebugEnabled();
	
	private static final String DEFAULT_CONTENT_TYPE = "text/html;charset=ISO-8859-1";
	
	private String prefix;

	private String suffix;
	
	/**
	 * Sets the prefix for the template name.
	 *
	 * @param prefix the new prefix for the template name
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Sets the suffix for the template name.
	 *
	 * @param suffix the new suffix for the template name
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public void dispatch(Activity activity, DispatchResponseRule dispatchResponseRule) throws ViewDispatchException {
		String dispatchName = null;

		try {
			dispatchName = dispatchResponseRule.getName(activity);
			if (dispatchName == null) {
				throw new IllegalArgumentException("No specified dispatch name.");
			}

			if (prefix != null && suffix != null) {
				dispatchName = prefix + dispatchName + suffix;
			} else if (prefix != null) {
				dispatchName = prefix + dispatchName;
			} else if (suffix != null) {
				dispatchName = dispatchName + suffix;
			}
			
			RequestAdapter requestAdapter = activity.getRequestAdapter();
			ResponseAdapter responseAdapter = activity.getResponseAdapter();

			String contentType = dispatchResponseRule.getContentType();
			String characterEncoding = dispatchResponseRule.getCharacterEncoding();

			if (contentType != null) {
				responseAdapter.setContentType(contentType);
			} else {
				responseAdapter.setContentType(DEFAULT_CONTENT_TYPE);
			}

			if (characterEncoding != null) {
				responseAdapter.setCharacterEncoding(characterEncoding);
			} else {
				characterEncoding = activity.getTranslet().getResponseCharacterEncoding();
				if (characterEncoding != null) {
					responseAdapter.setCharacterEncoding(characterEncoding);
				}
			}
			
			ProcessResult processResult = activity.getProcessResult();

			if (processResult != null) {
				setAttribute(requestAdapter, processResult);
			}

			HttpServletRequest request = requestAdapter.getAdaptee();
			HttpServletResponse response = responseAdapter.getAdaptee();
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(dispatchName);
			requestDispatcher.forward(request, response);

			if (debugEnabled) {
				log.debug("Dispatch to a JSP [" + dispatchName + "]");
			}
		} catch (Exception e) {
			throw new ViewDispatchException("Failed to dispatch to JSP " + dispatchResponseRule.toString(this, dispatchName), e);
		}
	}

	/**
	 * Stores an attribute in request.
	 *
	 * @param requestAdapter the request adapter
	 * @param processResult the process result
	 */
	private void setAttribute(RequestAdapter requestAdapter, ProcessResult processResult) {
		for (ContentResult contentResult : processResult) {
			for (ActionResult actionResult : contentResult) {
				Object actionResultValue = actionResult.getResultValue();

				if (actionResultValue instanceof ProcessResult) {
					setAttribute(requestAdapter, (ProcessResult)actionResultValue);
				} else {
					String actionId = actionResult.getActionId();
					if (actionId != null) {
						requestAdapter.setAttribute(actionId, actionResultValue);
					}
				}
			}
		}
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}