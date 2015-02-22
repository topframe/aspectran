/*
 *  Copyright (c) 2008 Jeong Ju Ho, All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aspectran.core.context.builder;

import java.io.IOException;
import java.io.InputStream;

import com.aspectran.core.context.rule.type.ImportFileType;
import com.aspectran.core.context.rule.type.ImportType;

/**
 * <p>Created: 2008. 04. 24 오전 11:23:36</p>
 * 
 * @author Gulendol
 */
public class ImportableResource extends Importable {
	
	private final static ImportType RESOURCE_IMPORT = ImportType.RESOURCE;
	
	private ClassLoader classLoader;

	private String resource;

	public ImportableResource(ClassLoader classLoader, String resource, ImportFileType importFileType) {
		super(RESOURCE_IMPORT);

		if(importFileType == null)
			importFileType = resource.endsWith(".apon") ? ImportFileType.APON : ImportFileType.XML;
		
		setImportFileType(importFileType);
		
		this.classLoader = classLoader;
		setLastModified(System.currentTimeMillis());
	}
	
	/**
	 * Gets the input stream.
	 * 
	 * @return the input stream
	 */
	public InputStream getInputStream() throws IOException {
		InputStream inputStream = classLoader.getResourceAsStream(resource);
		
		if(inputStream == null)
			throw new IOException("Could not find resource to import. resource: " + resource);
		
		return inputStream;
	}

}
