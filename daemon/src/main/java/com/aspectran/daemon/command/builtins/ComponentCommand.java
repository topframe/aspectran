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
package com.aspectran.daemon.command.builtins;

import com.aspectran.core.activity.request.ParameterMap;
import com.aspectran.core.component.aspect.AspectRuleRegistry;
import com.aspectran.core.component.schedule.ScheduleRuleRegistry;
import com.aspectran.core.component.translet.TransletRuleRegistry;
import com.aspectran.core.context.expr.ItemEvaluator;
import com.aspectran.core.context.expr.ItemExpression;
import com.aspectran.core.context.rule.AspectRule;
import com.aspectran.core.context.rule.ItemRuleMap;
import com.aspectran.core.context.rule.ScheduleRule;
import com.aspectran.core.context.rule.ScheduledJobRule;
import com.aspectran.core.context.rule.TransletRule;
import com.aspectran.core.context.rule.converter.RuleToParamsConverter;
import com.aspectran.core.context.rule.params.ScheduleParameters;
import com.aspectran.core.context.rule.params.SchedulerParameters;
import com.aspectran.core.context.rule.params.TriggerParameters;
import com.aspectran.core.context.rule.type.MethodType;
import com.aspectran.core.util.StringUtils;
import com.aspectran.core.util.apon.AponFormat;
import com.aspectran.core.util.apon.AponWriter;
import com.aspectran.core.util.apon.Parameters;
import com.aspectran.daemon.command.AbstractCommand;
import com.aspectran.daemon.command.CommandRegistry;
import com.aspectran.daemon.command.CommandResult;
import com.aspectran.daemon.command.polling.CommandParameters;
import com.aspectran.daemon.service.DaemonService;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Formatter;
import java.util.LinkedHashSet;
import java.util.Set;

public class ComponentCommand extends AbstractCommand {

    private static final String NAMESPACE = "builtins";

    private static final String COMMAND_NAME = "component";

    private final CommandDescriptor descriptor = new CommandDescriptor();

    public ComponentCommand(CommandRegistry registry) {
        super(registry);
    }

    @Override
    public CommandResult execute(CommandParameters parameters) {
        DaemonService service = getService();

        try {
            ItemEvaluator evaluator = new ItemExpression(getService().getActivityContext());

            ParameterMap parameterMap = null;
            ItemRuleMap parameterItemRuleMap = parameters.getParameterItemRuleMap();
            if (parameterItemRuleMap != null && !parameterItemRuleMap.isEmpty()) {
                parameterMap = evaluator.evaluateAsParameterMap(parameterItemRuleMap);
            }

            if (parameterMap == null) {
                return failed(error("There are no parameters specified"));
            }

            String type = parameterMap.getParameter("type");
            String mode = parameterMap.getParameter("mode");
            String[] targets = parameterMap.getParameterValues("targets");

            if (!StringUtils.hasLength(type)) {
                return failed(error("The component type is not specified"));
            }

            switch (type) {
                case "aspect":
                    switch (mode) {
                        case "list":
                            return listAspects(service, targets);
                        case "detail":
                            return detailAspectRule(service, targets);
                        case "enable":
                            return changeAspectActiveState(service, targets, false);
                        case "disable":
                            return changeAspectActiveState(service, targets, true);
                    }
                    break;
                case "translet": {
                    switch (mode) {
                        case "list":
                            return listTranslets(service, targets);
                        case "detail":
                            return detailTransletRule(service, targets);
                    }
                    break;
                }
                case "job": {
                    switch (mode) {
                        case "list":
                            return listScheduledJobs(service, targets);
                        case "detail":
                            return detailScheduledJobRule(service, targets);
                        case "enable":
                            return changeJobActiveState(service, targets, false);
                        case "disable":
                            return changeJobActiveState(service, targets, true);
                    }
                    break;
                }
                default:
                    return failed(error("Unknown component type: " + type));
            }
            return failed(error("Unknown mode: " + type));
        } catch (Exception e) {
            return failed(e);
        }
    }

