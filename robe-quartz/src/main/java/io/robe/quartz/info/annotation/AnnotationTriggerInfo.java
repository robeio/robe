package io.robe.quartz.info.annotation;

import io.robe.quartz.info.TriggerInfo;
import io.robe.quartz.RobeTrigger;

class AnnotationTriggerInfo implements TriggerInfo {
    private String name;
    private String group;
    private long startTime;
    private long endTime;
    private int repeatCount;
    private long repeatInterval;
    private String cron;
    private Type type;

    public AnnotationTriggerInfo(RobeTrigger ann) {
        name = ann.name();
        group = ann.group();
        startTime = ann.startTime();
        endTime = ann.endTime();
        repeatCount = ann.repeatCount();
        repeatInterval = ann.repeatInterval();
        cron = ann.cron();
        type = ann.type();

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    @Override
    public int getRepeatCount() {
        return repeatCount;
    }

    @Override
    public long getRepeatInterval() {
        return repeatInterval;
    }

    @Override
    public String getCron() {
        return cron;
    }

    @Override
    public Type getType() {
        return type;
    }
}