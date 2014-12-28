package com.aspectran.core.var.apon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.aspectran.core.util.StringUtils;

public abstract class AbstractParameters implements Parameters {

	private static final String DELIMITERS = "\n\r\f";
	
	private static final String CURLY_BRAKET_OPEN = "{";

	private static final String CURLY_BRAKET_CLOSE = "}";
	
	private static final String SQUARE_BRAKET_OPEN = "[";
	
	private static final String SQUARE_BRAKET_CLOSE = "]";

	protected final Map<String, ParameterValue> parameterValueMap;
	
	private final String title;
	
	private final String plaintext;
	
	private ParameterValue parent;
	
	private boolean preparsed;
	
	protected AbstractParameters(String title, ParameterValue[] parameterValues) {
		this(title, parameterValues, null);
	}

	protected AbstractParameters(String title, ParameterValue[] parameterValues, String plaintext) {
		this.title = title;
		this.plaintext = plaintext;
		
		if(parameterValues == null && plaintext != null) {
			parameterValues = preparse(plaintext);
		}
		
		this.parameterValueMap = new HashMap<String, ParameterValue>();
		
		if(parameterValues != null) {
			for(ParameterValue parameterValue : parameterValues) {
				parameterValue.setHolder(this);
				parameterValueMap.put(parameterValue.getName(), parameterValue);
			}
		}

		if(plaintext != null)
			valuelize(plaintext);
	}
	
	public ParameterValue getParent() {
		return parent;
	}

	public void setParent(ParameterValue parent) {
		this.parent = parent;
	}

	public String getTitle() {
		return title;
	}

	public String getQualifiedName() {
		if(parent != null)
			return parent.getQualifiedName();
		
		return title;
	}

	public ParameterValue getParameter(String name) {
		ParameterValue p = parameterValueMap.get(name);
		
		if(p == null)
			throw new UnknownParameterException(name, this);
		
		return p;
	}

	public Object getValue(String name) {
		ParameterValue p = getParameter(name);
		return p.getValue();
	}
	
	public Object getValue(ParameterValue parameter) {
		return getValue(parameter.getName());
	}
	
	public String getString(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsString();
	}

	public String getString(String name, String defaultValue) {
		String s = getString(name);
		
		if(s == null)
			return defaultValue;
		
		return s;
	}

	public String[] getStringArray(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsStringArray();
	}

	public String getString(ParameterValue parameter) {
		return getString(parameter.getName());
	}
	
	public String getString(ParameterValue parameter, String defaultValue) {
		return getString(parameter.getName(), defaultValue);
	}

	public String[] getStringArray(ParameterValue parameter) {
		return getStringArray(parameter.getName());
	}
	
	public int getInt(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsInt();
	}
	
	public int getInt(String name, int defaultValue) {
		ParameterValue p = getParameter(name);
		
		if(p == null)
			return defaultValue;
		
		return p.getValueAsInt();
	}

	public int[] getIntArray(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsIntArray();
	}

	public int getInt(ParameterValue parameter) {
		return getInt(parameter.getName());
	}

	public int getInt(ParameterValue parameter, int defaultValue) {
		return getInt(parameter.getName(), defaultValue);
	}

	public int[] getIntArray(ParameterValue parameter) {
		return getIntArray(parameter.getName());
	}
	
	public long getLong(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsLong();
	}
	
	public long getLong(String name, long defaultValue) {
		ParameterValue p = getParameter(name);
		
		if(p == null)
			return defaultValue;
		
		return p.getValueAsLong();
	}
	
	public long[] getLongArray(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsLongArray();
	}
	
	public long getLong(ParameterValue parameter) {
		return getLong(parameter.getName());
	}
	
	public long getLong(ParameterValue parameter, long defaultValue) {
		return getLong(parameter.getName());
	}
	
	public long[] getLongArray(ParameterValue parameter) {
		return getLongArray(parameter.getName());
	}
	
	public float getFloat(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsFloat();
	}
	
	public float getFloat(String name, float defaultValue) {
		ParameterValue p = getParameter(name);
		
		if(p == null)
			return defaultValue;
		
		return p.getValueAsFloat();
	}

	public float[] getFloatArray(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsFloatArray();
	}
	
	public float getFloat(ParameterValue parameter) {
		return getFloat(parameter.getName());
	}
	
	public float getFloat(ParameterValue parameter, float defaultValue) {
		return getFloat(parameter.getName(), defaultValue);
	}
	
