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
package com.aspectran.core.context.rule.parser.xml;

import com.aspectran.core.context.rule.DescriptionRule;
import com.aspectran.core.context.rule.EnvironmentRule;
import com.aspectran.core.context.rule.ItemRuleMap;
import com.aspectran.core.context.rule.assistant.ActivityRuleAssistant;
import com.aspectran.core.util.StringUtils;
import com.aspectran.core.util.nodelet.NodeletAdder;
import com.aspectran.core.util.nodelet.NodeletParser;

/**
 * The Class EnvironmentNodeletAdder.
 * 
 * <p>Created: 2016. 01. 09</p>
 */
class EnvironmentNodeletAdder implements NodeletAdder {

    @Override
    public void add(String xpath, NodeletParser parser) {
        AspectranNodeParser nodeParser = parser.getNodeParser();
        ActivityRuleAssistant assistant = nodeParser.getAssistant();

        parser.setXpath(xpath + "/environment");
        parser.addNodelet(attrs -> {
            String profile = attrs.get("profile");

            EnvironmentRule environmentRule = EnvironmentRule.newInstance(profile);
            parser.pushObject(environmentRule);
        });
        parser.addNodeEndlet(text -> {
            EnvironmentRule environmentRule = parser.popObject();
            assistant.addEnvironmentRule(environmentRule);
        });
        parser.setXpath(xpath + "/environment/description");
        parser.addNodelet(attrs -> {
            String profile = attrs.get("profile");
            String style = attrs.get("style");

            DescriptionRule descriptionRule = DescriptionRule.newInstance(profile, style);
            parser.pushObject(descriptionRule);
        });
        parser.addNodeEndlet(text -> {
            DescriptionRule descriptionRule = parser.popObject();
            EnvironmentRule environmentRule = parser.peekObject();

            descriptionRule.setContent(text);
            descriptionRule = assistant.profiling(descriptionRule, environmentRule.getDescriptionRule());
            environmentRule.setDescriptionRule(descriptionRule);
        });
        parser.setXpath(xpath + "/environment/properties");
        parser.addNodelet(attrs -> {
            ItemRuleMap irm = new ItemRuleMap();
            irm.setProfile(StringUtils.emptyToNull(attrs.get("profile")));
            parser.pushObject(irm);
        });
        nodeParser.addItemNodelets();
        parser.addNodeEndlet(text -> {
            ItemRuleMap irm = parser.popObject();
            EnvironmentRule environmentRule = parser.peekObject();
            environmentRule.addPropertyItemRuleMap(irm);
        });
    }

}
