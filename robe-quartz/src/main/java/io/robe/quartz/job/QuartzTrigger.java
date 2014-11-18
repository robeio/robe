package io.robe.quartz.job;

public interface QuartzTrigger {
    String getOid();

    String getCronExpression();

    boolean isActive();

    long getFireTime();

    String getJobId();
}