	public float[] getFloatArray(ParameterValue parameter) {
		return getFloatArray(parameter.getName());
	}
	
	public double getDouble(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsDouble();
	}
	
	public double getDouble(String name, double defaultValue) {
		ParameterValue p = getParameter(name);
		
		if(p == null)
			return defaultValue;
		
		return p.getValueAsDouble();
	}

	public double[] getDoubleArray(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsDoubleArray();
	}
	
	public double getDouble(ParameterValue parameter) {
		return getDouble(parameter.getName());
	}
	
	public double getDouble(ParameterValue parameter, double defaultValue) {
		return getDouble(parameter.getName(), defaultValue);
	}
	
	public double[] getDoubleArray(ParameterValue parameter) {
		return getDoubleArray(parameter.getName());
	}
	
	public boolean getBoolean(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsBoolean();
	}
	
	public boolean getBoolean(String name, boolean defaultValue) {
		ParameterValue p = getParameter(name);
		
		if(p == null)
			return defaultValue;
		
		return p.getValueAsBoolean();
	}
	
	public boolean[] getBooleanArray(String name) {
		ParameterValue p = getParameter(name);
		return p.getValueAsBooleanArray();
	}
	
	public boolean getBoolean(ParameterValue parameter) {
		return getBoolean(parameter.getName());
	}
	
	public boolean getBoolean(ParameterValue parameter, boolean defaultValue) {
		return getBoolean(parameter.getName(), defaultValue);
	}
	
	public boolean[] getBooleanArray(ParameterValue parameter) {
		return getBooleanArray(parameter.getName());
	}
	
	public Parameters getParameters(String name) {
		ParameterValue p = getParameter(name);
		return (Parameters)p.getValue();
	}
	
	public Parameters[] getParametersArray(String name) {
		ParameterValue p = getParameter(name);
		return p.getParametersArray();
	}
	
	public Parameters getParameters(ParameterValue parameter) {
		return getParameters(parameter.getName());
	}
	
	public Parameters[] getParametersArray(ParameterValue parameter) {
		return getParametersArray(parameter.getName());
	}
	
	public String toPlaintext() {
		return plaintext;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{title=").append(title);
		sb.append(", qualifiedName=").append(getQualifiedName());
		sb.append("}");
		
		return sb.toString();
	}
	
	protected ParameterValue[] preparse(String plaintext) {
		StringTokenizer st = new StringTokenizer(plaintext, DELIMITERS);
		
		ParameterValue[] parameterValues = preparse(st, null);
		
		preparsed = true;
		
		return parameterValues;
	}
	
	protected ParameterValue[] preparse(StringTokenizer st, ParameterValue parentParameterValue) {
		List<ParameterValue> parameterValueList = new ArrayList<ParameterValue>();
		
		preparse(st, parameterValueList, parentParameterValue != null ? CURLY_BRAKET_OPEN : null, null);
		
		ParameterValue[] parameterValues = parameterValueList.toArray(new ParameterValue[parameterValueList.size()]);
		
		if(parentParameterValue != null) {
			Parameters parameters = new GenericParameters(parentParameterValue.getName(), parameterValues);
			parentParameterValue.setValue(parameters);
		}
		
		return parameterValues;
	}
	
	protected void preparse(StringTokenizer st, List<ParameterValue> parameterValueList, String openBraket, ParameterValue parameterValue) {
		String name = null;
		String value = null;
		
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			
			if(StringUtils.hasText(token)) {
				token = token.trim();

				if(openBraket != null) {
					if(openBraket == CURLY_BRAKET_OPEN && CURLY_BRAKET_CLOSE.equals(token) ||
							openBraket == SQUARE_BRAKET_OPEN && SQUARE_BRAKET_CLOSE.equals(token))
						return;
				}
				
				if(openBraket == SQUARE_BRAKET_OPEN) {
					value = token;
				} else {
					int index = token.indexOf(":");

					if(index == -1)
						throw new InvalidParameterException(title + ": Cannot parse into name-value pair. \"" + token + "\"");

					name = token.substring(0, index).trim();
					value = token.substring(index + 1).trim();
				}

				if(StringUtils.hasText(value)) {
					if(CURLY_BRAKET_OPEN.equals(value)) {
						if(openBraket == SQUARE_BRAKET_OPEN) {
							preparse(st, parameterValue);
						} else {
							ParameterValue pv = new ParameterValue(name, ParameterValueType.PARAMETERS);
							parameterValueList.add(pv);
							preparse(st, pv);
						}
					} else if(openBraket != SQUARE_BRAKET_OPEN) {
						ParameterValueType parameterValueType = ParameterValueType.valueOfHint(name);
						
						if(parameterValueType == null)
							parameterValueType = ParameterValueType.STRING;

						if(SQUARE_BRAKET_OPEN.equals(value)) {
							ParameterValue pv = new ParameterArrayValue(name, parameterValueType);
							parameterValueList.add(pv);
	
							preparse(st, parameterValueList, SQUARE_BRAKET_OPEN, pv);
						} else {
							ParameterValue pv = new ParameterValue(name, parameterValueType);
							parameterValueList.add(pv);
						}
					}
				}
			}
		}
		
		if(openBraket != null) {
			if(openBraket == CURLY_BRAKET_OPEN) {
				throw new InvalidParameterException(title + ": Cannot parse value of '" + name + "' to an array of strings.");
			} else if(openBraket == SQUARE_BRAKET_OPEN) {
				throw new InvalidParameterException(title + ": Cannot parse value of '" + name + "' to an array of strings.");
			}
		}
	}
	
