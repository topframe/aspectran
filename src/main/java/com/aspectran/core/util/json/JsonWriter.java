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
package com.aspectran.core.util.json;

import java.io.Flushable;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.aspectran.core.util.BeanUtils;
import com.aspectran.core.util.apon.Parameter;
import com.aspectran.core.util.apon.ParameterValue;
import com.aspectran.core.util.apon.Parameters;

/**
 * Converts an object to a JSON formatted string.
 * If pretty-printing is enabled, includes spaces, tabs and new-lines to make the format more readable.
 * Pretty-printing is disabled by default.
 * The default indentation string is a tab character.
 * 
 * <p>Created: 2008. 06. 12 PM 8:20:54</p>
 * 
 * @author Juho Jeong
 */
public class JsonWriter implements Flushable {

	private Writer writer;

	private boolean prettyPrint;
	
	private String indentString;

	private int indentDepth;

	private boolean willWriteValue;

	/**
	 * Instantiates a new JsonWriter.
	 * Pretty-printing is disabled by default.
	 * 
	 * @param writer the character-output stream
	 */
	public JsonWriter(Writer writer) {
		this(writer, false, null);
	}

	/**
	 * Instantiates a new JsonWriter.
	 * If pretty-printing is enabled, includes spaces, tabs and new-lines to make the format more readable.
	 * The default indentation string is a tab character.
	 * 
	 * @param writer the character-output stream
	 * @param prettyPrint enables or disables pretty-printing
	 */
	public JsonWriter(Writer writer, boolean prettyPrint) {
		this(writer, prettyPrint, "\t");
	}
	
	/**
	 * Instantiates a new JsonWriter.
	 * If pretty-printing is enabled, includes spaces, tabs and new-lines to make the format more readable.
	 *
	 * @param writer the character-output stream
	 * @param prettyPrint enables or disables pretty-printing
	 * @param indentString the string that should be used for indentation when pretty-printing is enabled
	 */
	public JsonWriter(Writer writer, boolean prettyPrint, String indentString) {
		this.writer = writer;
		this.prettyPrint = prettyPrint;
		this.indentString = indentString;
	}
	
