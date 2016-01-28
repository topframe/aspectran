/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.aspectran.core.activity.process.action;

import com.aspectran.core.activity.process.ActionList;

/**
 * The Class AbstractAction.
 * 
 * <p>Created: 2008. 03. 22 PM 5:50:35</p>
 */
public abstract class AbstractAction {

	protected final ActionList parent;
	
	/**
	 * Instantiates a new AbstractAction.
	 *
	 * @param actionId the action id
	 * @param parent the parent
	 */
	public AbstractAction(String actionId, ActionList parent) {
		this.parent = parent;
	}
	
	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public ActionList getParent() {
		return parent;
	}
	
	/**
	 * Gets the action id.
	 *
	 * @return the action id
	 */
	public abstract String getActionId();

}
