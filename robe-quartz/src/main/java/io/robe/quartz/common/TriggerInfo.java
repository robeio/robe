package io.robe.quartz.common;

public interface TriggerInfo {

    public String getName();

    public String getGroup();

    public long getStartTime();

    public long getEndTime();

    public int getRepeatCount();

    public long getRepeatInterval();

    public String getCron();

    public Type getType();

    enum Type {
        SIMPLE,
        CRON,
        ON_APP_START,
        ON_APP_STOP
    }

}
