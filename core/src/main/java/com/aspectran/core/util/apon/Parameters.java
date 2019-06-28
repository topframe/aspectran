/*
 * Copyright (c) 2008-2019 The Aspectran Project
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
package com.aspectran.core.util.apon;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Interface Parameters.
 */
public interface Parameters {

    void updateContainer(Parameters parameters);

    /**
     * Returns a map of the {@code ParameterValue}s.
     *
     * @return a map of the {@code ParameterValue}s
     */
    Map<String, ParameterValue> getParameterValueMap();

    /**
     * Specifies the identifier {@code Parameter}.
     *
     * @param identifier the identifier {@code Parameter}
     */
    void setIdentifier(Parameter identifier);

    /**
     * Returns the identifier {@code Parameter}.
     *
     * @return the identifier {@code Parameter}
     */
    Parameter getIdentifier();

    /**
     * Returns the qualified name.
     *
     * @return the qualified name
     */
    String getQualifiedName();

    /**
     * Returns the parent {@code Parameter} of the identifier {@code Parameter}.
     * <pre>
     *     parent = identifier --&gt; container --&gt; identifier
     * </pre>
     *
     * @return a {@code Parameter}
     */
    Parameter getParent();

    /**
     * Returns all the parameter names associated with this Parameters.
     *
     * @return an array of all parameter names associated with this Parameters
     */
    String[] getParameterNames();

    /**
     * Returns all the parameter names associated with this Parameters.
     *
     * @return the Set with all parameter names associated with this Parameters
     */
    Set<String> getParameterNameSet();

    /**
     * Returns whether this parameter exists.
     *
     * @param name the name of the parameter to check
     * @return {@code true} if the specified parameter exists; {@code false} otherwise
     */
    boolean hasParameter(String name);

    /**
     * Returns whether the specified parameter exists.
     *
     * @param parameterDefinition the parameter definition
     * @return {@code true} if the specified parameter exists; {@code false} otherwise
     */
    boolean hasParameter(ParameterDefinition parameterDefinition);

    /**
     * Returns whether a value is assigned to the specified parameter.
     * Even if a null is assigned, it is valid.
     *
     * @param name the name of the parameter to check
     * @return {@code true} if a parameter is assigned a value; {@code false} otherwise
     */
    boolean isAssigned(String name);

    /**
     * Returns whether a value is assigned to the specified parameter.
     * Even if a null is assigned, it is valid.
     *
     * @param parameterDefinition the parameter definition
     * @return {@code true} if a parameter is assigned a value; {@code false} otherwise
     */
    boolean isAssigned(ParameterDefinition parameterDefinition);

    /**
     * Returns whether a non-null value is assigned to the specified parameter.
     *
     * @param name the name of the parameter to check
     * @return {@code true} if a non-null value is assigned a value; {@code false} otherwise
     */
    boolean hasValue(String name);

    /**
     * Returns whether a non-null value is assigned to the specified parameter.
     *
     * @param parameterDefinition the parameter definition
     * @return {@code true} if a non-null value is assigned a value; {@code false} otherwise
     */
    boolean hasValue(ParameterDefinition parameterDefinition);

    /**
     * Returns the Parameter with the specified name.
     *
     * @param name the parameter name
     * @return the Parameter with the specified name, or {@code null} if it does not exist
     */
    Parameter getParameter(String name);

