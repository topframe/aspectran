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
package com.aspectran.core.context.rule;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aspectran.core.activity.request.parameter.FileParameter;
import com.aspectran.core.context.builder.apon.params.ItemHolderParameters;
import com.aspectran.core.context.builder.apon.params.ItemParameters;
import com.aspectran.core.context.builder.apon.params.ReferenceParameters;
import com.aspectran.core.context.expr.token.Token;
import com.aspectran.core.context.expr.token.TokenParser;
import com.aspectran.core.context.rule.type.ItemType;
import com.aspectran.core.context.rule.type.ItemValueType;
import com.aspectran.core.context.rule.type.TokenType;
import com.aspectran.core.util.StringUtils;
import com.aspectran.core.util.apon.Parameters;

/**
 * The Class ItemRule.
 * 
 * <p>Created: 2008. 03. 27 오후 3:57:48</p>
 * 
 * @author Juho Jeong
 */
public class ItemRule {

	/**  suffix for array-type item: "[]". */
	public static final String ARRAY_SUFFIX = "[]";
	
	/**  suffix for map-type item: "{}". */
	public static final String MAP_SUFFIX = "{}";

	private ItemType type;
	
	private String name;
	
	private ItemValueType valueType;
	
	private String defaultValue;

	private Boolean tokenize;

	private Token[] tokens;
	
	private List<Token[]> tokensList;
	
	private Map<String, Token[]> tokensMap;
	
	private boolean autoGeneratedName;

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
	 * Returns the value of the item.
	 * 
	 * @return the value of the item
	 */
	public String getValue() {
		return toString(tokens);
	}
	
	/**
	 * Gets the value type  of the item.
	 *
	 * @return the value type of the item
	 */
	public ItemValueType getValueType() {
		return valueType;
	}

	/**
	 * Sets the value type of the item.
	 *
	 * @param valueType the new value type of the item
	 */
	public void setValueType(ItemValueType valueType) {
		this.valueType = valueType;
	}

	/**
	 * Gets the default value.
	 *
	 * @return the default value
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets the default value.
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
	 * Returns whether to tokenize.
	 *
	 * @return whether to tokenize
	 */
	public boolean isTokenize() {
		return !(tokenize == Boolean.FALSE);
	}
	
