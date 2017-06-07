/*
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
package com.aspectran.core.activity;

import static junit.framework.TestCase.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.aspectran.core.component.expr.token.Token;
import com.aspectran.core.component.expr.token.Tokenizer;

/**
 * <p>Created: 2016. 3. 1.</p>
 */
public class PathVariableMapTest {

    @Test
    public void testNewInstance() throws Exception {
        String transletNamePattern = "/aaa/${bbb1}/bbb2/ccc/${ddd:eee}/fff/${ggg:ggg}";
        String requestTransletName = "/aaa/bbb1/bbb2/ccc/ddd/fff/";

        List<Token> tokenList = Tokenizer.tokenize(transletNamePattern, false);
        Token[] nameTokens = tokenList.toArray(new Token[tokenList.size()]);

        Map<Token, String> map = PathVariableMap.newInstance(nameTokens, requestTransletName);

        //System.out.println();
        //System.out.println(map);

        assertNotNull(map);
    }

}