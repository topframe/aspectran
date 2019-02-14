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

import com.aspectran.core.component.template.TemplateRenderer;
import com.aspectran.core.context.ActivityContext;
import com.aspectran.core.context.builder.ActivityContextBuilder;
import com.aspectran.core.context.builder.ActivityContextBuilderException;
import com.aspectran.core.context.builder.HybridActivityContextBuilder;
import com.aspectran.core.sample.call.NumericBean;
import com.aspectran.core.sample.call.TotalBean;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cases for calling beans and templates.
 *
 * <p>Created: 2017. 3. 20.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CallTest {

    private File baseDir = new File("./target/test-classes");

    private ActivityContextBuilder activityContextBuilder;

    private ActivityContext context;

    @BeforeAll
    void ready() throws IOException, ActivityContextBuilderException {
        activityContextBuilder = new HybridActivityContextBuilder();
        activityContextBuilder.setBasePath(baseDir.getCanonicalPath());

        this.context = activityContextBuilder.build("/config/call/call-test-config.xml");
    }

    @AfterAll
    void finish() {
        if (activityContextBuilder != null) {
            activityContextBuilder.destroy();
        }
    }

    @Test
    void testBeanCall() {
        TotalBean totalBean = context.getBeanRegistry().getBean("totalBean");
        int count = 1;
        for (NumericBean o : totalBean.getNumerics()) {
            assertEquals(count++, o.getNumber());
            //System.out.println(o.getNumber() + " : " + o);
        }
    }

    @Test
    void testTemplateCall() {
        TemplateRenderer templateRenderer = context.getTemplateRenderer();
        String result1 = templateRenderer.render("template-2");

        //System.out.println("-------------------------------");
        //System.out.println(" Test cases for template calls");
        //System.out.println("-------------------------------");

        assertEquals("TEMPLATE-1", result1);
        //System.out.println(result1);

        String result2 = templateRenderer.render("template-4");

        assertEquals("TEMPLATE-3", result2);
        //System.out.println(result2);

        String result4 = templateRenderer.render("aponStyle");
        //System.out.println("=== aponStyle ===");
        //System.out.println(result4);

        String result5 = templateRenderer.render("compactStyle");
        //System.out.println("=== compactStyle ===");
        //System.out.println(result5);

        String result6 = templateRenderer.render("compressedStyle");
        //System.out.println("=== compressedStyle ===");
        //System.out.println(result6);

        String result7 = templateRenderer.render("aspectranVersion");
        //System.out.println("=== static method call ===");
        //System.out.println(result7);
    }

}