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
package com.aspectran.core.context.template.engine.pebble;

import com.aspectran.core.context.bean.ablility.FactoryBean;
import com.aspectran.core.context.bean.ablility.InitializableBean;
import com.mitchellbosecke.pebble.PebbleEngine;

/**
 * JavaBean to configure Pebble Engine.
 *
 * <p>Created: 2016. 1. 25.</p>
 */
public class PebbleEngineFactoryBean extends PebbleEngineFactory
        implements InitializableBean, FactoryBean<PebbleEngine> {

    private PebbleEngine pebbleEngine;

    /**
     * Initialize FreeMarkerConfigurationFactory's Configuration
     * if not overridden by a preconfigured FreeMarker Configuation.
     *
     * @throws Exception if an exception occurs during initialization.
     */
    @Override
    public void initialize() throws Exception {
        if (this.pebbleEngine == null) {
            this.pebbleEngine = createPebbleEngine();
        }
    }

    @Override
    public PebbleEngine getObject() {
        return this.pebbleEngine;
    }

}
