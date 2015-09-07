/*
 * Copyright 2008-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aspectran.core.service;

import com.aspectran.core.adapter.ApplicationAdapter;
import com.aspectran.core.context.ActivityContext;
import com.aspectran.core.context.loader.AspectranClassLoader;

public interface AspectranService extends AspectranServiceController {

	public AspectranClassLoader getAspectranClassLoader();
	
	public ApplicationAdapter getApplicationAdapter();
	
	public ActivityContext getActivityContext();

	public void setAspectranServiceControllerListener(AspectranServiceControllerListener aspectranServiceControllerListener);
	
	public boolean isHardReload();
	
	public boolean dispose();

}
