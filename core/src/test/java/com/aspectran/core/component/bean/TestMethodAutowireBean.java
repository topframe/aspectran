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
package com.aspectran.core.component.bean;

import com.aspectran.core.component.bean.annotation.Autowired;
import com.aspectran.core.component.bean.annotation.Qualifier;

/**
 * <p>Created: 2017. 11. 29.</p>
 */
public class TestMethodAutowireBean {

    private TestFieldValueAutowireBean bean1;

    private TestFieldValueAutowireBean bean2;

    @Autowired
    @Qualifier("bean.TestFieldValueAutowireBean")
    public void setBean1(TestFieldValueAutowireBean bean) {
        this.bean1 = bean;
    }

    public TestFieldValueAutowireBean getBean1() {
        return bean1;
    }
    @Autowired(required = false)
    @Qualifier("bean.TestFieldValueAutowireBean3")
    public void setBean2(TestFieldValueAutowireBean bean) {
        this.bean2 = bean;
    }

    public TestFieldValueAutowireBean getBean2() {
        return bean2;
    }

}