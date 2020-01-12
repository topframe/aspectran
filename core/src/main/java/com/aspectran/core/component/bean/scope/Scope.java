/*
 * Copyright (c) 2008-2020 The Aspectran Project
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
package com.aspectran.core.component.bean.scope;

import com.aspectran.core.activity.Activity;
import com.aspectran.core.component.bean.BeanInstance;
import com.aspectran.core.component.session.NonPersistent;
import com.aspectran.core.context.rule.BeanRule;
import com.aspectran.core.context.rule.type.ScopeType;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * The Interface Scope.
 *
 * @since 2011. 3. 12.
 */
public interface Scope extends NonPersistent {

    /**
     * Returns the scope type.
     *
     * @return the scope type
     */
    ScopeType getScopeType();

    /**
     * Returns the scope lock.
     *
     * @return the scope lock
     */
    ReadWriteLock getScopeLock();

    /**
     * Returns an instance of the bean that matches the given bean rule.
     *
     * @param beanRule the bean rule of the bean to retrieve
     * @return an instance of the bean
     */
    BeanInstance getBeanInstance(BeanRule beanRule);

    /**
     * Saves an instantiated bean with the given bean rule into the scope.
     *
     * @param activity the current activity
     * @param beanRule the bean rule of the bean to save
     * @param beanInstance an instance of the bean
     */
    void putBeanInstance(Activity activity, BeanRule beanRule, BeanInstance beanInstance);

    BeanRule getBeanRule(Object bean);

    boolean containsBeanRule(BeanRule beanRule);

    void destroy(Object bean) throws Exception;

    /**
     * Destroy all scoped beans in this scope.
     */
    void destroy();

}
