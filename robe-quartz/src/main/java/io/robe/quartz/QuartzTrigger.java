package io.robe.quartz;

public interface QuartzTrigger {
    String getOid();

    String getCronExpression();

    boolean isActive();

    long getFireTime();

    String getJobId();
}
