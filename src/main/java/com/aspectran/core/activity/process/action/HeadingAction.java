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
package com.aspectran.core.activity.process.action;

import com.aspectran.core.activity.Activity;
import com.aspectran.core.activity.process.ActionList;
import com.aspectran.core.adapter.ResponseAdapter;
import com.aspectran.core.context.expr.ItemEvaluator;
import com.aspectran.core.context.expr.ItemExpressionParser;
import com.aspectran.core.context.rule.HeadingActionRule;
import com.aspectran.core.context.rule.type.ActionType;
import com.aspectran.core.util.MultiValueMap;
import com.aspectran.core.util.ToStringBuilder;
import com.aspectran.core.util.logging.Log;
import com.aspectran.core.util.logging.LogFactory;

/**
 * The Class HeadingAction.
 * 
 * <p>Created: 2016. 08. 23.</p>
 * 
 * @since 3.0.0
 */
public class HeadingAction extends AbstractAction {

	private static final Log log = LogFactory.getLog(HeadingAction.class);
	
	private final HeadingActionRule headingActionRule;
	
	/**
	 * Instantiates a new HeadingAction.
	 * 
	 * @param headingActionRule the heading action rule
	 * @param parent the parent
	 */
	public HeadingAction(HeadingActionRule headingActionRule, ActionList parent) {
		super(parent);
		this.headingActionRule = headingActionRule;
	}

	@Override
	public Object execute(Activity activity) throws Exception {
		if (headingActionRule.getHeaderItemRuleMap() == null) {
			return null;
		}
		
		try {
			ItemEvaluator evaluator = new ItemExpressionParser(activity);
			MultiValueMap<String, String> valueMap = evaluator.evaluateAsMultiValueMap(headingActionRule.getHeaderItemRuleMap());
			
			if (!valueMap.isEmpty()) {
				ResponseAdapter responseAdapter = activity.getResponseAdapter();
				responseAdapter.touchHeaders().putAll(valueMap);
			}
			
			return valueMap;
		} catch (Exception e) {
			log.error("Failed to execute heading action " + headingActionRule + " Cause: " + e.toString());
			throw e;
		}
	}
	
	/**
	 * Returns the heading action rule.
	 * 
	 * @return the headingActionRule
	 */
	public HeadingActionRule getHeadingActionRule() {
		return headingActionRule;
	}

	@Override
	public String getActionId() {
		return headingActionRule.getActionId();
	}

	@Override
	public boolean isHidden() {
		return headingActionRule.isHidden();
	}

	@Override
	public ActionType getActionType() {
		return ActionType.HEADERS;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getActionRule() {
		return (T)headingActionRule;
	}
	
	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder();
		tsb.append("actionType", getActionType());
		tsb.append("headingActionRule", headingActionRule);
		return tsb.toString();
	}

}
