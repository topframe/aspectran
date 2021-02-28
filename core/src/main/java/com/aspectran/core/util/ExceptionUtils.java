/*
 * Copyright (c) 2008-2021 The Aspectran Project
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
package com.aspectran.core.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Provides utilities for manipulating and examining Throwable objects.
 *
 * <p>Created: 2017. 10. 7.</p>
 *
 * @since 5.0.0
 */
public class ExceptionUtils {

    /**
     * Method that can be used to find the "root cause", innermost
     * of chained (wrapped) exceptions.
     * @param t the Throwable to possibly propagate
     * @return the root cause
     */
    public static Throwable getRootCause(Throwable t) {
        if (t == null) {
            return null;
        }
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t;
    }

    /**
     * Gets the stack trace from a Throwable as a String.
     *
     * <p>The result of this method vary by JDK version as this method
     * uses {@link Throwable#printStackTrace(java.io.PrintWriter)}.
     * On JDK1.3 and earlier, the cause exception will not be shown
     * unless the specified throwable alters printStackTrace.</p>
     * @param throwable the {@code Throwable} to be examined
     * @return the stack trace as generated by the exception's
     *      {@code printStackTrace(PrintWriter)} method
     */
    public static String getStacktrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString().trim();
    }

    /*
     **********************************************************
     * Exception handling; simple re-throw
     **********************************************************
     */

    /**
     * Helper method that will check if argument is an {@link Error},
     * and if so, (re)throw it; otherwise just return.
     * @param t the Throwable to possibly propagate
     * @return the Throwable
     */
    public static Throwable throwIfError(Throwable t) {
        if (t instanceof Error) {
            throw (Error)t;
        }
        return t;
    }

    /**
     * Helper method that will check if argument is an {@link RuntimeException},
     * and if so, (re)throw it; otherwise just return.
     * @param t the Throwable to possibly propagate
     * @return the Throwable
     */
    public static Throwable throwIfRTE(Throwable t) {
        if (t instanceof RuntimeException) {
            throw (RuntimeException)t;
        }
        return t;
    }

    /**
     * Helper method that will check if argument is an {@link IOException},
     * and if so, (re)throw it; otherwise just return.
     * @param t the Throwable to possibly propagate
     * @return the Throwable
     * @throws IOException rethrow the IOException
     */
    public static Throwable throwIfIOE(Throwable t) throws IOException {
        if (t instanceof IOException) {
            throw (IOException)t;
        }
        return t;
    }

    /**
     * Method that works like by calling {@link #getRootCause} and then
     * either throwing it (if instanceof {@link IOException}), or return.
     * @param t the Throwable to possibly propagate
     * @return the Throwable
     * @throws IOException rethrow the IOException
     */
    public static Throwable throwRootCauseIfIOE(Throwable t) throws IOException {
        return throwIfIOE(getRootCause(t));
    }

    /**
     * Method that will wrap 't' as an {@link IllegalArgumentException} if it
     * is a checked exception; otherwise (runtime exception or error) throw as is.
     * @param t the Throwable to possibly propagate
     */
    public static IllegalArgumentException throwAsIAE(Throwable t) {
        return throwAsIAE(t, t.getMessage());
    }

    /**
     * Method that will wrap 't' as an {@link IllegalArgumentException} (and with
     * specified message) if it is a checked exception; otherwise (runtime exception or error)
     * throw as is.
     * @param t the Throwable to possibly propagate
     * @param msg the detail message
     */
    public static IllegalArgumentException throwAsIAE(Throwable t, String msg) {
        throwIfRTE(t);
        throwIfError(t);
        throw new IllegalArgumentException(msg, t);
    }

    /**
     * Method that will locate the innermost exception for given Throwable;
     * and then wrap it as an {@link IllegalArgumentException} if it
     * is a checked exception; otherwise (runtime exception or error) throw as is.
     * @param t the Throwable to possibly propagate
     */
    public static IllegalArgumentException unwrapAndThrowAsIAE(Throwable t) {
        return throwAsIAE(getRootCause(t));
    }

    /**
     * Method that will locate the innermost exception for given Throwable;
     * and then wrap it as an {@link IllegalArgumentException} if it
     * is a checked exception; otherwise (runtime exception or error) throw as is.
     * @param t the Throwable to possibly propagate
     * @param msg the detail msg
     */
    public static IllegalArgumentException unwrapAndThrowAsIAE(Throwable t, String msg) {
        throw throwAsIAE(getRootCause(t), msg);
    }

}
