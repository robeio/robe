# Robe-Quartz
Robe-quartz modules helps developer to define and manage jobs easily. With provider API and annotation support it discovers jobs and triggers easily and schedules them. 
By implementing the following interfaces you can easly include your custom job provider to the central management.
These interfaces are;

* io.robe.quartz.info.JobInfoProvider
* io.robe.quartz.info.JobInfo
* io.robe.quartz.info.TriggerInfo

## Getting started
 Add dependency (Maven sample)

```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-quartz</artifactId>
  <version>0.5.1.0-beta.23</version>
</dependency>
```
You can find sample default implementations for annotation support at `io.robe.quartz.info.annotation` package at robe-quartz.
Also hibernate implementation for the API is included at robe-admin. Here some quick examples

### By Annotation (default)
For using the default annotation support you should provide all the triggers as an annotation array. 
You can see below.

```java
@RobeJob(name = "Hello ANJOB", description = "A simple job says ANJOB", triggers = {
        @RobeTrigger(cron = "0/6 * * * * ?", name = "Every 6 seconds", group = "Sample", type = TriggerInfo.Type.CRON),
        @RobeTrigger(cron = "0/10 * * * * ?", name = "Every 10 seconds", group = "Sample", type = TriggerInfo.Type.CRON)
})
public class AnnJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("---" + jobExecutionContext.getTrigger().getKey().getName());
    }
}
```
This job will print the logs below according to their cron timing.

``` bash
INFO  [2017-01-03 07:06:48,005] io.robe.admin.AnnJob: ---Every 6 seconds
INFO  [2017-01-03 07:06:50,004] io.robe.admin.AnnJob: ---Every 10 seconds
```
### By Hibernate (custom)
For using custom api, you should give the RobeJob annotation not with triggers but with a provider class. This time it is `HibernateJobInfoProvider.class`.
You can see the custom impl of the provider API. For more please take a look at robe-admin.

```java
@RobeJob(name = "DBJOB", description = "A simple job says DB", provider = HibernateJobInfoProvider.class)
public class DBJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("-------------------------");
        LOGGER.info("DB!!!" + jobExecutionContext.getTrigger().getKey().getName());
        LOGGER.info("-------------------------");
    }
}
```

This job will print the logs below according to their cron timing.

``` bash
INFO  [2017-01-03 07:06:48,005] io.robe.admin.DBJob: -------------------------
INFO  [2017-01-03 07:06:48,005] io.robe.admin.DBJob: DB!!!Every 6 second
INFO  [2017-01-03 07:06:48,005] io.robe.admin.DBJob: -------------------------
INFO  [2017-01-03 07:06:50,004] io.robe.admin.DBJob: -------------------------
INFO  [2017-01-03 07:06:50,004] io.robe.admin.DBJob: DB!!!Every 10 second
INFO  [2017-01-03 07:06:50,004] io.robe.admin.DBJob: -------------------------
```
## Details
Configuration, usage and details will be explained below.
### Configuration
Configuration includes two group of fields. 
* robe-quartz configuration. Packages for discovery.
 * `scanPackages`: Package list of Jobs. All jobs under these packages will discovered automatic.
* quartz configuration. Quartz configuration mappings.
 * `instanceName`: `org.quartz.scheduler.instanceName`
 * `threadCount`: `org.quartz.threadPool.threadCount`
 * `threadPriority`:`org.quartz.threadPool.threadPriority`
 * `skipUpdateCheck`:`org.quartz.scheduler.skipUpdateCheck`
 * `jobStore`:
 * `className`: org.quartz.jobStore.class 
 * `properties`: Properies of jobstore
 
 Sample of alternate jobstore with extra properties.
 
 ```
 quartz:
   scanPackages: [io.robe.admin]
   properties:
     org.quartz.scheduler.instanceName: QuartzScheduler
     org.quartz.threadPool.class: org.quartz.simpl.SimpleThreadPool
     org.quartz.threadPool.threadCount: 1
     org.quartz.threadPool.threadPriority: 8
     org.quartz.scheduler.skipUpdateCheck: false
     org.quartz.jobStore.class: org.quartz.simpl.RAMJobStore
 ```
