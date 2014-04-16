package io.robe.quartz;

public interface QuartzTrigger {
    public String getOid();

    public String getCronExpression();

    public boolean isActive();

    public long getFireTime();

    public String getJobId();
}
