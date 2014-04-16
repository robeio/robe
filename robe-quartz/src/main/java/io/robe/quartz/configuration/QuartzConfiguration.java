package io.robe.quartz.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.robe.quartz.configuration.jobstore.JobStoreConfiguration;

import javax.validation.constraints.NotNull;

public class QuartzConfiguration {
    @NotNull
    @JsonProperty
    private String instanceName;
    @JsonProperty
    private String threadPoolClass = "org.quartz.simpl.SimpleThreadPool";
    @JsonProperty
    private int threadCount = 5;
    @JsonProperty
    private int threadPriority = 8;

    @NotNull
    @JsonProperty
    private String[] scanPackages;

    @JsonProperty
    private String skipUpdateCheck = "true";

    @NotNull
    @JsonProperty
    private JobStoreConfiguration jobStore;

    public JobStoreConfiguration getJobStore() {
        return jobStore;
    }

    public void setJobStore(JobStoreConfiguration jobStore) {
        this.jobStore = jobStore;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getThreadPoolClass() {
        return threadPoolClass;
    }

    public void setThreadPoolClass(String threadPoolClass) {
        this.threadPoolClass = threadPoolClass;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getThreadPriority() {
        return threadPriority;
    }

    public void setThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
    }

    public String[] getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    public String getSkipUpdateCheck() {
        return skipUpdateCheck;
    }

    public void setSkipUpdateCheck(String skipUpdateCheck) {
        this.skipUpdateCheck = skipUpdateCheck;
    }

}
