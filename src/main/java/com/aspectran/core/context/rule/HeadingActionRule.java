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
package com.aspectran.core.context.rule;

import com.aspectran.core.util.BooleanUtils;
import com.aspectran.core.util.ToStringBuilder;

/**
 * The Class HeadingActionRule.
 * 
 * <p>Created: 2016. 08. 23.</p>
 * 
 * @since 3.0.0
 */
public class HeadingActionRule {
	
	private String actionId;
	
	private ItemRuleMap headerItemRuleMap;

	private Boolean hidden;
	
	/**
	 * Gets the action id.
	 * 
	 * @return the action id
	 */
	public String getActionId() {
		return actionId;
	}

	/**
	 * Sets the action id.
	 * 
	 * @param actionId the new action id
	 */
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	/**
	 * Gets the header item rule map.
	 *
	 * @return the header item rule map
	 */
	public ItemRuleMap getHeaderItemRuleMap() {
		return headerItemRuleMap;
	}

	/**
	 * Sets the header rule map.
	 *
	 * @param headerItemRuleMap the new header item rule map
	 */
	public void setHeaderItemRuleMap(ItemRuleMap headerItemRuleMap) {
		this.headerItemRuleMap = headerItemRuleMap;
	}

	/**
	 * Adds the header item rule.
	 *
	 * @param headerItemRule the header item rule
	 */
	public void addItemRule(ItemRule headerItemRule) {
		if(headerItemRuleMap == null) 
			headerItemRuleMap = new ItemRuleMap();
		
		headerItemRuleMap.putItemRule(headerItemRule);
	}
	
	/**
	 * Returns whether to hide result of the action.
	 * 
	 * @return true, if is hidden
	 */
	public Boolean getHidden() {
		return hidden;
	}

	/**
	 * Returns whether to hide result of the action.
	 *
	 * @return true, if is hidden
	 */
	public boolean isHidden() {
		return BooleanUtils.toBoolean(hidden, true);
	}

	/**
	 * Sets whether to hide result of the action.
	 * 
	 * @param hidden whether to hide result of the action
	 */
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	
	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder();
		tsb.append("id", actionId);
		if(headerItemRuleMap != null)
			tsb.append("headers", headerItemRuleMap.keySet());
		tsb.append("hidden", hidden);
		return tsb.toString();
	}
	
	/**
	 * Returns a new derived instance of EchoActionRule.
	 *
	 * @param id the action id
	 * @param hidden whether to hide result of the action
	 * @return the echo action rule
	 */
	public static HeadingActionRule newInstance(String id, Boolean hidden) {
		HeadingActionRule echoActionRule = new HeadingActionRule();
		echoActionRule.setActionId(id);
		echoActionRule.setHidden(hidden);
		return echoActionRule;
	}
	
}