    private CommandResult listAspects(DaemonService service, String[] keywords) {
        AspectRuleRegistry aspectRuleRegistry = service.getActivityContext().getAspectRuleRegistry();
        Collection<AspectRule> aspectRules = aspectRuleRegistry.getAspectRules();
        Formatter formatter = new Formatter();
        formatter.format("-%4s-+-%-45s-+-%-8s-+-%-8s-%n", "----", "---------------------------------------------",
                "--------", "--------");
        formatter.format(" %4s | %-45s | %-8s | %-8s %n", "No.", "Aspect ID", "Isolated", "Enabled");
        formatter.format("-%4s-+-%-45s-+-%-8s-+-%-8s-%n", "----", "---------------------------------------------",
                "--------", "--------");
        int num = 0;
        for (AspectRule aspectRule : aspectRules) {
            if (keywords != null) {
                boolean exists = false;
                for (String keyw : keywords) {
                    if (aspectRule.getId().toLowerCase().contains(keyw.toLowerCase())) {
                        exists = true;
                    }
                }
                if (!exists) {
                    continue;
                }
            }
            formatter.format("%5d | %-45s | %-8s | %-8s %n", ++num, aspectRule.getId(), aspectRule.isIsolated(),
                    !aspectRule.isDisabled());
        }
        if (num == 0) {
            formatter.format("%33s %s%n", " ", "No Data");
        }
        formatter.format("-%4s-+-%-45s-+-%-8s-+-%-8s-", "----", "---------------------------------------------",
                "--------", "--------");
        return success(formatter.toString());
    }

    private CommandResult detailAspectRule(DaemonService service, String[] targets) throws IOException {
        AspectRuleRegistry aspectRuleRegistry = service.getActivityContext().getAspectRuleRegistry();
        Collection<AspectRule> aspectRules;
        if (targets == null || targets.length == 0) {
            aspectRules = aspectRuleRegistry.getAspectRules();
        } else {
            aspectRules = new LinkedHashSet<>();
            for (String aspectId : targets) {
                AspectRule aspectRule = aspectRuleRegistry.getAspectRule(aspectId);
                if (aspectRule == null) {
                    return failed(error("Unknown aspect: " + aspectId));
                }
                aspectRules.add(aspectRule);
            }
        }
        int count = 0;
        StringWriter writer = new StringWriter();
        for (AspectRule aspectRule : aspectRules) {
            Parameters aspectParameters = RuleToParamsConverter.toAspectParameters(aspectRule);

            if (count > 0) {
                writer.write("----------------------------------------------------------------------------");
                writer.write(AponFormat.NEW_LINE);
            }

            AponWriter aponWriter = new AponWriter(writer, true);
            aponWriter.setIndentString("  ");
            aponWriter.write(aspectParameters);
            count++;
        }
        if (count == 0) {
            return success("No aspects");
        } else {
            return success(writer.toString().trim());
        }
    }

    private CommandResult changeAspectActiveState(DaemonService service, String[] targets, boolean disabled) {
        if (targets == null || targets.length == 0) {
            return failed(error("Please specify aspects to be enabled or disabled"));
        }
        AspectRuleRegistry aspectRuleRegistry = service.getActivityContext().getAspectRuleRegistry();
        Set<AspectRule> aspectRules = new LinkedHashSet<>();
        for (String aspectId : targets) {
            AspectRule aspectRule = aspectRuleRegistry.getAspectRule(aspectId);
            if (aspectRule == null) {
                return failed(error("Unknown aspect: " + aspectId));
            }
            if (aspectRule.isIsolated()) {
                return failed(error("Can not be disabled or enabled for isolated Aspect '" + aspectId + "'"));
            }
            aspectRules.add(aspectRule);
        }
        Formatter formatter = new Formatter();
        for (AspectRule aspectRule : aspectRules) {
            if (disabled) {
                if (aspectRule.isDisabled()) {
                    formatter.format("Aspect '%s' is already inactive%n", aspectRule.getId());
                } else {
                    aspectRule.setDisabled(true);
                    formatter.format("Aspect '%s' is now inactive%n", aspectRule.getId());
                }
            } else {
                if (!aspectRule.isDisabled()) {
                    formatter.format("Aspect '%s' is already active%n", aspectRule.getId());
                } else {
                    aspectRule.setDisabled(false);
                    formatter.format("Aspect '%s' is now active%n", aspectRule.getId());
                }
            }
        }
        return success(formatter.toString());
    }

    private CommandResult listTranslets(DaemonService service, String[] keywords) {
        TransletRuleRegistry transletRuleRegistry = service.getActivityContext().getTransletRuleRegistry();
        Collection<TransletRule> transletRules = transletRuleRegistry.getTransletRules();
        Formatter formatter = new Formatter();
        formatter.format("-%4s-+-%-67s-%n", "----", "-------------------------------------------------------------------");
        formatter.format(" %4s | %-67s %n", "No.", "Translet Name");
        formatter.format("-%4s-+-%-67s-%n", "----", "-------------------------------------------------------------------");
        int num = 0;
        for (TransletRule transletRule : transletRules) {
            String transletName = transletRule.getName();
            if (keywords != null) {
                boolean exists = false;
                for (String keyw : keywords) {
                    if (transletName.toLowerCase().contains(keyw.toLowerCase())) {
                        exists = true;
                    }
                }
                if (!exists) {
                    continue;
                }
            }
            MethodType[] requestMethods = transletRule.getAllowedMethods();
            if (requestMethods != null) {
                transletName = StringUtils.toDelimitedString(requestMethods, ",") + " " + transletName;
            }
            formatter.format("%5d | %s%n", ++num, transletName);
        }
        if (num == 0) {
            formatter.format("%33s %s%n", " ", "No Data");
        }
        formatter.format("-%4s-+-%-67s-", "----", "-------------------------------------------------------------------");
        return success(formatter.toString());
    }

