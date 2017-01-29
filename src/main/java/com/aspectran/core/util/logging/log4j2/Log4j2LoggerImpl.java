/**
 * Copyright 2008-2017 Juho Jeong
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
package com.aspectran.core.util.logging.log4j2;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import com.aspectran.core.util.logging.Log;
import com.aspectran.core.util.logging.LogFactory;

/**
 * @author Eduardo Macarron
 */
public class Log4j2LoggerImpl implements Log {

	private static Marker MARKER = MarkerManager.getMarker(LogFactory.MARKER);

	private Logger log;

	public Log4j2LoggerImpl(Logger logger) {
		log = logger;
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	@Override
	public void error(String s, Throwable e) {
		log.error(MARKER, s, e);
	}

	@Override
	public void error(String s) {
		log.error(MARKER, s);
	}

	@Override
	public void debug(String s) {
		log.debug(MARKER, s);
	}

	@Override
	public void info(String s) {
		log.info(MARKER, s);
	}

	@Override
	public void trace(String s) {
		log.trace(MARKER, s);
	}

	@Override
	public void warn(String s) {
		log.warn(MARKER, s);
	}

	@Override
	public void warn(String s, Throwable e) {
		log.warn(MARKER, s, e);
	}

}
