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
package com.aspectran.core.context.bean;

import com.aspectran.core.context.rule.BeanRule;

/**
 * The Class BeanDestroyFailedException.
 */
public class BeanDestroyFailedException extends BeanRuleException {

	/** @serial */
	private static final long serialVersionUID = -2416583532228763870L;
	
	/**
	 * Create a new BeanDestroyFailedException.
	 *
	 * @param beanRule the bean rule
	 */
	public BeanDestroyFailedException(BeanRule beanRule) {
		this("Cannot destroy a bean", beanRule);
	}

	/**
	 * Create a new BeanDestroyFailedException.
	 *
	 * @param msg the detail message
	 * @param beanRule the bean rule
	 */
	public BeanDestroyFailedException(String msg, BeanRule beanRule) {
		super(msg, beanRule);
	}

	/**
	 * Create a new BeanDestroyFailedException.
	 *
	 * @param beanRule the bean rule
	 * @param cause the root cause
	 */
	public BeanDestroyFailedException(BeanRule beanRule, Throwable cause) {
		this("Cannot destroy a bean", beanRule, cause);
	}

	/**
	 * Create a new BeanDestroyFailedException.
	 *
	 * @param msg the detail message
	 * @param beanRule the bean rule
	 * @param cause the root cause
	 */
	public BeanDestroyFailedException(String msg, BeanRule beanRule, Throwable cause) {
		super(msg, beanRule, cause);
	}

}
