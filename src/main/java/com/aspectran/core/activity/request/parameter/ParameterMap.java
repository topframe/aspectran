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
package com.aspectran.core.activity.request.parameter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Class ParameterMap.
 * 
 * <p>Created: 2008. 06. 11 PM 8:55:13</p>
 */
public class ParameterMap extends LinkedHashMap<String, String[]> {

	/** @serial */
	private static final long serialVersionUID = 1709146569240133920L;

	/**
	 * Instantiates a new Parameter map.
	 */
	public ParameterMap() {
		super();
	}

	/**
	 * Instantiates a new Parameter map.
	 *
	 * @param params the other parameter map
	 */
	public ParameterMap(Map<String, String[]> params) {
		super(params);
	}

	/**
	 * Returns the string value to which the specified name is mapped,
	 * or {@code null} if this map contains no mapping for the name.
	 * 
	 * @param name the parameter name
	 * @return a {@code String} representing the
	 *			single value of the parameter
	 */
	public String getParameter(String name) {
		String[] values = get(name);
		return (values != null && values.length > 0) ? values[0] : null;
	}
	
	/**
	 * Returns the string values to which the specified name is mapped,
	 * or {@code null} if this map contains no mapping for the name.
	 *
	 * @param name the parameter name
	 * @return an array of {@code String} objects
	 *			containing the parameter's values
	 */
	public String[] getParameterValues(String name) {
		return get(name);
	}

	/**
	 * Sets the value to the parameter with the given name.
	 *
	 * @param name a {@code String} specifying the name of the parameter
	 * @param value a {@code String} representing the
	 *			single value of the parameter
	 * @see #setParameter(String, String[])
	 */
	public void setParameter(String name, String value) {
		String[] values = new String[] { value };
		put(name, values);
	}

	/**
	 * Sets the value to the parameter with the given name.
	 *
	 * @param name a {@code String} specifying the name of the parameter
	 * @param values an array of {@code String} objects
	 *			containing the parameter's values
	 * @see #setParameter
	 */
	public void setParameter(String name, String[] values) {
		put(name, values);
	}

	/**
	 * Returns an {@code Enumeration} of {@code String} objects containing
	 * the names of the parameters.
	 * If no parameters, the method returns an empty {@code Enumeration}.
	 *
	 * @return an {@code Enumeration} of {@code String} objects, each {@code String}
	 * 			containing the name of a parameter;
	 * 			or an empty {@code Enumeration} if no parameters
	 */
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(keySet());
	}

}
