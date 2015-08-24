/*
 *  Copyright (c) 2008 Jeong Ju Ho, All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aspectran.core.context.rule.type;

import java.util.HashMap;
import java.util.Map;

          
public final class JoinpointScopeType extends Type {

	public static final JoinpointScopeType TRANSLET;
	
	public static final JoinpointScopeType REQUEST;
	
	public static final JoinpointScopeType RESPONSE;

	public static final JoinpointScopeType CONTENT;
	
	public static final JoinpointScopeType BEAN;
	
	private static final Map<String, JoinpointScopeType> types;
	
	static {
		TRANSLET = new JoinpointScopeType("translet");
		REQUEST = new JoinpointScopeType("request");
		RESPONSE = new JoinpointScopeType("response");
		CONTENT = new JoinpointScopeType("content");
		BEAN = new JoinpointScopeType("bean");

		types = new HashMap<String, JoinpointScopeType>();
		types.put(TRANSLET.toString(), TRANSLET);
		types.put(REQUEST.toString(), REQUEST);
		types.put(RESPONSE.toString(), RESPONSE);
		types.put(CONTENT.toString(), CONTENT);
		types.put(BEAN.toString(), BEAN);
	}

	private JoinpointScopeType(String type) {
		super(type);
	}

	/**
	 * Returns a <code>JoinpointTargetType</code> with a value represented by the specified String.
	 * 
	 * @param type the type
	 * 
	 * @return the content type
	 */
	public static JoinpointScopeType valueOf(String type) {
		if(type == null)
			return null;
		
		return types.get(type);
	}
}