	/**
	 * Sets whether to tokenize.
	 *
	 * @param tokenize whether to tokenize
	 */
	public void setTokenize(Boolean tokenize) {
		this.tokenize = tokenize;
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
	 * Gets the value list.
	 *
	 * @return the value list
	 */
	public List<String> getValueList() {
		if(tokensList == null)
			return null;
		
		List<String> list = new ArrayList<String>();
		
		if(tokensList.size() == 0)
			return list;
		
		for(Token[] tokens : tokensList) {
			list.add(toString(tokens));
		}
		
		return list;
	}
	
	/**
	 * Gets the tokens map.
	 * 
	 * @return the tokens map
	 */
	public Map<String, Token[]> getTokensMap() {
		return tokensMap;
	}

	public Map<String, String> getValueMap() {
		if(tokensMap == null)
			return null;
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		if(tokensMap.size() == 0)
			return map;
		
		for(Map.Entry<String, Token[]> entry : tokensMap.entrySet()) {
			map.put(entry.getKey(), toString(entry.getValue()));
		}
		
		return map;
	}
	
	/**
	 * Returns whether item name is auto generated.
	 *
	 * @return true, if is auto generated name
	 */
	public boolean isAutoGeneratedName() {
		return autoGeneratedName;
	}

	/**
	 * Sets whether item name is auto generated.
	 *
	 * @param autoGeneratedName the new unknown name
	 */
	public void setAutoGeneratedName(boolean autoGeneratedName) {
		this.autoGeneratedName = autoGeneratedName;
	}

	/**
	 * Convert to string from token array.
	 *
	 * @param tokens the tokens
	 * @return the string
	 */
	private String toString(Token[] tokens) {
		if(tokens == null)
			return null;
		
		if(tokens.length == 0)
			return StringUtils.EMPTY;
		
		StringBuilder sb = new StringBuilder();
		
		for(Token t : tokens) {
			sb.append(t.toString());
		}
		
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{type=").append(type.toString());
		sb.append(", name=").append(name);
		sb.append(", valueType=").append(valueType);
		sb.append(", value=");
		
		if(type == ItemType.SINGULAR) {
			if(tokens != null) {
				for(Token t : tokens) {
					sb.append(t.toString());
				}
			}
		} else if(type == ItemType.ARRAY || type == ItemType.LIST || type == ItemType.SET) {
			if(tokensList != null) {
				sb.append('[');

				for(int i = 0; i < tokensList.size(); i++) {
					Token[] ts = tokensList.get(i);
					
					if(i > 0)
						sb.append(", ");

					for(Token t : ts) {
						sb.append(t.toString());
					}
				}

				sb.append(']');
			}
		} else if(type == ItemType.MAP || type == ItemType.PROPERTIES) {
			if(tokensMap != null) {
				sb.append('{');
				
				Iterator<String> iter = tokensMap.keySet().iterator();
				String key = null;
				
				while(iter.hasNext()) {
					if(key != null)
						sb.append(", ");

					key = iter.next();
					Token[] ts = tokensMap.get(key);
					
					sb.append(key).append("=");
					
					for(Token t : ts) {
						sb.append(t.toString());
					}
				}
				
				sb.append('}');
			}
		}
		sb.append(", tokenize=").append(tokenize);
		sb.append(", autoGeneratedName=").append(autoGeneratedName);
		sb.append("}");
		
		return sb.toString();
	}

	/**
	 * Sets the name of a item.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		if(name.endsWith(ARRAY_SUFFIX)) {
			this.name = name.substring(0, name.length() - 2);
			type = ItemType.ARRAY;
		} else if(name.endsWith(MAP_SUFFIX)) {
			this.name = name.substring(0, name.length() - 2);
			type = ItemType.MAP;
		} else {
			this.name = name;
			
			if(type == null)
				type = ItemType.SINGULAR;
		}
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		Token[] tokens;
		
		if(isTokenize())
			tokens = TokenParser.parse(value);
		else {
			tokens = new Token[1];
			tokens[0] = new Token(TokenType.TEXT, value);
		}

		setValue(tokens);
	}

	/**
	 * Sets the value.
	 *
	 * @param tokens the new value
	 */
	public void setValue(Token[] tokens) {
		checkValueType(ItemType.SINGULAR, null, null);
		this.tokens = tokens;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param tokensList the new value
	 */
	public void setValue(List<Token[]> tokensList) {
		checkValueType(ItemType.ARRAY, ItemType.LIST, ItemType.SET);
		this.tokensList = tokensList;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param tokensMap the tokens map
	 */
	public void setValue(Map<String, Token[]> tokensMap) {
		checkValueType(ItemType.MAP, ItemType.PROPERTIES, null);
		this.tokensMap = tokensMap;
	}

	/**
	 * Convert the given string into tokens.
	 *
	 * @param text the text
	 * @return the token[]
	 */
	public Token[] makeTokens(String text) {
		Token[] tokens;
		
		if(isTokenize())
			tokens = TokenParser.parse(text);
		else {
			tokens = new Token[1];
			tokens[0] = new Token(TokenType.TEXT, text);
		}
		
		return tokens;
	}
	
	/**
	 * Check value type.
	 *
	 * @param compareItemType the compare item type
	 */
	private void checkValueType(ItemType compareItemType, ItemType compareItemType2, ItemType compareItemType3) {
		if(type == null)
			throw new IllegalArgumentException("item-type is required");
		
		if(type != compareItemType && type != compareItemType2 && type != compareItemType3)
			throw new IllegalArgumentException("The item-type of violation has occurred. current item-type: " + type.toString());
	}
	
	/**
	 * Gets the class of value.
	 *
	 * @param ir the item rule
	 * @param value the value
	 * @return the class of value
	 */
	public static Class<?> getClassOfValue(ItemRule ir, Object value) {
		ItemValueType valueType = ir.getValueType();
		
		if(ir.getType() == ItemType.ARRAY) {
			if(valueType == ItemValueType.STRING) {
				return String[].class;
			} else if(valueType == ItemValueType.INT) {
				return Integer[].class;
			} else if(valueType == ItemValueType.LONG) {
				return Long[].class;
			} else if(valueType == ItemValueType.FLOAT) {
				return Float[].class;
			} else if(valueType == ItemValueType.DOUBLE) {
				return Double[].class;
			} else if(valueType == ItemValueType.BOOLEAN) {
				return Boolean[].class;
			} else if(valueType == ItemValueType.PARAMETERS) {
				return Parameters[].class;
			} else if(valueType == ItemValueType.FILE) {
				return File[].class;
			} else if(valueType == ItemValueType.MULTIPART_FILE) {
				return FileParameter[].class;
			}
		}
		
		if(value != null)
			return value.getClass();
		
		return Object.class;
	}
	
	/**
	 * Returns a new derived instance of ItemRule.
	 *
	 * @param type the type
	 * @param name the name
	 * @param value the value
	 * @param valueType the value type
	 * @param defaultValue the default value
	 * @param tokenize whether to tokenize
	 * @return the item rule
	 */
	public static ItemRule newInstance(String type, String name, String value, String valueType, String defaultValue, Boolean tokenize) {
		ItemRule itemRule = new ItemRule();
		
		ItemType itemType = ItemType.valueOf(type);
		
		if(type != null && itemType == null)
			throw new IllegalArgumentException("No item type registered for '" + type + "'.");
		
		if(itemType != null)
			itemRule.setType(itemType);
		else
			itemRule.setType(ItemType.SINGULAR); //default

		if(!StringUtils.isEmpty(name)) {
			itemRule.setName(name);
		} else {
			itemRule.setAutoGeneratedName(true);
		}

		if(value != null)
			itemRule.setValue(value);
		
		ItemValueType itemValueType = ItemValueType.valueOf(valueType);
		
		if(valueType != null && itemValueType == null)
			throw new IllegalArgumentException("No item value type registered for '" + valueType + "'.");
		
		itemRule.setValueType(itemValueType);

		if(defaultValue != null)
			itemRule.setDefaultValue(defaultValue);
		
		if(tokenize != null)
			itemRule.setTokenize(tokenize);
		
		return itemRule;
	}
	
	/**
	 * Update reference.
	 *
	 * @param itemRule the item rule
	 * @param beanId the bean id
	 * @param parameter the parameter name
	 * @param attribute the attribute name
	 * @param property the bean's property
	 */
	public static void updateReference(ItemRule itemRule, String beanId, String parameter, String attribute, String property) {
		Token[] tokens = makeReferenceTokens(beanId, parameter, attribute, property);
		
		if(tokens[0] != null)
			itemRule.setValue(tokens);
	}
	
	/**
	 * Make reference tokens.
	 *
	 * @param beanId the bean id
	 * @param parameter the parameter
	 * @param attribute the attribute
	 * @param property the property
	 * @return the token[]
	 */
	public static Token[] makeReferenceTokens(String beanId, String parameter, String attribute, String property) {
		Token[] tokens = new Token[1];
		
		if(!StringUtils.isEmpty(beanId)) {
			tokens[0] = new Token(TokenType.REFERENCE_BEAN, beanId);
			
			if(!StringUtils.isEmpty(property))
				tokens[0].setGetterName(property);
		} else if(!StringUtils.isEmpty(parameter))
			tokens[0] = new Token(TokenType.PARAMETER, parameter);
		else if(!StringUtils.isEmpty(attribute))
			tokens[0] = new Token(TokenType.ATTRIBUTE, attribute);
		else
			tokens[0] = null;
		
		return tokens;
	}
	
	/**
	 * Returns Token iterator.
	 *
	 * @param itemRule the item rule
	 * @return the iterator
	 */
	public static Iterator<Token[]> tokenIterator(ItemRule itemRule) {
		Iterator<Token[]> iter = null;
		
		if(itemRule.getType() == ItemType.ARRAY || itemRule.getType() == ItemType.LIST || itemRule.getType() == ItemType.SET) {
			List<Token[]> list = itemRule.getTokensList();
			iter = list.iterator();
		} else if(itemRule.getType() == ItemType.MAP || itemRule.getType() == ItemType.PROPERTIES) {
			Map<String, Token[]> map = itemRule.getTokensMap();
			if(map != null)
				iter = map.values().iterator();
		}
		
		return iter;
	}
	
	/**
	 * Parses the value.
	 *
	 * @param itemRule the item rule
	 * @param valueName the value name
	 * @param valueText the value text
	 * @return the token[]
	 */
	public static Token[] parseValue(ItemRule itemRule, String valueName, String valueText) {
		if(itemRule.getType() == ItemType.SINGULAR) {
			if(valueText != null) {
				itemRule.setValue(valueText);
			}
			
			return null;
		} else {
			Token[] tokens = null;
			
			if(itemRule.getType() == ItemType.ARRAY || itemRule.getType() == ItemType.LIST || itemRule.getType() == ItemType.SET) {
				tokens = itemRule.makeTokens(valueText);
			} else if(itemRule.getType() == ItemType.MAP || itemRule.getType() == ItemType.PROPERTIES) {
				if(!StringUtils.isEmpty(valueText)) {
					tokens = itemRule.makeTokens(valueText);
				}
			}
			
			return tokens;
		}
	}
	
	/**
	 * Begin value collection.
	 *
	 * @param itemRule the item rule
	 */
	public static void beginValueCollection(ItemRule itemRule) {
		if(itemRule.getType() == ItemType.ARRAY || itemRule.getType() == ItemType.LIST || itemRule.getType() == ItemType.SET) {
			List<Token[]> tokensList = new ArrayList<Token[]>();
			itemRule.setValue(tokensList);
		} else if(itemRule.getType() == ItemType.MAP || itemRule.getType() == ItemType.PROPERTIES) {
			Map<String, Token[]> tokensMap = new LinkedHashMap<String, Token[]>();
			itemRule.setValue(tokensMap);
		}
	}
	
	/**
	 * Finish value collection.
	 *
	 * @param itemRule the item rule
	 * @param name the name
	 * @param tokens the tokens
	 */
	public static void flushValueCollection(ItemRule itemRule, String name, Token[] tokens) {
		if(itemRule.getType() == ItemType.ARRAY || itemRule.getType() == ItemType.LIST || itemRule.getType() == ItemType.SET) {
			List<Token[]> list = itemRule.getTokensList();
			list.add(tokens);
		} else if(itemRule.getType() == ItemType.MAP || itemRule.getType() == ItemType.PROPERTIES) {
			if(!StringUtils.isEmpty(name)) {
				Map<String, Token[]> map = itemRule.getTokensMap();
				map.put(name, tokens);
			}
		}
	}
	
	/**
	 * Adds the item rule.
	 *
	 * @param itemRuleMap the item rule map
	 * @param itemRule the item rule
	 */
	public static void addItemRule(ItemRule itemRule, ItemRuleMap itemRuleMap) {
		if(itemRule.isAutoGeneratedName()) {
			generateItemName(itemRule, itemRuleMap);
		}
		
		itemRuleMap.putItemRule(itemRule);
	}

	
	/**
	 * Naming for Unnamed item name.
	 *
	 * @param itemRule the item rule
	 * @param itemRuleMap the item rule map
	 */
	private static void generateItemName(ItemRule itemRule, ItemRuleMap itemRuleMap) {
		int count = 1;
		
		for(ItemRule ir : itemRuleMap) {
			if(ir.isAutoGeneratedName() && ir.getType() == itemRule.getType()) {
				count++;
				
				if(itemRule == ir)
					break;
			}
		}
		
		if(itemRule.getType() != ItemType.SINGULAR || itemRule.getValueType() == null) {
			String name = itemRule.getType().toString() + count;
			itemRule.setName(name);
		} else {
			if(itemRule.getValueType() != null) {
				String name = itemRule.getValueType().toString() + count;
				itemRule.setName(name);
			}
		}
	}

	/**
	 * Convert to item rule map from parameters list.
	 *
	 * @param itemParametersList the item parameters list
	 * @return the item rule map
	 */
	public static ItemRuleMap toItemRuleMap(List<Parameters> itemParametersList) {
		if(itemParametersList == null || itemParametersList.isEmpty())
			return null;
		
		ItemRuleMap itemRuleMap = new ItemRuleMap();
		
		for(Parameters parameters : itemParametersList) {
			ItemRule itemRule = toItemRule(parameters);

			if(StringUtils.isEmpty(itemRule.getName())) {
				itemRule.setAutoGeneratedName(true);
				generateItemName(itemRule, itemRuleMap);
			}

			itemRuleMap.putItemRule(itemRule);
		}
		
		return itemRuleMap;
	}
	
	/**
	 * Convert then Parameters to the item rule.
	 * <pre>
	 * [
	 * 	{
	 * 		type: map
	 * 		name: property1
	 * 		value: {
	 * 			code1: value1
	 * 			code2: value2
	 * 		}
	 * 		valueType: java.lang.String
	 * 		defaultValue: default value
	 * 		tokenize: true
	 * 	}
	 * 	{
	 * 		name: property2
	 * 		value(int): 123
	 * 	}
	 * 	{
	 * 		name: property2
	 * 		reference: {
	 * 			bean: a.bean
	 * 		}
	 * 	}
	 * ]
	 * </pre>
	 *
	 * @param itemParameters the item parameters
	 * @return the item rule
	 */
	public static ItemRule toItemRule(Parameters itemParameters) {
		String type = itemParameters.getString(ItemParameters.type);
		String name = itemParameters.getString(ItemParameters.name);
		String valueType = itemParameters.getString(ItemParameters.valueType);
		String defaultValue = itemParameters.getString(ItemParameters.defaultValue);
		Boolean tokenize = itemParameters.getBoolean(ItemParameters.tokenize);
		Parameters referenceParameters = itemParameters.getParameters(ItemParameters.reference);
		
		ItemRule itemRule = ItemRule.newInstance(type, name, null, valueType, defaultValue, tokenize);
		
		if(referenceParameters != null) {
			String bean = referenceParameters.getString(ReferenceParameters.bean);
			String parameter = referenceParameters.getString(ReferenceParameters.parameter);
			String attribute = referenceParameters.getString(ReferenceParameters.attribute);
			String property = referenceParameters.getString(ReferenceParameters.property);
			
			updateReference(itemRule, bean, parameter, attribute, property);
		} else {
			if(itemRule.getType() == ItemType.SINGULAR) {
				String value = itemParameters.getString(ItemParameters.value);
				parseValue(itemRule, null, value);
			} else if(itemRule.getType() == ItemType.ARRAY || itemRule.getType() == ItemType.LIST || itemRule.getType() == ItemType.SET) {
				List<String> stringList = itemParameters.getStringList(ItemParameters.value);
				
				if(stringList != null) {
					beginValueCollection(itemRule);
					for(String value : stringList) {
						Token[] tokens = parseValue(itemRule, null, value);
						flushValueCollection(itemRule, name, tokens);
					}
				}
			} else if(itemRule.getType() == ItemType.MAP || itemRule.getType() == ItemType.PROPERTIES) {
				Parameters parameters = itemParameters.getParameters(ItemParameters.value);

				if(parameters != null) {
					Set<String> parametersNames = parameters.getParameterNameSet();
					
					if(parametersNames != null) {
						beginValueCollection(itemRule);
						for(String valueName : parametersNames) {
							Token[] tokens = parseValue(itemRule, valueName, parameters.getString(valueName));
							flushValueCollection(itemRule, valueName, tokens);
						}
					}
				} 
			}
		}
		
		return itemRule;
	}
	
	/**
	 * Convert to item parameters from a string.
	 *
	 * @param text the text
	 * @return the list
	 */
	public static List<Parameters> toItemParametersList(String text) {
		Parameters holder = new ItemHolderParameters(text);
		return holder.getParametersList(ItemHolderParameters.item);
	}
	
}
