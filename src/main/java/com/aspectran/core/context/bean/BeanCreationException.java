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
package com.aspectran.core.context.bean;

import com.aspectran.core.context.rule.BeanRule;

/**
 * The Class BeanCreationException.
 */
public class BeanCreationException extends BeanRuleException {

	/** @serial */
	private static final long serialVersionUID = 8126208342749350818L;

	/**
	 * Instantiates a new BeanCreationException.
	 *
	 * @param beanRule the bean rule
	 */
	public BeanCreationException(BeanRule beanRule) {
		this(beanRule, "Cannot create a bean");
	}

	/**
	 * Instantiates a new BeanCreationException.
	 *
	 * @param beanRule the bean rule
	 * @param msg The detail message
	 */
	public BeanCreationException(BeanRule beanRule, String msg) {
		super(beanRule, msg);
	}

	/**
	 * Instantiates a new BeanCreationException.
	 *
	 * @param beanRule the bean rule
	 * @param cause the root cause
	 */
	public BeanCreationException(BeanRule beanRule, Throwable cause) {
		this(beanRule, "Cannot create a bean", cause);
	}

	/**
	 * Instantiates a new BeanCreationException.
	 *
	 * @param beanRule the bean rule
	 * @param msg The detail message
	 * @param cause the root cause
	 */
	public BeanCreationException(BeanRule beanRule, String msg, Throwable cause) {
		super(beanRule, msg, cause);
	}

}
