/*
 * Copyright (c) 2008-2017 The Aspectran Project
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
package com.aspectran.core.context.rule.type;

/**
 * Supported Bean referer types.
 * 
 * <p>Created: 2016. 2. 20.</p>
 */
public enum BeanRefererType {

    ASPECT_RULE("aspectRule"),
    SCHEDULE_RULE("scheduleRule"),
    BEAN_ACTION_RULE("beanActionRule"),
    BEAN_RULE("beanRule"),
    AUTOWIRE_RULE("autowireRule"),
    TOKEN("token"),
    TEMPLATE_RULE("templateRule");

    private final String alias;

    BeanRefererType(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return this.alias;
    }

    /**
     * Returns a {@code BeanReferrerType} with a value represented
     * by the specified {@code String}.
     *
     * @param alias the bean referrer type as a {@code String}
     * @return a {@code BeanReferrerType}, may be {@code null}
     */
    public static BeanRefererType resolve(String alias) {
        for (BeanRefererType type : values()) {
            if (type.alias.equals(alias)) {
                return type;
            }
        }
        return null;
    }

}
