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
package com.aspectran.core.context.bean;

/**
 * Contains an object of the instantiated bean.
 *
 * <p>Created: 2016. 12. 27.</p>
 *
 * @since 3.2.0
 */
public class InstantiatedBean {

	private Object bean;

	public InstantiatedBean(Object bean) {
		this.bean = bean;
	}

	public Object getBean() {
		return bean;
	}

}