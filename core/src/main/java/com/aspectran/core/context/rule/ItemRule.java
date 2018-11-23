/*
 * Copyright (c) 2008-2018 The Aspectran Project
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

import com.aspectran.core.activity.request.parameter.FileParameter;
import com.aspectran.core.component.bean.annotation.Attribute;
import com.aspectran.core.component.bean.annotation.Parameter;
import com.aspectran.core.context.expr.token.Token;
import com.aspectran.core.context.expr.token.TokenParser;
import com.aspectran.core.context.rule.params.CallParameters;
import com.aspectran.core.context.rule.params.ItemHolderParameters;
import com.aspectran.core.context.rule.params.ItemParameters;
import com.aspectran.core.context.rule.type.ItemType;
import com.aspectran.core.context.rule.type.ItemValueType;
import com.aspectran.core.context.rule.type.TokenType;
import com.aspectran.core.util.StringUtils;
import com.aspectran.core.util.ToStringBuilder;
import com.aspectran.core.util.apon.Parameters;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * The Class ItemRule.
 * 
 * <p>Created: 2008. 03. 27 PM 3:57:48</p>
 */
public class ItemRule {

    /**  suffix for array-type item: "[]". */
    private static final String ARRAY_SUFFIX = "[]";

    /**  suffix for map-type item: "{}". */
    private static final String MAP_SUFFIX = "{}";

    private ItemType type;

    private String name;

    private ItemValueType valueType;

    private String defaultValue;

    private Boolean tokenize;

    private Token[] tokens;

    private List<Token[]> tokensList;

    private Map<String, Token[]> tokensMap;

    private Boolean mandatory;

    private Boolean security;

    private boolean autoNamed;

