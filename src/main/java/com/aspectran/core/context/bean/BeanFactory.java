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

import com.aspectran.core.context.ActivityContext;

/**
 * The Interface BeanFactory.
 *
 * @author Juho Jeong
 * 
 * <p>Created: 2012. 11. 9. AM 11:36:47</p>
 */
public interface BeanFactory {
	
	public void initialize(ActivityContext context);
	
	public void destroy();

}
