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
package com.aspectran.shell.command.option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes a single command-line option.  It maintains
 * information regarding the short-name of the option, the long-name,
 * if any exists, a flag indicating if an argument is required for
 * this option, and a self-documenting description of the option.
 *
 * <p>An Option is not created independently, but is created through
 * an instance of {@link Options}. An Option is required to have
 * at least a short or a long-name.</p>
 *
 * <p><strong>Note:</strong> once an {@link Option} has been added to an instance
 * of {@link Options}, it's required flag may not be changed anymore.</p>
 *
 * @see Options
 * @see ParsedOptions
 */
public class Option implements Cloneable, Serializable {

    /** @serial */
    private static final long serialVersionUID = -7707766888283034409L;

    /** Constant that specifies the number of argument values has not been specified */
    public static final int UNINITIALIZED = -1;

    /** Constant that specifies the number of argument values is infinite */
    public static final int UNLIMITED_VALUES = -2;

    /** The name of the option */
    private final String opt;

    /** The long representation of the option */
    private String longOpt;

    /** The name of the argument for this option */
    private String argName;

    /** Description of the option */
    private String description;

    /** Specifies whether this option is required to be present */
    private boolean required;

    /** Specifies whether the argument value of this Option is optional */
    private boolean optionalArg;

    /** The number of argument values this option can have */
    private int numberOfArgs = UNINITIALIZED;

    /** The type of this Option */
    private OptionValueType valueType = OptionValueType.STRING;

    /** The list of argument values **/
    private List<String> values = new ArrayList<>();

    /** The character that is the value separator */
    private char valueSeparator;

    /**
     * Private constructor used by the nested Builder class.
     * 
     * @param builder builder used to create this option
     */
    private Option(Builder builder) {
        this.opt = builder.opt;
        this.longOpt = builder.longOpt;
        this.argName = builder.argName;
        this.description = builder.description;
        this.required = builder.required;
        this.optionalArg = builder.optionalArg;
        this.numberOfArgs = builder.numberOfArgs;
        this.valueType = builder.valueType;
        this.valueSeparator = builder.valueSeparator;
    }
    
    /**
     * Creates an Option using the specified parameters.
     * The option does not take an argument.
     *
     * @param opt short representation of the option
     * @param description describes the function of the option
     * @throws IllegalArgumentException if there are any non valid
     *      Option characters in {@code opt}
     */
    public Option(String opt, String description) throws IllegalArgumentException {
        this(opt, null, false, description);
    }

    /**
     * Creates an Option using the specified parameters.
     *
     * @param opt short representation of the option
     * @param hasArg specifies whether the Option takes an argument or not
     * @param description describes the function of the option
     * @throws IllegalArgumentException if there are any non valid
     *      Option characters in {@code opt}
     */
    public Option(String opt, boolean hasArg, String description) throws IllegalArgumentException {
        this(opt, null, hasArg, description);
    }

    /**
     * Creates an Option using the specified parameters.
     *
     * @param opt short representation of the option
     * @param longOpt the long representation of the option
     * @param hasArg specifies whether the Option takes an argument or not
     * @param description describes the function of the option
     * @throws IllegalArgumentException if there are any non valid
     *      Option characters in {@code opt}
     */
    public Option(String opt, String longOpt, boolean hasArg, String description)
           throws IllegalArgumentException {
        // ensure that the option is valid
        OptionUtils.validateOption(opt);

        this.opt = opt;
        this.longOpt = longOpt;

        // if hasArg is set then the number of arguments is 1
        if (hasArg) {
            this.numberOfArgs = 1;
        }

        this.description = description;
    }

    /**
     * Returns the id of this Option.  This is only set when the
     * Option shortOpt is a single character.  This is used for switch
     * statements.
     *
     * @return the id of this Option
     */
    public int getId() {
        return getKey().charAt(0);
    }

    /**
     * Returns the 'unique' Option identifier.
     * 
     * @return the 'unique' Option identifier
     */
    public String getKey() {
        // if 'opt' is null, then it is a 'long' option
        return (opt == null ? longOpt : opt);
    }

    /** 
     * Retrieve the name of this Option.
     *
     * It is this String which can be used with
     * {@link ParsedOptions#hasOption(String opt)} and
     * {@link ParsedOptions#getValue(String opt)} to check
     * for existence and argument.
     *
     * @return the name of this option
     */
    public String getOpt() {
        return opt;
    }

    /**
     * Retrieve the type of this Option.
     * 
     * @return the type of this option
     */
    public OptionValueType getValueType() {
        return valueType;
    }

    /**
     * Sets the type of this Option.
     *
     * @param valueType the type of this Option
     */
    public void setValueType(OptionValueType valueType) {
        this.valueType = valueType;
    }