    /**
     * Instantiates a new ItemRule.
     */
    public ItemRule() {
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Sets the item type.
     *
     * @param type the new item type
     */
    public void setType(ItemType type) {
        this.type = type;
    }

    /**
     * Returns the name of the item.
     *
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of a item.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        if (name.endsWith(ARRAY_SUFFIX)) {
            this.name = name.substring(0, name.length() - 2);
            type = ItemType.ARRAY;
        } else if (name.endsWith(MAP_SUFFIX)) {
            this.name = name.substring(0, name.length() - 2);
            type = ItemType.MAP;
        } else {
            this.name = name;
            if (type == null) {
                type = ItemType.SINGLE;
            }
        }
    }

    /**
     * Returns the value of the item.
     *
     * @return the value of the item
     */
    public String getValue() {
        return TokenParser.toString(tokens);
    }

    /**
     * Gets the tokens.
     *
     * @return the tokens
     */
    public Token[] getTokens() {
        return tokens;
    }

    /**
     * Gets the list of tokens.
     *
     * @return the tokens list
     */
    public List<Token[]> getTokensList() {
        return tokensList;
    }

    /**
     * Returns a list of string values of this item.
     *
     * @return a list of string values
     */
    public List<String> getValueList() {
        if (tokensList == null) {
            return null;
        }
        if (tokensList.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<String> list = new ArrayList<>(tokensList.size());
            for (Token[] tokens : tokensList) {
                list.add(TokenParser.toString(tokens));
            }
            return list;
        }
    }

    /**
     * Gets the tokens map.
     *
     * @return the tokens map
     */
    public Map<String, Token[]> getTokensMap() {
        return tokensMap;
    }

    /**
     * Returns a map of string values of this item.
     *
     * @return a map of string values
     */
    public Map<String, String> getValueMap() {
        if (tokensMap == null) {
            return null;
        }
        if (tokensMap.isEmpty()) {
            return new LinkedHashMap<>();
        } else {
            Map<String, String> map = new LinkedHashMap<>(tokensMap.size());
            for (Map.Entry<String, Token[]> entry : tokensMap.entrySet()) {
                map.put(entry.getKey(), TokenParser.toString(entry.getValue()));
            }
            return map;
        }
    }

    /**
     * Sets the specified value to this Single type item.
     *
     * @param text the value to be analyzed for use as the value of this item
     * @see #setValue(Token[])
     */
    public void setValue(String text) {
        Token[] tokens = TokenParser.makeTokens(text, isTokenize());
        setValue(tokens);
    }

    /**
     * Sets the specified value to this Single type item.
     *
     * @param tokens an array of tokens
     */
    public void setValue(Token[] tokens) {
        if (type == null) {
            type = ItemType.SINGLE;
        }
        if (type != ItemType.SINGLE) {
            throw new IllegalArgumentException("The type of this item must be 'single'");
        }
        this.tokens = tokens;
    }

    /**
     * Puts the specified value with the specified key to this Map type item.
     *
     * @param name the value name; may be null
     * @param text the value to be analyzed for use as the value of this item
     * @see #putValue(String, Token[])
     */
    public void putValue(String name, String text) {
        Token[] tokens = TokenParser.makeTokens(text, isTokenize());
        putValue(name, tokens);
    }

    /**
     * Puts the specified value with the specified key to this Map type item.
     *
     * @param name the value name; may be null
     * @param tokens an array of tokens
     */
    public void putValue(String name, Token[] tokens) {
        if (type == null) {
            type = ItemType.MAP;
        }
        if (!isMappableType()) {
            throw new IllegalArgumentException("The type of this item must be 'map' or 'properties'");
        }
        if (tokensMap == null) {
            tokensMap = new LinkedHashMap<>();
        }
        tokensMap.put(name, tokens);
    }

    /**
     * Sets a value to this Map type item.
     *
     * @param tokensMap the tokens map
     */
    public void setValue(Map<String, Token[]> tokensMap) {
        if (type == null) {
            type = ItemType.MAP;
        }
        if (!isMappableType()) {
            throw new IllegalArgumentException("The type of this item must be 'map' or 'properties'");
        }
        this.tokensMap = tokensMap;
    }

    /**
     * Sets a value to this Properties type item.
     *
     * @param properties the properties
     */
    public void setValue(Properties properties)  {
        if (properties == null) {
            throw new IllegalArgumentException("Argument 'properties' must not be null");
        }
        if (type == null) {
            type = ItemType.PROPERTIES;
        }
        if (!isMappableType()) {
            throw new IllegalArgumentException("The type of this item must be 'properties' or 'map'");
        }
        tokensMap = new LinkedHashMap<>();
        for (String key : properties.stringPropertyNames()) {
            Object o = properties.get(key);
            if (o instanceof Token[]) {
                tokensMap.put(key, (Token[])o);
            } else if (o instanceof Token) {
                Token[] tokens = new Token[] { (Token)o };
                tokensMap.put(key, tokens);
            } else {
                Token[] tokens = TokenParser.makeTokens(o.toString(), isTokenize());
                putValue(name, tokens);
            }
        }
    }

    /**
     * Adds the specified value to this List type item.
     *
     * @param text the value to be analyzed for use as the value of this item
     * @see #addValue(Token[])
     */
    public void addValue(String text) {
        Token[] tokens = TokenParser.makeTokens(text, isTokenize());
        addValue(tokens);
    }

    /**
     * Adds the specified value to this List type item.
     *
     * @param tokens an array of tokens
     */
    public void addValue(Token[] tokens) {
        if (type == null) {
            type = ItemType.LIST;
        }
        if (!isListableType()) {
            throw new IllegalArgumentException("The type of this item must be 'array', 'list' or 'set'");
        }
        if (tokensList == null) {
            tokensList = new ArrayList<>();
        }
        tokensList.add(tokens);
    }

    /**
     * Sets a value to this List type item.
     *
     * @param tokensList the tokens list
     */
    public void setValue(List<Token[]> tokensList) {
        if (type == null) {
            type = ItemType.LIST;
        }
        if (!isListableType()) {
            throw new IllegalArgumentException("The item type must be 'array', 'list' or 'set' for this item " + this);
        }
        this.tokensList = tokensList;
    }

    /**
     * Sets a value to this Set type item.
     *
     * @param tokensSet the tokens set
     */
    public void setValue(Set<Token[]> tokensSet) {
        if (tokensSet == null) {
            throw new IllegalArgumentException("Argument 'tokensSet' must not be null");
        }
        if (type == null) {
            type = ItemType.SET;
        }
        if (!isListableType()) {
            throw new IllegalArgumentException("The type of this item must be 'set', 'array' or 'list'");
        }
        tokensList = new ArrayList<>(tokensSet);
    }

    /**
     * Gets the value type of this item.
     *
     * @return the value type of this item
     */
    public ItemValueType getValueType() {
        return valueType;
    }

    /**
     * Sets the value type of this item.
     *
     * @param valueType the new value type
     */
    public void setValueType(ItemValueType valueType) {
        this.valueType = valueType;
    }

    /**
     * Gets the default value of this item.
     *
     * @return the default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the default value of this item.
     *
     * @param defaultValue the new default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns whether to tokenize.
     *
     * @return whether to tokenize
     */
    public Boolean getTokenize() {
        return tokenize;
    }

    /**
     * Returns whether tokenize.
     *
     * @return whether tokenize
     */
    public boolean isTokenize() {
        return !(tokenize == Boolean.FALSE);
    }

    /**
     * Sets whether tokenize.
     *
     * @param tokenize whether tokenize
     */
    public void setTokenize(Boolean tokenize) {
        this.tokenize = tokenize;
    }

    /**
     * Returns whether the item name was auto generated.
     *
     * @return true, if the item name was auto generated
     */
    public boolean isAutoNamed() {
        return autoNamed;
    }

    /**
     * Sets whether the item is an auto generated name.
     *
     * @param autoNamed true, if the item name is auto generated
     */
    public void setAutoNamed(boolean autoNamed) {
        this.autoNamed = autoNamed;
    }

    /**
     * Return whether this item is listable type.
     *
     * @return true, if this item is listable type
     */
    public boolean isListableType() {
        return (type == ItemType.ARRAY || type == ItemType.LIST || type == ItemType.SET);
    }

    /**
     * Return whether this item is mappable type.
     *
     * @return true, if this item is mappable type
     */
    public boolean isMappableType() {
        return (type == ItemType.MAP || type == ItemType.PROPERTIES);
    }

    /**
     * Returns whether this item is mandatory.
     *
     * @return whether or not this item is mandatory
     */
    public Boolean getMandatory() {
        return mandatory;
    }

    /**
     * Returns whether this item is mandatory.
     *
     * @return whether or not this item is mandatory
     */
    public boolean isMandatory() {
        return (mandatory == Boolean.TRUE);
    }

    /**
     * Sets whether this item is mandatory.
     *
     * @param mandatory whether or not this item is mandatory
     */
    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * Returns whether this item requires security input.
     *
     * @return whether or not this item requires security input
     */
    public Boolean getSecurity() {
        return security;
    }

    /**
     * Returns whether this item requires security input.
     *
     * @return whether or not this item requires security input
     */
    public boolean isSecurity() {
        return (security == Boolean.TRUE);
    }

    /**
     * Sets whether this item requires security input.
     *
     * @param security whether or not this item requires security input
     */
    public void setSecurity(Boolean security) {
        this.security = security;
    }

    public Token[] getAllTokens() {
        if (type == ItemType.SINGLE) {
            return tokens;
        } else if (isListableType()) {
            if (tokensList == null || tokensList.isEmpty()) {
                return null;
            } else if (tokensList.size() == 1) {
                return tokensList.get(0);
            } else {
                List<Token> list = new ArrayList<>();
                for (Token[] tokens : tokensList) {
                    Collections.addAll(list, tokens);
                }
                return list.toArray(new Token[0]);
            }
        } else if (isMappableType()) {
            if (tokensMap == null || tokensMap.isEmpty()) {
                return null;
            } else if (tokensMap.size() == 1) {
                Iterator iter = tokensMap.values().iterator();
                if (iter.hasNext()) {
                    return (Token[])iter.next();
                } else {
                    return new Token[0];
                }
            } else {
                List<Token> list = new ArrayList<>();
                for (Token[] tokens : tokensMap.values()) {
                    Collections.addAll(list, tokens);
                }
                return list.toArray(new Token[0]);
            }
        } else {
            return null;
        }
    }

    public boolean containsToken(Token token) {
        Token[] allTokens = getAllTokens();
        if (allTokens != null) {
            for (Token t : allTokens) {
                if (t != null && t.equals(token)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        ToStringBuilder tsb = new ToStringBuilder();
        tsb.append("type", type);
        tsb.append("name", name);
        tsb.append("valueType", valueType);
        if (type == ItemType.SINGLE) {
            tsb.append("value", tokens);
        } else if (isListableType()) {
            tsb.append("value", tokensList);
        } else if (isMappableType()) {
            tsb.append("value", tokensMap);
        }
        tsb.append("tokenize", tokenize);
        tsb.append("mandatory", mandatory);
        tsb.append("security", security);
        tsb.append("autoNamed", autoNamed);
        return tsb.toString();
    }

    /**
     * Gets the class of value.
     *
     * @param ir the item rule
     * @param value the value
     * @return the class of value
     */
    public static Class<?> getPrototypeClass(ItemRule ir, Object value) {
        ItemValueType valueType = ir.getValueType();
        if (ir.getType() == ItemType.ARRAY) {
            if (valueType == ItemValueType.STRING) {
                return String[].class;
            } else if (valueType == ItemValueType.INT) {
                return Integer[].class;
            } else if (valueType == ItemValueType.LONG) {
                return Long[].class;
            } else if (valueType == ItemValueType.FLOAT) {
                return Float[].class;
            } else if (valueType == ItemValueType.DOUBLE) {
                return Double[].class;
            } else if (valueType == ItemValueType.BOOLEAN) {
                return Boolean[].class;
            } else if (valueType == ItemValueType.PARAMETERS) {
                return Parameters[].class;
            } else if (valueType == ItemValueType.FILE) {
                return File[].class;
            } else if (valueType == ItemValueType.MULTIPART_FILE) {
                return FileParameter[].class;
            } else {
                return (value != null ? value.getClass() : String[].class);
            }
        } else if (ir.getType() == ItemType.LIST) {
            return (value != null ? value.getClass() : List.class);
        } else if (ir.getType() == ItemType.MAP) {
            return (value != null ? value.getClass() : Map.class);
        } else if (ir.getType() == ItemType.SET) {
            return (value != null ? value.getClass() : Set.class);
        } else if (ir.getType() == ItemType.PROPERTIES) {
            return (value != null ? value.getClass() : Properties.class);
        } else {
            if (valueType == ItemValueType.STRING) {
                return String.class;
            } else if (valueType == ItemValueType.INT) {
                return Integer.class;
            } else if (valueType == ItemValueType.LONG) {
                return Long.class;
            } else if (valueType == ItemValueType.FLOAT) {
                return Float.class;
            } else if (valueType == ItemValueType.DOUBLE) {
                return Double.class;
            } else if (valueType == ItemValueType.BOOLEAN) {
                return Boolean.class;
            } else if (valueType == ItemValueType.PARAMETERS) {
                return Parameters.class;
            } else if (valueType == ItemValueType.FILE) {
                return File.class;
            } else if (valueType == ItemValueType.MULTIPART_FILE) {
                return FileParameter.class;
            } else {
                return (value != null ? value.getClass() : String.class);
            }
        }
    }

    /**
     * Returns a new derived instance of ItemRule.
     *
     * @param type the type
     * @param name the name
     * @param valueType the value type
     * @param defaultValue the default value
     * @param tokenize whether to tokenize
     * @param mandatory whether or not this item is mandatory
     * @param security whether or not this item requires security input
     * @return the item rule
     * @throws IllegalRuleException if an illegal rule is found
     */
    public static ItemRule newInstance(String type, String name, String valueType, String defaultValue, Boolean tokenize,
                                       Boolean mandatory, Boolean security) throws IllegalRuleException {
        ItemRule itemRule = new ItemRule();

        ItemType itemType = ItemType.resolve(type);
        if (type != null && itemType == null) {
            throw new IllegalRuleException("No item type for '" + type + "'");
        }
        if (itemType != null) {
            itemRule.setType(itemType);
        } else {
            itemRule.setType(ItemType.SINGLE); //default
        }

        if (!StringUtils.isEmpty(name)) {
            itemRule.setName(name);
        } else {
            itemRule.setAutoNamed(true);
        }

        if (tokenize != null) {
            itemRule.setTokenize(tokenize);
        }

        if (valueType != null) {
            ItemValueType itemValueType = ItemValueType.resolve(valueType);
            if (itemValueType == null) {
                throw new IllegalRuleException("No item value type for '" + valueType + "'");
            }
            itemRule.setValueType(itemValueType);
        }

        if (defaultValue != null) {
            itemRule.setDefaultValue(defaultValue);
        }

        if (mandatory != null) {
            itemRule.setMandatory(mandatory);
        }

        if (security != null) {
            itemRule.setSecurity(security);
        }

        return itemRule;
    }

    /**
     * Returns a made reference token.
     *
     * @param bean the bean id
     * @param template the template id
     * @param parameter the parameter name
     * @param attribute the attribute name
     * @param property the property name
     * @return the token
     */
    public static Token makeReferenceToken(String bean, String template, String parameter,
                                           String attribute, String property) {
        Token token;
        if (bean != null) {
            token = new Token(TokenType.BEAN, bean);
        } else if (template != null) {
            token = new Token(TokenType.TEMPLATE, template);
        } else if (parameter != null) {
            token = new Token(TokenType.PARAMETER, parameter);
        } else if (attribute != null) {
            token = new Token(TokenType.ATTRIBUTE, attribute);
        } else if (property != null) {
            token = new Token(TokenType.PROPERTY, property);
        } else {
            token = null;
        }
        return token;
    }

    /**
     * Returns a {@code Token} iterator.
     *
     * @param itemRule the item rule
     * @return the iterator for tokens
     */
    public static Iterator<Token[]> tokenIterator(ItemRule itemRule) {
        Iterator<Token[]> iter = null;
        if (itemRule.isListableType()) {
            List<Token[]> list = itemRule.getTokensList();
            if (list != null) {
                iter = list.iterator();
            }
        } else if (itemRule.isMappableType()) {
            Map<String, Token[]> map = itemRule.getTokensMap();
            if (map != null) {
                iter = map.values().iterator();
            }
        } else {
            return new Iterator<Token[]>() {
                private int count = 0;
                @Override
                public boolean hasNext() {
                    return (count++ < 1);
                }
                @Override
                public Token[] next() {
                    return itemRule.getTokens();
                }
                @Override
                public void remove() {
                    throw new UnsupportedOperationException("Cannot remove an element of an array");
                }
            };
        }
        return iter;
    }

    /**
     * Convert the given item parameters list into an {@code ItemRuleMap}.
     *
     * @param itemParametersList the item parameters list to convert
     * @return the item rule map
     * @throws IllegalRuleException if an illegal rule is found
     */
    public static ItemRuleMap toItemRuleMap(List<ItemParameters> itemParametersList) throws IllegalRuleException {
        if (itemParametersList == null || itemParametersList.isEmpty()) {
            return null;
        }
        ItemRuleMap itemRuleMap = new ItemRuleMap();
        for (ItemParameters parameters : itemParametersList) {
            itemRuleMap.putItemRule(toItemRule(parameters));
        }
        return itemRuleMap;
    }

    /**
     * Convert the given item parameters list into an {@code ItemRuleList}.
     *
     * @param itemParametersList the item parameters list to convert
     * @return the item rule list
     * @throws IllegalRuleException if an illegal rule is found
     */
    public static ItemRuleList toItemRuleList(List<ItemParameters> itemParametersList) throws IllegalRuleException {
        ItemRuleList itemRuleList = new ItemRuleList();
        for (ItemParameters parameters : itemParametersList) {
            itemRuleList.add(ItemRule.toItemRule(parameters));
        }
        return itemRuleList;
    }

    /**
     * Convert the given item parameters into an {@code ItemRule}.
     * <pre>
     * [
     *   {
     *     type: "map"
     *     name: "property1"
     *     value: {
     *       code1: "value1"
     *       code2: "value2"
     *     }
     *     valueType: "java.lang.String"
     *     defaultValue: "default value"
     *     tokenize: true
     *   }
     *   {
     *     name: "property2"
     *     value(int): 123
     *   }
     *   {
     *     name: "property2"
     *     reference: {
     *       bean: "a.bean"
     *     }
     *   }
     * ]
     * </pre>
     *
     * @param itemParameters the item parameters
     * @return an instance of {@code ItemRule}
     * @throws IllegalRuleException if an illegal rule is found
     */
    public static ItemRule toItemRule(ItemParameters itemParameters) throws IllegalRuleException {
        String type = itemParameters.getString(ItemParameters.type);
        String name = itemParameters.getString(ItemParameters.name);
        String valueType = itemParameters.getString(ItemParameters.valueType);
        String defaultValue = itemParameters.getString(ItemParameters.defaultValue);
        Boolean tokenize = itemParameters.getBoolean(ItemParameters.tokenize);
        Boolean mandatory = itemParameters.getBoolean(ItemParameters.mandatory);
        Boolean security = itemParameters.getBoolean(ItemParameters.security);
        Parameters callParameters = itemParameters.getParameters(ItemParameters.call);

        ItemRule itemRule = ItemRule.newInstance(type, name, valueType, defaultValue, tokenize, mandatory, security);

        if (callParameters != null) {
            String bean = StringUtils.emptyToNull(callParameters.getString(CallParameters.bean));
            String template = StringUtils.emptyToNull(callParameters.getString(CallParameters.template));
            String parameter = StringUtils.emptyToNull(callParameters.getString(CallParameters.parameter));
            String attribute = StringUtils.emptyToNull(callParameters.getString(CallParameters.attribute));
            String property = StringUtils.emptyToNull(callParameters.getString(CallParameters.property));

            Token t = ItemRule.makeReferenceToken(bean, template, parameter, attribute, property);
            if (t != null) {
                Token[] tokens = new Token[] { t };
                if (itemRule.isListableType()) {
                    itemRule.addValue(tokens);
                } else {
                    itemRule.setValue(tokens);
                }
            }
        } else {
            if (itemRule.isListableType()) {
                List<String> stringList = itemParameters.getStringList(ItemParameters.value);
                if (stringList != null) {
                    for (String text : stringList) {
                        itemRule.addValue(text);
                    }
                }
            } else if (itemRule.isMappableType()) {
                Parameters parameters = itemParameters.getParameters(ItemParameters.value);
                if (parameters != null) {
                    Set<String> parametersNames = parameters.getParameterNameSet();
                    if (parametersNames != null) {
                        for (String valueName : parametersNames) {
                            String text = parameters.getString(valueName);
                            itemRule.putValue(valueName, text);
                        }
                    }
                }
            } else {
                String text = itemParameters.getString(ItemParameters.value);
                itemRule.setValue(text);
            }
        }

        return itemRule;
    }

    /**
     * Convert the given {@code String} into an Item {@code Parameters}.
     *
     * @param text the {@code String} to convert
     * @return the item parameters list
     */
    public static List<ItemParameters> toItemParametersList(String text) {
        Parameters holder = new ItemHolderParameters(text);
        return holder.getParametersList(ItemHolderParameters.item);
    }

    /**
     * Convert the given {@code String} into an {@code ItemRuleMap}.
     *
     * @param text the {@code String} to convert
     * @return an {@code ItemRuleMap}
     * @throws IllegalRuleException if an illegal rule is found
     */
    public static ItemRuleMap toItemRuleMap(String text) throws IllegalRuleException {
        Parameters holder = new ItemHolderParameters(text);
        List<ItemParameters> itemParametersList = holder.getParametersList(ItemHolderParameters.item);
        return toItemRuleMap(itemParametersList);
    }

    public static ItemRuleMap toItemRuleMap(Parameter[] parameters) throws IllegalRuleException {
        if (parameters == null || parameters.length == 0) {
            return null;
        }
        ItemRuleMap itemRuleMap = new ItemRuleMap();
        for (Parameter parameter : parameters) {
            itemRuleMap.putItemRule(toItemRule(parameter));
        }
        return itemRuleMap;
    }

    public static ItemRule toItemRule(Parameter parameter) throws IllegalRuleException {
        String name = parameter.name();
        String value = parameter.value();
        String defaultValue = parameter.defaultValue();
        boolean tokenize = parameter.tokenize();
        boolean mandatory = parameter.mandatory();
        boolean security = parameter.security();

        ItemRule itemRule = ItemRule.newInstance(null, name, null, defaultValue, tokenize, mandatory, security);
        itemRule.setValue(value);
        return itemRule;
    }

    public static ItemRuleMap toItemRuleMap(Attribute[] attributes) throws IllegalRuleException {
        if (attributes == null || attributes.length == 0) {
            return null;
        }
        ItemRuleMap itemRuleMap = new ItemRuleMap();
        for (Attribute attribute : attributes) {
            itemRuleMap.putItemRule(toItemRule(attribute));
        }
        return itemRuleMap;
    }

    public static ItemRule toItemRule(Attribute attribute) throws IllegalRuleException {
        String name = attribute.name();
        String value = attribute.value();
        String defaultValue = attribute.defaultValue();
        boolean tokenize = attribute.tokenize();
        boolean mandatory = attribute.mandatory();
        boolean security = attribute.security();

        ItemRule itemRule = ItemRule.newInstance(null, name, null, defaultValue, tokenize, mandatory, security);
        itemRule.setValue(value);
        return itemRule;
    }

}