    private CommandResult detailTransletRule(DaemonService service, String[] targets) throws IOException {
        TransletRuleRegistry transletRuleRegistry = service.getActivityContext().getTransletRuleRegistry();
        Collection<TransletRule> transletRules;
        if (targets == null || targets.length == 0) {
            transletRules = transletRuleRegistry.getTransletRules();
        } else {
            transletRules = new LinkedHashSet<>();
            for (String transletName : targets) {
                MethodType requestMethod = null;
                for (MethodType methodType : MethodType.values()) {
                    if (transletName.startsWith(methodType.name() + " ")) {
                        transletName = transletName.substring(methodType.name().length() + 1);
                        requestMethod = methodType;
                        break;
                    }
                }
                TransletRule transletRule;
                if (requestMethod != null) {
                    transletRule = transletRuleRegistry.getTransletRule(transletName, requestMethod);
                } else {
                    transletRule = transletRuleRegistry.getTransletRule(transletName);
                }
                if (transletRule == null) {
                    return failed(error("Unknown translet: " + targets[0]));
                }
                transletRules.add(transletRule);
            }
        }
        int count = 0;
        StringWriter writer = new StringWriter();
        for (TransletRule transletRule : transletRules) {
            Parameters transletParameters = RuleToParamsConverter.toTransletParameters(transletRule);

            if (count > 0) {
                writer.write("----------------------------------------------------------------------------");
                writer.write(AponFormat.NEW_LINE);
            }

            AponWriter aponWriter = new AponWriter(writer, true);
            aponWriter.setIndentString("  ");
            aponWriter.write(transletParameters);
            count++;
        }
        if (count == 0) {
            return success("No translets");
        } else {
            return success(writer.toString().trim());
        }
    }

    private CommandResult listScheduledJobs(DaemonService service, String[] keywords) {
        Collection<ScheduleRule> scheduleRules = service.getActivityContext().getScheduleRuleRegistry().getScheduleRules();
        Formatter formatter = new Formatter();
        formatter.format("-%4s-+-%-20s-+-%-33s-+-%-8s-%n", "----", "--------------------",
                "---------------------------------", "--------");
        formatter.format(" %4s | %-20s | %-33s | %-8s %n", "No.", "Schedule ID", "Job Name", "Enabled");
        formatter.format("-%4s-+-%-20s-+-%-33s-+-%-8s-%n", "----", "--------------------",
                "---------------------------------", "--------");
        int num = 0;
        for (ScheduleRule scheduleRule : scheduleRules) {
            for (ScheduledJobRule jobRule : scheduleRule.getScheduledJobRuleList()) {
                if (keywords != null) {
                    boolean exists = false;
                    for (String keyw : keywords) {
                        if (jobRule.getTransletName().toLowerCase().contains(keyw.toLowerCase())) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        continue;
                    }
                }
                formatter.format("%5d | %-20s | %-33s | %-8s %n", ++num, scheduleRule.getId(),
                        jobRule.getTransletName(), !jobRule.isDisabled());
            }
        }
        if (num == 0) {
            formatter.format("%33s %s%n", " ", "No Data");
        }
        formatter.format("-%4s-+-%-20s-+-%-33s-+-%-8s-", "----", "--------------------",
                "---------------------------------", "--------");
        return success(formatter.toString());
    }