    /** 
     * Retrieve the long name of this Option.
     *
     * @return the long name of this Option, or null, if there is no long name
     */
    public String getLongOpt() {
        return longOpt;
    }

    /**
     * Sets the long name of this Option.
     *
     * @param longOpt the long name of this Option
     */
    public void setLongOpt(String longOpt) {
        this.longOpt = longOpt;
    }

    /**
     * Sets whether this Option can have an optional argument.
     *
     * @param optionalArg specifies whether the Option can have
     *      an optional argument.
     */
    public void setOptionalArg(boolean optionalArg) {
        this.optionalArg = optionalArg;
    }

    /**
     * Returns whether this Option can have an optional argument.
     *
     * @return whether this Option can have an optional argument
     */
    public boolean hasOptionalArg() {
        return optionalArg;
    }

    /** 
     * Query to see if this Option has a long name.
     *
     * @return boolean flag indicating existence of a long name
     */
    public boolean hasLongOpt() {
        return longOpt != null;
    }

    /**
     * Query to see if this Option requires an argument.
     *
     * @return boolean flag indicating if an argument is required
     */
    public boolean hasArg() {
        return (numberOfArgs > 0 || numberOfArgs == UNLIMITED_VALUES);
    }

    /** 
     * Retrieve the self-documenting description of this Option
     *
     * @return the string description of this option
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the self-documenting description of this Option
     *
     * @param description the description of this option
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     * Query to see if this Option is mandatory
     *
     * @return boolean flag indicating whether this Option is mandatory
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets whether this Option is mandatory.
     *
     * @param required specifies whether this Option is mandatory
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Sets the display name for the argument value.
     *
     * @param argName the display name for the argument value
     */
    public void setArgName(String argName) {
        this.argName = argName;
    }

    /**
     * Gets the display name for the argument value.
     *
     * @return the display name for the argument value
     */
    public String getArgName() {
        return argName;
    }

    /**
     * Returns whether the display name for the argument value has been set.
     *
     * @return if the display name for the argument value has been set
     */
    public boolean hasArgName() {
        return argName != null && argName.length() > 0;
    }

    /** 
     * Query to see if this Option can take many values.
     *
     * @return boolean flag indicating if multiple values are allowed
     */
    public boolean hasArgs() {
        return (numberOfArgs > 1 || numberOfArgs == UNLIMITED_VALUES);
    }

    /** 
     * Sets the number of argument values this Option can take.
     *
     * @param num the number of argument values
     */
    public void setArgs(int num) {
        this.numberOfArgs = num;
    }

