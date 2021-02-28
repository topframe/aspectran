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
package com.aspectran.core.component.bean.proxy;

import com.aspectran.core.component.bean.BeanException;
import com.aspectran.core.context.rule.BeanRule;

/**
 * Exception thrown when instantiation of a proxy bean failed.
 */
public class ProxyBeanInstantiationException extends BeanException {

    private static final long serialVersionUID = -3560168431550039638L;

    private BeanRule beanRule;

    /**
     * Create a new ProxyBeanInstantiationException.
     * @param beanRule the offending bean rule
     * @param cause the root cause
     */
    public ProxyBeanInstantiationException(BeanRule beanRule, Throwable cause) {
        super("Could not instantiate proxy bean " + beanRule, cause);
        this.beanRule = beanRule;
    }

    /**
     * Return the offending bean rule.
     * @return the bean rule
     */
    public BeanRule getBeanRule() {
        return beanRule;
    }

}