    private CommandResult detailScheduledJobRule(DaemonService service, String[] targets)
            throws IOException {
        ScheduleRuleRegistry scheduleRuleRegistry = service.getActivityContext().getScheduleRuleRegistry();
        if (targets == null || targets.length == 0) {
            Writer writer = new StringWriter();
            int count = 0;
            for (ScheduleRule scheduleRule : scheduleRuleRegistry.getScheduleRules()) {
                Parameters scheduleParameters = RuleToParamsConverter.toScheduleParameters(scheduleRule);
                if (count > 0) {
                    writer.write("----------------------------------------------------------------------------");
                    writer.write(AponFormat.NEW_LINE);
                }
                AponWriter aponWriter = new AponWriter(writer, true);
                aponWriter.setIndentString("  ");
                aponWriter.write(scheduleParameters);
                count++;
            }
            if (count == 0) {
                return success("No scheduled jobs");
            } else {
                return success(writer.toString().trim());
            }
        } else {
            Set<String> transletNames = new LinkedHashSet<>();
            for (ScheduleRule scheduleRule : scheduleRuleRegistry.getScheduleRules()) {
                for (ScheduledJobRule jobRule : scheduleRule.getScheduledJobRuleList()) {
                    for (String transletName : targets) {
                        if (jobRule.getTransletName().equals(transletName)) {
                            transletNames.add(jobRule.getTransletName());
                        }
                    }
                }
            }
            if (transletNames.isEmpty()) {
                return failed(error("Unknown scheduled jobs " + Arrays.toString(targets)));
            }
            int count = 0;
            Writer writer = new StringWriter();
            for (ScheduleRule scheduleRule : scheduleRuleRegistry.getScheduleRules()) {
                for (ScheduledJobRule jobRule : scheduleRule.getScheduledJobRuleList()) {
                    for (String transletName : transletNames) {
                        if (jobRule.getTransletName().equals(transletName)) {
                            ScheduleParameters scheduleParameters = new ScheduleParameters();
                            scheduleParameters.putValueNonNull(ScheduleParameters.description, scheduleRule.getDescription());
                            scheduleParameters.putValueNonNull(ScheduleParameters.id, scheduleRule.getId());
                            SchedulerParameters schedulerParameters = scheduleParameters.newParameters(ScheduleParameters.scheduler);
                            schedulerParameters.putValueNonNull(SchedulerParameters.bean, scheduleRule.getSchedulerBeanId());
                            TriggerParameters triggerParameters = scheduleRule.getTriggerParameters();
                            if (triggerParameters != null && scheduleRule.getTriggerType() != null) {
                                triggerParameters.putValueNonNull(TriggerParameters.type, scheduleRule.getTriggerType().toString());
                                schedulerParameters.putValue(SchedulerParameters.trigger, scheduleRule.getTriggerParameters());
                            }
                            scheduleParameters.putValue(ScheduleParameters.job, RuleToParamsConverter.toScheduledJobParameters(jobRule));
                            if (count > 0) {
                                writer.write("----------------------------------------------------------------------------");
                                writer.write(AponFormat.NEW_LINE);
                            }
                            AponWriter aponWriter = new AponWriter(writer, true);
                            aponWriter.setIndentString("  ");
                            aponWriter.write(scheduleParameters);
                            count++;
                        }
                    }
                }
            }
            return success(writer.toString().trim());
        }
    }

    private CommandResult changeJobActiveState(DaemonService service, String[] targets, boolean disabled) {
        if (targets == null || targets.length == 0) {
            return failed(error("Please specify jobs to be enabled or disabled"));
        }
        ScheduleRuleRegistry scheduleRuleRegistry = service.getActivityContext().getScheduleRuleRegistry();
        Set<ScheduledJobRule> scheduledJobRules = scheduleRuleRegistry.getScheduledJobRules(targets);
        if (scheduledJobRules.isEmpty()) {
            return failed(error("Unknown scheduled jobs " + Arrays.toString(targets)));
        }
        Formatter formatter = new Formatter();
        for (ScheduledJobRule jobRule : scheduledJobRules) {
            if (disabled) {
                if (jobRule.isDisabled()) {
                    formatter.format("Scheduled job '%s' on schedule '%s' is already inactive",
                            jobRule.getTransletName(), jobRule.getScheduleRule().getId());
                } else {
                    jobRule.setDisabled(true);
                    formatter.format("Scheduled job '%s' on schedule '%s' is now inactive",
                            jobRule.getTransletName(), jobRule.getScheduleRule().getId());
                }
            } else {
                if (!jobRule.isDisabled()) {
                    formatter.format("Scheduled job '%s' on schedule '%s' is already active",
                            jobRule.getTransletName(), jobRule.getScheduleRule().getId());
                } else {
                    jobRule.setDisabled(false);
                    formatter.format("Scheduled job '%s' on schedule '%s' is now active",
                            jobRule.getTransletName(), jobRule.getScheduleRule().getId());
                }
            }
        }
        return success(formatter.toString());
    }

    @Override
    public Descriptor getDescriptor() {
        return descriptor;
    }

    private class CommandDescriptor implements Descriptor {

        @Override
        public String getNamespace() {
            return NAMESPACE;
        }

        @Override
        public String getName() {
            return COMMAND_NAME;
        }

        @Override
        public String getDescription() {
            return "Executes a translet";
        }

    }

}