    /**
     * Sets the value separator.  For example if the argument value
     * was a Java property, the value separator would be '='.
     *
     * @param valueSeparator the value separator
     */
    public void setValueSeparator(char valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    /**
     * Returns the value separator character.
     *
     * @return the value separator character
     */
    public char getValueSeparator() {
        return valueSeparator;
    }

    /**
     * Return whether this Option has specified a value separator.
     * 
     * @return whether this Option has specified a value separator
     */
    public boolean hasValueSeparator() {
        return (valueSeparator > 0);
    }

    /** 
     * Returns the number of argument values this Option can take.
     * 
     * <p>
     * A value equal to the constant {@link #UNINITIALIZED} (= -1) indicates
     * the number of arguments has not been specified.
     * A value equal to the constant {@link #UNLIMITED_VALUES} (= -2) indicates
     * that this options takes an unlimited amount of values.
     * </p>
     *
     * @return num the number of argument values
     * @see #UNINITIALIZED
     * @see #UNLIMITED_VALUES
     */
    public int getArgs() {
        return numberOfArgs;
    }

    /**
     * Adds the specified value to this Option.
     * 
     * @param value is a/the value of this Option
     */
    public void addValue(String value) {
        if (numberOfArgs == UNINITIALIZED) {
            throw new RuntimeException("NO_ARGS_ALLOWED");
        }
        add(value);
    }

    /**
     * Add the value to this Option.  If the number of arguments
     * is greater than zero and there is enough space in the list then
     * add the value.  Otherwise, throw a runtime exception.
     *
     * @param value the value to be added to this Option
     */
    private void add(String value) {
        if (!acceptsArg()) {
            throw new RuntimeException("Cannot add value, list full");
        }

        // store value
        values.add(value);
    }

    /**
     * Returns the specified value of this Option or 
     * {@code null} if there is no value.
     *
     * @return the value/first value of this Option or 
     *      {@code null} if there is no value
     */
    public String getValue() {
        return (hasNoValues() ? null : values.get(0));
    }

    /**
     * Returns the specified value of this Option or 
     * {@code null} if there is no value.
     *
     * @param index the index of the value to be returned.
     * @return the specified value of this Option or
     *      {@code null} if there is no value.
     * @throws IndexOutOfBoundsException if index is less than 1
     *      or greater than the number of the values for this Option
     */
    public String getValue(int index) throws IndexOutOfBoundsException {
        return (hasNoValues() ? null : values.get(index));
    }

    /**
     * Returns the value/first value of this Option or the 
     * <code>defaultValue</code> if there is no value.
     *
     * @param defaultValue the value to be returned if there
     *      is no value.
     * @return the value/first value of this Option or the
     *      <code>defaultValue</code> if there are no values
     */
    public String getValue(String defaultValue) {
        String value = getValue();
        return (value != null ? value : defaultValue);
    }

    /**
     * Return the values of this Option as a String array 
     * or null if there are no values.
     *
     * @return the values of this Option as a String array 
     *      or null if there are no values
     */
    public String[] getValues() {
        return (hasNoValues() ? null : values.toArray(new String[0]));
    }

    /**
     * Returns the values of this Option as a List
     * or null if there are no values.
     *
     * @return the values of this Option as a List
     *      or null if there are no values
     */
    public List<String> getValuesList() {
        return values;
    }

    /** 
     * Dump state, suitable for debugging.
     *
     * @return the stringified form of this object
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder().append("[ option: ");
        buf.append(opt);
        if (longOpt != null) {
            buf.append(" ").append(longOpt);
        }
        buf.append(" ");
        if (hasArgs()) {
            buf.append("[ARG...]");
        } else if (hasArg()) {
            buf.append(" [ARG]");
        }
        buf.append(" :: ").append(description);
        if (valueType != null) {
            buf.append(" :: ").append(valueType);
        }
        buf.append(" ]");
        return buf.toString();
    }

    /**
     * Returns whether this Option has any values.
     *
     * @return true if this Option has no value; false otherwise
     */
    private boolean hasNoValues() {
        return values.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Option option = (Option)o;
        if (opt != null ? !opt.equals(option.opt) : option.opt != null) {
            return false;
        }
        if (longOpt != null ? !longOpt.equals(option.longOpt) : option.longOpt != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (opt != null ? opt.hashCode() : 0);
        result = 31 * result + (longOpt != null ? longOpt.hashCode() : 0);
        return result;
    }

    /**
     * A rather odd clone method - due to incorrect code in 1.0 it is public 
     * and in 1.1 rather than throwing a CloneNotSupportedException it throws 
     * a RuntimeException so as to maintain backwards compat at the API level. 
     *
     * After calling this method, it is very likely you will want to call 
     * clearValues(). 
     *
     * @return a clone of this Option instance
     * @throws RuntimeException if a {@link CloneNotSupportedException} has been thrown
     *      by {@code super.clone()}
     */
    @Override
    public Option clone() {
        try {
            Option option = (Option)super.clone();
            option.values = new ArrayList<>(values);
            return option;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("A CloneNotSupportedException was thrown: " + e.getMessage());
        }
    }

    /**
     * Clear the Option values. After a parse is complete, these are left with
     * data in them and they need clearing if another parse is done.
     *
     * See: <a href="https://issues.apache.org/jira/browse/CLI-71">CLI-71</a>
     */
    void clearValues() {
        values.clear();
    }

    /**
     * Tells if the option can accept more arguments.
     * 
     * @return false if the maximum number of arguments is reached
     */
    boolean acceptsArg() {
        return (hasArg() || hasArgs() || hasOptionalArg()) && (numberOfArgs <= 0 || values.size() < numberOfArgs);
    }

    /**
     * Tells if the option requires more arguments to be valid.
     * 
     * @return false if the option doesn't require more arguments
     */
    boolean requiresArg() {
        if (optionalArg) {
            return false;
        }
        if (numberOfArgs == UNLIMITED_VALUES) {
            return values.isEmpty();
        }
        return acceptsArg();
    }
    
    /**
     * Returns a {@link Builder} to create an {@link Option} using descriptive
     * methods.  
     * 
     * @return a new {@link Builder} instance
     */
    public static Builder builder() {
        return builder(null);
    }
    
    /**
     * Returns a {@link Builder} to create an {@link Option} using descriptive
     * methods.  
     *
     * @param opt short representation of the option
     * @return a new {@link Builder} instance
     * @throws IllegalArgumentException if there are any non valid Option characters in {@code opt}
     */
    public static Builder builder(String opt) {
        return new Builder(opt);
    }
    
    /**
     * A nested builder class to create <code>Option</code> instances
     * using descriptive methods.
     * <p>
     * Example usage:
     * <pre>
     * Option option = Option.builder("a")
     *     .required(true)
     *     .longOpt("arg-name")
     *     .build();
     * </pre>
     */
    public static final class Builder {

        /** The name of the option */
        private final String opt;

        /** Description of the option */
        private String description;

        /** The long representation of the option */
        private String longOpt;

        /** The name of the argument for this option */
        private String argName;

        /** Specifies whether this option is required to be present */
        private boolean required;

        /** Specifies whether the argument value of this Option is optional */
        private boolean optionalArg;

        /** The number of argument values this option can have */
        private int numberOfArgs = UNINITIALIZED;

        /** The type of this Option */
        private OptionValueType valueType = OptionValueType.STRING;

        /** The character that is the value separator */
        private char valueSeparator;

        /**
         * Constructs a new {@code Builder} with the minimum
         * required parameters for an {@code Option} instance.
         * 
         * @param opt short representation of the option
         * @throws IllegalArgumentException if there are any non valid Option characters in {@code opt}
         */
        private Builder(String opt) throws IllegalArgumentException {
            OptionUtils.validateOption(opt);
            this.opt = opt;
        }
        
        /**
         * Sets the display name for the argument value.
         *
         * @param argName the display name for the argument value
         * @return this builder, to allow method chaining
         */
        public Builder argName(String argName) {
            this.argName = argName;
            return this;
        }

        /**
         * Sets the description for this option.
         *
         * @param description the description of the option
         * @return this builder, to allow method chaining
         */
        public Builder desc(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the long name of the Option.
         *
         * @param longOpt the long name of the Option
         * @return this builder, to allow method chaining
         */        
        public Builder longOpt(String longOpt) {
            this.longOpt = longOpt;
            return this;
        }
        
        /** 
         * Sets the number of argument values the Option can take.
         *
         * @param numberOfArgs the number of argument values
         * @return this builder, to allow method chaining
         */        
        public Builder numberOfArgs(int numberOfArgs) {
            this.numberOfArgs = numberOfArgs;
            return this;
        }
        
        /**
         * Sets whether the Option can have an optional argument.
         *
         * @param isOptional specifies whether the Option can have
         *      an optional argument
         * @return this builder, to allow method chaining
         */
        public Builder optionalArg(boolean isOptional) {
            this.optionalArg = isOptional;
            return this;
        }
        
        /**
         * Marks this Option as required.
         *
         * @return this builder, to allow method chaining
         */
        public Builder required() {
            return required(true);
        }

        /**
         * Sets whether the Option is mandatory.
         *
         * @param required specifies whether the Option is mandatory
         * @return this builder, to allow method chaining
         */
        public Builder required(boolean required) {
            this.required = required;
            return this;
        }
        
        /**
         * Sets the type of the Option.
         *
         * @param valueType the type of the Option
         * @return this builder, to allow method chaining
         */
        public Builder valueType(OptionValueType valueType) {
            this.valueType = valueType;
            return this;
        }

        /**
         * The Option will use '=' as a means to separate argument value.
         *
         * @return this builder, to allow method chaining
         */
        public Builder valueSeparator() {
            return valueSeparator('=');
        }

        /**
         * The Option will use <code>sep</code> as a means to
         * separate argument values.
         * <p>
         * <b>Example:</b>
         * <pre>
         * Option opt = Option.builder("D").hasArgs()
         *                                 .valueSeparator('=')
         *                                 .build();
         * Options options = new Options();
         * options.addOption(opt);
         * String[] args = {"-Dkey=value"};
         * CommandLineParser parser = new DefaultParser();
         * CommandLine line = parser.parse(options, args);
         * String propertyName = line.getOptionValues("D")[0];  // will be "key"
         * String propertyValue = line.getOptionValues("D")[1]; // will be "value"
         * </pre>
         *
         * @param valueSeparator the value separator.
         * @return this builder, to allow method chaining
         */
        public Builder valueSeparator(char valueSeparator) {
            this.valueSeparator = valueSeparator;
            hasArgs();
            return this;
        }
        
        /**
         * Indicates that the Option will require an argument.
         * 
         * @return this builder, to allow method chaining
         */
        public Builder hasArg() {
            return hasArg(true);
        }

        /**
         * Indicates if the Option has an argument or not.
         * 
         * @param hasArg specifies whether the Option takes an argument or not
         * @return this builder, to allow method chaining
         */
        public Builder hasArg(boolean hasArg) {
            // set to UNINITIALIZED when no arg is specified to be compatible with OptionBuilder
            numberOfArgs = (hasArg ? 1 : Option.UNINITIALIZED);
            return this;
        }

        /**
         * Indicates that the Option can have unlimited argument values.
         * 
         * @return this builder, to allow method chaining
         */
        public Builder hasArgs() {
            numberOfArgs = Option.UNLIMITED_VALUES;
            return this;
        }

        /**
         * Constructs an Option with the values declared by this {@link Builder}.
         * 
         * @return the new {@link Option}
         * @throws IllegalArgumentException if neither {@code opt} or {@code longOpt} has been set
         */
        public Option build() {
            if (opt == null && longOpt == null) {
                throw new IllegalArgumentException("Either opt or longOpt must be specified");
            }
            return new Option(this);
        }
    }

}