	protected void valuelize(String plaintext) {
		StringTokenizer st = new StringTokenizer(plaintext, DELIMITERS);

		valuelize(st, null, null);
	}
	
	protected void valuelize(StringTokenizer st, String openBraket, ParameterValue parameterValue) {
		String name = null;
		String value = null;
		
		int curlyBraketCount = 0;
		
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			
			if(StringUtils.hasText(token)) {
				token = token.trim();
				
				if(openBraket != null) {
					if(openBraket == CURLY_BRAKET_OPEN && CURLY_BRAKET_CLOSE.equals(token) ||
							openBraket == SQUARE_BRAKET_OPEN && SQUARE_BRAKET_CLOSE.equals(token))
						return;
				}
				
				if(openBraket == SQUARE_BRAKET_OPEN) {
					name = parameterValue.getName();
					value = token;
				} else {
					int index = token.indexOf(":");
					
					if(index == -1)
						throw new InvalidParameterException(title + ": Cannot parse into name-value pair. \"" + token + "\"");
					
					name = token.substring(0, index).trim();
					value = token.substring(index + 1).trim();
					
					parameterValue = parameterValueMap.get(name);
					
					if(parameterValue == null)
						throw new InvalidParameterException(title + ": invalid parameter \"" + token + "\"");
				}
				
				if(StringUtils.hasText(value)) {
					ParameterValueType parameterValueType = parameterValue.getParameterValueType();
					
					if(CURLY_BRAKET_OPEN.equals(value)) {
						if(openBraket == SQUARE_BRAKET_OPEN) {
							AbstractParameters parameters2 = (AbstractParameters)parameterValue.getParameters(curlyBraketCount++);
							
							if(parameters2 == null)
								parameters2 = (AbstractParameters)parameterValue.getParameters();
							
							if(parameters2 == null)
								throw new InvalidParameterException("Cannot parse parameter value of '" + name + "'. parameters is null.");
							
							parameters2.valuelize(st, CURLY_BRAKET_OPEN, null);
						} else {
							AbstractParameters parameters2 = (AbstractParameters)parameterValue.getParameters();
							parameters2.valuelize(st, CURLY_BRAKET_OPEN, null);
						}
					} else if(SQUARE_BRAKET_OPEN.equals(value)) {
						valuelize(st, SQUARE_BRAKET_OPEN, parameterValue);
					} else if(parameterValueType == ParameterValueType.STRING) {
						parameterValue.setValue(value);
					} else if(parameterValueType == ParameterValueType.INTEGER) {
						try {
							parameterValue.setValue(new Integer(value));
						} catch(NumberFormatException ex) {
							throw new InvalidParameterException(title + ": Cannot parse value of '" + name + "' to an integer. \"" + token + "\"");
						}
					} else if(parameterValueType == ParameterValueType.BOOLEAN) {
						parameterValue.setValue(Boolean.valueOf(value));
					}
				}
			}
		}
		
		if(!preparsed && openBraket != null) {
			if(openBraket == CURLY_BRAKET_OPEN) {
				throw new InvalidParameterException(title + ": Cannot parse value of '" + name + "' to an array of strings.");
			} else if(openBraket == SQUARE_BRAKET_OPEN) {
				throw new InvalidParameterException(title + ": Cannot parse value of '" + name + "' to an array of strings.");
			}
		}
		
	}
	
}
