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
package com.aspectran.core.context.expr;

import com.aspectran.core.activity.Activity;
import com.aspectran.core.activity.NonActivity;
import com.aspectran.core.context.ActivityContext;
import com.aspectran.core.context.builder.ActivityContextBuilder;
import com.aspectran.core.context.builder.ActivityContextBuilderException;
import com.aspectran.core.context.builder.HybridActivityContextBuilder;
import com.aspectran.core.context.rule.IllegalRuleException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>Created: 2021/02/04</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExpressionEvaluationTest {

    private Activity activity;

    @BeforeAll
    void setUp() throws ActivityContextBuilderException {
        ActivityContextBuilder builder = new HybridActivityContextBuilder();
        ActivityContext context = builder.build();

        NonActivity activity = new NonActivity(context);
        activity.getRequestAdapter().setParameter("foo", "foo");
        activity.getRequestAdapter().setParameter("bars", new String[] {"bar1", "bar2", "bar3"});
        this.activity = activity;
    }

    @Test
    void evaluate() throws IllegalRuleException {
        assertEquals(true, ExpressionEvaluator.evaluate("foo in {'foo','bar'}", activity));
        assertEquals(true, ExpressionEvaluator.evaluate("${foo} in {'foo','bar'}", activity));
        assertEquals(3, (int)ExpressionEvaluator.evaluate("bars.length", activity));
        assertEquals(3, (int)ExpressionEvaluator.evaluate("${bars}.length", activity));
        assertEquals("bar2", ExpressionEvaluator.evaluate("bars[1]", activity));
        assertEquals("bar2", ExpressionEvaluator.evaluate("${bars}[1]", activity));
        assertEquals("T", ExpressionEvaluator.evaluate("${bars}.length > 3 ? \"F\" : \"T\"", activity));
        assertEquals("foofoo", ExpressionEvaluator.evaluate("${foo} + ${foo}", activity));
        assertEquals("[1]", ExpressionEvaluator.evaluate("'${bars--}[1]'", activity));
        //System.out.println(ExpressionEvaluator.evaluate("${foo} + ${foo}", activity));
        //System.out.println(ExpressionEvaluator.evaluate("'${bars--}[1]'", activity));
    }

}
