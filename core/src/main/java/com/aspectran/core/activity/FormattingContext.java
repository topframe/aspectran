package com.aspectran.core.activity;

import com.aspectran.core.util.BooleanUtils;
import com.aspectran.core.util.StringUtils;
import com.aspectran.core.util.logging.Log;
import com.aspectran.core.util.logging.LogFactory;

/**
 * <p>Created: 2019-07-06</p>
 */
public class FormattingContext {

    private static final Log log = LogFactory.getLog(FormattingContext.class);

    private static final int MAX_INDENT_SIZE = 8;

    private static final String FORMAT_INDENT_TAB = "format.indentTab";

    private static final String FORMAT_INDENT_SIZE = "format.indentSize";

    private static final String FORMAT_DATE_FORMAT = "format.dateFormat";

    private static final String FORMAT_DATETIME_FORMAT = "format.dateTimeFormat";

    private static final String FORMAT_NULL_WRITABLE = "format.nullWritable";

    private boolean pretty;

    private int indentSize;

    private boolean indentTab;

    private String dateFormat;

    private String dateTimeFormat;

    private Boolean nullWritable;

    public FormattingContext() {
    }

    public boolean isPretty() {
        return pretty;
    }

    public void setPretty(boolean pretty) {
        this.pretty = pretty;
    }

    public int getIndentSize() {
        return indentSize;
    }

    public void setIndentSize(int indentSize) {
        this.indentSize = indentSize;
    }

    public void setIndentTab(boolean indentTab) {
        this.indentTab = indentTab;
    }

    public String makeIndentString() {
        if (pretty) {
            if (indentTab) {
                return "\t";
            } else if (indentSize > 0) {
                return StringUtils.repeat(' ', indentSize);
            } else {
                return StringUtils.EMPTY;
            }
        }
        return null;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public Boolean getNullWritable() {
        return nullWritable;
    }

    public void setNullWritable(Boolean nullWritable) {
        this.nullWritable = nullWritable;
    }

    public static FormattingContext parse(Activity activity) {
        String indentStyle = activity.getSetting(FORMAT_INDENT_TAB);
        String indentSize = activity.getSetting(FORMAT_INDENT_SIZE);
        String dateFormat = activity.getSetting(FORMAT_DATE_FORMAT);
        String dateTimeFormat = activity.getSetting(FORMAT_DATETIME_FORMAT);
        Boolean nullWritable = BooleanUtils.toNullableBooleanObject(activity.getSetting(FORMAT_NULL_WRITABLE));

        FormattingContext formattingContext = new FormattingContext();
        if ("tab".equalsIgnoreCase(indentStyle)) {
            formattingContext.setPretty(true);
            formattingContext.setIndentTab(true);
        } else {
            int size = parseIndentSize(indentSize);
            if (size > 0) {
                formattingContext.setPretty(true);
                formattingContext.setIndentSize(size);
            }
        }
        if (!StringUtils.isEmpty(dateFormat)) {
            formattingContext.setDateFormat(dateFormat);
        }
        if (!StringUtils.isEmpty(dateTimeFormat)) {
            formattingContext.setDateTimeFormat(dateTimeFormat);
        }
        if (nullWritable != null) {
            formattingContext.setNullWritable(nullWritable);
        }
        return formattingContext;
    }

    private static int parseIndentSize(String indentSize) {
        try {
            int size = Integer.parseInt(indentSize);
            if (size > MAX_INDENT_SIZE) {
                if (log.isDebugEnabled()) {
                    log.debug("Indent size should be less than " + MAX_INDENT_SIZE);
                }
                size = MAX_INDENT_SIZE;
            }
            return size;
        } catch (NumberFormatException e) {
            // ignore
        }
        return 0;
    }

}