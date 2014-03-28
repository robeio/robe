package io.robe.quartz;

public class QuartzConfiguration {
    private String instanceName;
    private String threadPoolClass;
    private int threadCount;
    private int threadPriority;
    private String jobStoreClass;
    private String[] scanPackages;
    private String driverDelegateClass;
    private String tablePrefix;
    private int maxConnections;
    private String skipUpdateCheck;

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

    public String getJobStoreClass() {
        return jobStoreClass;
    }

    public void setJobStoreClass(String jobStoreClass) {
        this.jobStoreClass = jobStoreClass;
    }

    public String[] getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    public String getDriverDelegateClass() {
        return driverDelegateClass;
    }

    public void setDriverDelegateClass(String driverDelegateClass) {
        this.driverDelegateClass = driverDelegateClass;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public String getSkipUpdateCheck() {
        return skipUpdateCheck;
    }

    public void setSkipUpdateCheck(String skipUpdateCheck) {
        this.skipUpdateCheck = skipUpdateCheck;
    }
}