	/**
	 * Write an object to a character stream.
	 * 
	 * @param object the object to write to a character-output stream.
	 * @throws IOException an I/O error occurs.
	 * @throws InvocationTargetException the invocation target exception
	 */
	public void write(Object object) throws IOException, InvocationTargetException {
		if(object == null) {
			writeNull();
		} else if(object instanceof String ||
					object instanceof Date) {
			writeString(object.toString());
		} else if(object instanceof Boolean) {
			writeBoolean((Boolean)object);
		} else if(object instanceof Number) {
			writeNumber((Number)object);
		} else if(object instanceof Parameters) {
			openCurlyBracket();

			Map<String, ParameterValue> params = ((Parameters)object).getParameterValueMap();
			Iterator<ParameterValue> iter = params.values().iterator();
			while(iter.hasNext()) {
				Parameter p = iter.next();
				String name = p.getName();
				Object value = p.getValue();
				checkCircularReference(object, value);

				writeName(name);
				write(value);
				if(iter.hasNext()) {
					writeComma();
				}
			}
			
			closeCurlyBracket();
		} else if(object instanceof Map<?, ?>) {
			openCurlyBracket();

			@SuppressWarnings("unchecked")
			Iterator<Map.Entry<Object, Object>> iter = ((Map<Object, Object>)object).entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Object, Object> entry = iter.next();
				String name = entry.getKey().toString();
				Object value = entry.getValue();
				checkCircularReference(object, value);

				writeName(name);
				write(value);
				if(iter.hasNext()) {
					writeComma();
				}
			}

			closeCurlyBracket();
		} else if(object instanceof Collection<?>) {
			@SuppressWarnings("unchecked")
			Iterator<Object> iter = ((Collection<Object>)object).iterator();

			openSquareBracket();

			while(iter.hasNext()) {
				Object value = iter.next();
				checkCircularReference(object, value);

				write(value);
				if(iter.hasNext()) {
					writeComma();
				}
			}

			closeSquareBracket();
		} else if(object.getClass().isArray()) {
			openSquareBracket();

			int len = Array.getLength(object);
			for(int i = 0; i < len; i++) {
				Object value = Array.get(object, i);
				checkCircularReference(object, value);

				if(i > 0) {
					writeComma();
				}
				write(value);
			}

			closeSquareBracket();
		} else {
			String[] readablePropertyNames = BeanUtils.getReadablePropertyNames(object);
			if(readablePropertyNames != null && readablePropertyNames.length > 0) {
				openCurlyBracket();

				for(int i = 0; i < readablePropertyNames.length; i++) {
					Object value = BeanUtils.getObject(object, readablePropertyNames[i]);
					checkCircularReference(object, value);

					writeName(readablePropertyNames[i]);
					write(value);
					if(i < (readablePropertyNames.length - 1)) {
						writeComma();
					}
				}

				closeCurlyBracket();
			} else {
				writeString(object.toString());
			}
		}
	}

	private void checkCircularReference(Object wrapper, Object member) {
		if(wrapper.equals(member)) {
			throw new IllegalArgumentException("JSON Serialization Failure: A circular reference was detected while converting a member object [" + member + "] in [" + wrapper + "]");
		}
	}

	/**
	 * Write a tab character to a character stream.
	 * 
	 * @throws IOException an I/O error occurs
	 */
	protected void indent() throws IOException {
		if(prettyPrint) {
			for(int i = 0; i < indentDepth; i++) {
				writer.write(indentString);
			}
		}
	}

	/**
	 * Writes a key name to a character stream.
	 * 
	 * @param name the string to write to a character-output stream
	 * @throws IOException an I/O error occurs
	 */
	protected void writeName(String name) throws IOException {
		indent();

		writer.write(escape(name));
		writer.write(":");

		if(prettyPrint)
			writer.write(" ");

		willWriteValue = true;
	}

	/**
	 * Writes a string to a character stream.
	 * If {@code value} is null, write a null string ("").
	 *
	 * @param value the string to write to a character-output stream
	 * @throws IOException an I/O error occurs
	 */
	protected void writeString(String value) throws IOException {
		if(!willWriteValue)
			indent();

		writer.write(escape(value));

		willWriteValue = false;
	}

	/**
	 *  Writes a {@code Boolean} object to a character stream.
	 *
	 * @param value a {@code Boolean} object to write to a character-output stream
	 * @throws IOException an I/O error occurs
	 */
	protected void writeBoolean(Boolean value) throws IOException {
		if(!willWriteValue)
			indent();

		writer.write(value.toString());

		willWriteValue = false;
	}

	/**
	 *  Writes a {@code Number} object to a character stream.
	 *
	 * @param value a {@code Number} object to write to a character-output stream
	 * @throws IOException an I/O error occurs
	 */
	protected void writeNumber(Number value) throws IOException {
		if(!willWriteValue)
			indent();

		writer.write(value.toString());

		willWriteValue = false;
	}

	/**
	 * Write a string "null" to a character stream.
	 *
	 * @throws IOException an I/O error occurs
	 */
	protected void writeNull() throws IOException {
		writer.write("null");
	}

	/**
	 * Write a comma character to a character stream.
	 *
	 * @throws IOException an I/O error occurs
	 */
	protected void writeComma() throws IOException {
		writer.write(",");

		if(prettyPrint)
			writer.write(" ");
		
		nextLine();
	}
	
	/**
	 * Write a new line character to a character stream.
	 * 
	 * @throws IOException an I/O error occurs
	 */
	protected void nextLine() throws IOException {
		if(prettyPrint)
			writer.write("\n");
	}

	/**
	 * Open brace.
	 * 
	 * @throws IOException an I/O error occurs
	 */
	protected void openCurlyBracket() throws IOException {
		if(!willWriteValue)
			indent();

		writer.write("{");
		nextLine();

		indentDepth++;
	}

	/**
	 * Close brace.
	 * 
	 * @throws IOException an I/O error occurs
	 */
	protected void closeCurlyBracket() throws IOException {
		indentDepth--;

		nextLine();
		indent();

		writer.write("}");
	}

	/**
	 * Open square bracket.
	 * 
	 * @throws IOException an I/O error occurs
	 */
	protected void openSquareBracket() throws IOException {
		if(!willWriteValue)
			indent();

		writer.write("[");
		nextLine();
		
		indentDepth++;
		willWriteValue = false;
	}

	/**
	 * Close square bracket.
	 * 
	 * @throws IOException an I/O error occurs
	 */
	protected void closeSquareBracket() throws IOException {
		indentDepth--;

		nextLine();
		indent();

		writer.write("]");
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	/**
	 * Closes the writer.
	 *
	 * @throws IOException an I/O error occurs
	 */
	public void close() throws IOException {
		if(writer != null)
			writer.close();
		
		writer = null;
	}
	
	/**
	 * Produce a string in double quotes with backslash sequences in all the
	 * right places. A backslash will be inserted within &lt;/, allowing JSON
	 * text to be delivered in HTML. In JSON text, a string cannot contain a
	 * control character or an unescaped quote or backslash.
	 * 
	 * @param string a String
	 * @return a String correctly formatted for insertion in a JSON text
	 */
	private static String escape(String string) {
		if(string == null || string.length() == 0)
			return "\"\"";

		int len = string.length();
		StringBuilder sb = new StringBuilder(len + 4);
		char b;
		char c = 0;
		String t;

		sb.append('"');

		for(int i = 0; i < len; i++) {
			b = c;
			c = string.charAt(i);

			switch(c) {
			case '\\':
			case '"':
				sb.append('\\');
				sb.append(c);
				break;
			case '/':
				if(b == '<') {
					sb.append('\\');
				}
				sb.append(c);
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\r':
				sb.append("\\r");
				break;
			default:
				if(c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
					t = "000" + Integer.toHexString(c);
					sb.append("\\u").append(t.substring(t.length() - 4));
				} else {
					sb.append(c);
				}
			}
		}

		sb.append('"');

		return sb.toString();
	}
	
	/**
	 * Converts an object to a JSON formatted string.
	 * Pretty-printing is disabled by default.
	 *
	 * @param object an object to convert to a JSON formatted string
	 * @return the JSON formatted string
	 */
	public static String stringify(Object object) {
		return stringify(object, false, null);
	}
	
	/**
	 * Converts an object to a JSON formatted string.
	 * If pretty-printing is enabled, includes spaces, tabs and new-lines to make the format more readable.
	 * The default indentation string is a tab character.
	 *
	 * @param object an object to convert to a JSON formatted string
	 * @param prettyPrint enables or disables pretty-printing
	 * @return the JSON formatted string
	 */
	public static String stringify(Object object, boolean prettyPrint) {
		if(prettyPrint)
			return stringify(object, true, "\t");
		else
			return stringify(object, false, null);
	}
	
	/**
	 * Converts an object to a JSON formatted string.
	 * If pretty-printing is enabled, includes spaces, tabs and new-lines to make the format more readable.
	 *
	 * @param object an object to convert to a JSON formatted string
	 * @param prettyPrint enables or disables pretty-printing
	 * @param indentString the string that should be used for indentation when pretty-printing is enabled
	 * @return the JSON formatted string
	 */
	public static String stringify(Object object, boolean prettyPrint, String indentString) {
		if(object == null)
			return null;
		
		try {
			Writer writer = new StringWriter();
			
			JsonWriter jsonWriter;
			jsonWriter = new JsonWriter(writer, prettyPrint, indentString);
			jsonWriter.write(object);
			jsonWriter.close();
			
			return writer.toString();
		} catch(Exception e) {
			throw new RuntimeException("Cannot convert to a JSON formatted string.", e);
		}
	}

}
