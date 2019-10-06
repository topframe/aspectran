/*
 * Copyright (c) 2008-2019 The Aspectran Project
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
package com.aspectran.core.component.bean;

import com.aspectran.core.context.ActivityContext;
import com.aspectran.core.context.rule.BeanRule;
import com.aspectran.core.context.rule.type.BeanProxifierType;
import com.aspectran.core.lang.NonNull;
import com.aspectran.core.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Collection;

/**
 * The Class ContextualBeanRegistry.
 * 
 * <p>Created: 2009. 03. 09 PM 23:48:09</p>
 */
public class ContextualBeanRegistry extends AbstractBeanRegistry {

    public ContextualBeanRegistry(ActivityContext context, BeanRuleRegistry beanRuleRegistry,
                                  BeanProxifierType beanProxifierType) {
        super(context, beanRuleRegistry, beanProxifierType);
    }

    @Override
    public <T> T getBean(@NonNull String id) {
        BeanRule beanRule = getBeanRuleRegistry().getBeanRule(id);
        if (beanRule == null) {
            throw new NoSuchBeanException(id);
        }
        return getBean(beanRule);
    }

    @Override
    public <T> T getBean(@NonNull Class<T> type) {
        return getBean(type, null);
    }

    @Override
    public <T> T getBean(@NonNull Class<T> type, @Nullable String id) {
        BeanRule[] beanRules = getBeanRuleRegistry().getBeanRules(type);
        if (beanRules == null) {
            BeanRule beanRule = getBeanRuleRegistry().getBeanRuleForConfig(type);
            if (beanRule != null) {
                return getBean(beanRule);
            } else {
                throw new NoSuchBeanException(type, id);
            }
        }
        if (beanRules.length == 1) {
            if (id != null) {
                if (id.equals(beanRules[0].getId())) {
                    return getBean(beanRules[0]);
                } else {
                    throw new NoSuchBeanException(type, id);
                }
            } else {
                return getBean(beanRules[0]);
            }
        } else {
            if (id != null) {
                for (BeanRule beanRule : beanRules) {
                    if (id.equals(beanRule.getId())) {
                        return getBean(beanRule);
                    }
                }
                throw new NoSuchBeanException(type, id);
            } else {
                throw new NoUniqueBeanException(type, beanRules);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] getBeansOfType(@NonNull Class<T> type) {
        BeanRule[] beanRules = getBeanRuleRegistry().getBeanRules(type);
        if (beanRules != null) {
            Object arr = Array.newInstance(type, beanRules.length);
            for (int i = 0; i < beanRules.length; i++) {
                Object bean = getBean(beanRules[i]);
                Array.set(arr, i, bean);
            }
            return (T[])arr;
        } else {
            return null;
        }
    }

    @Override
    public boolean containsBean(@NonNull String id) {
        return getBeanRuleRegistry().containsBeanRule(id);
    }

    @Override
    public boolean containsBean(@NonNull Class<?> type) {
        return getBeanRuleRegistry().containsBeanRule(type);
    }

    @Override
    public boolean containsBean(@NonNull Class<?> type, @Nullable String id) {
        BeanRule[] beanRules = getBeanRuleRegistry().getBeanRules(type);
        if (beanRules == null) {
            return false;
        }
        if (beanRules.length == 1) {
            if (id != null) {
                return id.equals(beanRules[0].getId());
            } else {
                return true;
            }
        } else {
            if (id != null) {
                for (BeanRule beanRule : beanRules) {
                    if (id.equals(beanRule.getId())) {
                        return true;
                    }
                }
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public Collection<Class<?>> findConfigBeanClassesWithAnnotation(Class<? extends Annotation> annotationType) {
        return getBeanRuleRegistry().findConfigBeanClassesWithAnnotation(annotationType);
    }

}