    /**
     * Returns the Parameter corresponding to the specified parameter definition.
     *
     * @param parameterDefinition the parameter definition
     * @return the Parameter corresponding to the specified parameter definition,
     *      or {@code null} if it does not exist
     */
    Parameter getParameter(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    Object getValue(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    Object getValue(ParameterDefinition parameterDefinition);

    void putAll(Parameters parameters);

    /**
     * Put a value into the Parameter with the specified name.
     *
     * @param name the parameter name
     * @param value the value of parameter
     */
    void putValue(String name, Object value);

    /**
     * Put a value into the Parameter with the specified parameter definition.
     *
     * @param parameterDefinition the parameter definition
     * @param value the value of parameter
     */
    void putValue(ParameterDefinition parameterDefinition, Object value);

    /**
     * Put a value of the parameter corresponding to the given name.
     * If the value is null, ignore it.
     *
     * @param name the parameter name
     * @param value the value of parameter
     */
    void putValueNonNull(String name, Object value);

    /**
     * Put a value of the parameter corresponding to the given parameter definition.
     * If the value is null, ignore it.
     *
     * @param parameterDefinition the parameter definition
     * @param value the value of parameter
     */
    void putValueNonNull(ParameterDefinition parameterDefinition, Object value);

    void clearValue(String name);

    void clearValue(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    String getString(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param name the parameter name
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    String getString(String name, String defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    String[] getStringArray(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    String getString(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    String getString(ParameterDefinition parameterDefinition, String defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    String[] getStringArray(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    List<String> getStringList(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    List<String> getStringList(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    Integer getInt(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param name the parameter name
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    int getInt(String name, int defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    Integer[] getIntArray(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    Integer getInt(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    int getInt(ParameterDefinition parameterDefinition, int defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    Integer[] getIntArray(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    List<Integer> getIntList(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    List<Integer> getIntList(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    Long getLong(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param name the parameter name
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    long getLong(String name, long defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    Long[] getLongArray(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    Long getLong(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    long getLong(ParameterDefinition parameterDefinition, long defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    Long[] getLongArray(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    List<Long> getLongList(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    List<Long> getLongList(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    Float getFloat(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param name the parameter name
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    float getFloat(String name, float defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    Float[] getFloatArray(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    Float getFloat(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    float getFloat(ParameterDefinition parameterDefinition, float defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    Float[] getFloatArray(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    List<Float> getFloatList(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    List<Float> getFloatList(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    Double getDouble(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param name the parameter name
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    double getDouble(String name, double defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    Double[] getDoubleArray(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    Double getDouble(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    double getDouble(ParameterDefinition parameterDefinition, double defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    Double[] getDoubleArray(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    List<Double> getDoubleList(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    List<Double> getDoubleList(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    Boolean getBoolean(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param name the parameter name
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    boolean getBoolean(String name, boolean defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    Boolean[] getBooleanArray(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    Boolean getBoolean(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code defaultValue} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @param defaultValue the default value to return if no value is found
     * @return the value for the specified parameter, or {@code defaultValue}
     */
    boolean getBoolean(ParameterDefinition parameterDefinition, boolean defaultValue);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    Boolean[] getBooleanArray(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    List<Boolean> getBooleanList(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    List<Boolean> getBooleanList(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param <T> the type parameter
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    <T extends Parameters> T getParameters(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param <T> the type parameter
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    <T extends Parameters> T[] getParametersArray(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param <T> the type parameter
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    <T extends Parameters> T getParameters(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param <T> the type parameter
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    <T extends Parameters> T[] getParametersArray(ParameterDefinition parameterDefinition);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param <T> the type parameter
     * @param name the parameter name
     * @return the value for the specified parameter, or {@code null}
     */
    <T extends Parameters> List<T> getParametersList(String name);

    /**
     * Return the value for the specified parameter,
     * or {@code null} if the parameter does not exist.
     *
     * @param <T> the type parameter
     * @param parameterDefinition the parameter definition
     * @return the value for the specified parameter, or {@code null}
     */
    <T extends Parameters> List<T> getParametersList(ParameterDefinition parameterDefinition);

    ParameterValue newParameterValue(String name, ParameterValueType parameterValueType);

    ParameterValue newParameterValue(String name, ParameterValueType parameterValueType, boolean array);

    <T extends Parameters> T newParameters(String name);

    <T extends Parameters> T newParameters(ParameterDefinition parameterDefinition);

    <T extends Parameters> T touchParameters(String name);

    <T extends Parameters> T touchParameters(ParameterDefinition parameterDefinition);

    /**
     * Returns whether the parameter can be added after the parameters instance is created.
     *
     * @return {@code true} if the parameter can be added after the parameters instance is created,
     *      otherwise {@code false}
     */
    boolean isAddable();

    String describe();

    String describe(boolean details);

    void setIndentString(String indentString);

    void readFrom(String text) throws IOException;

}
