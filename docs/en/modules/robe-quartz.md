# robe-quartz
---
Quartz bundle for dropwizard.
## Motivation
Creating an auth bundle which can provide following points. 
* easy to understand 
* configurable 
* ready to use
* Cron,On application start,On application stop and Basic including 4 different types.

## Getting started
You have to complete 4 steps in order to start using quartz bundle.
* Add dependency (Maven sample)

```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-quartz</artifactId>
  <version>0.5.0</version>
</dependency>
```

* Decide the properties. and how to serve in yml.

```yml
quartz:
  instanceName: QuartzScheduler
  threadPoolClass: org.quartz.simpl.SimpleThreadPool
  threadCount: 10
  threadPriority: 8
  providers: [io.robe.quartz.info.annotation,io.robe.admin.quartz]
  scanPackages: [io.robe.admin.timely]
  skipUpdateCheck: false
  jobStore:
    className: org.quartz.simpl.RAMJobStore
   ```
   
* Add bundle to the bootstrap at you Aplication

```java
bootstrap.addBundle(new QuartzBundle<T>());
```
* Use `@QJob` annotation to make your hibernateJobInfo class configured automaticly. Sample multiple trigger hibernateJobInfo.

```java
@QJob(name = "SampleJob", description = "Sample Quartz Job for a demonstration.",
        triggers = {
                @QTrigger(type = TriggerInfo.Type.CRON, name = "Minute", group = "TEST", cron = "1 * * * * ?"),
                @QTrigger(type = TriggerInfo.Type.CRON, name = "Second", group = "TEST", cron = "* * * * * ?", startTime= 1418805997000L),
                @QTrigger(type = TriggerInfo.Type.SIMPLE, name = "Simple", group = "TEST", repeatCount = 5, repeatInterval = 2000),
                @QTrigger(type = TriggerInfo.Type.ON_APP_START, name = "AppStart", group = "TEST")
        })
public class SampleJob implements org.quartz.Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("TRIGGER: " + context.getTrigger().getKey().getName() + " This is a Quartz Job   Next fire time : " + context.getNextFireTime());
    }
}
```
Now it is fire with 4 different triggers.

## Details
Configuration, usage and details will be explained below.
### Configuration
Configuration includes two group of fields. 
* robe-quartz configuration. Packages for discovery.
 * `providers`: package list of **JobProviders**. It will search providers itself. *__!!Will explain later!!__*
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
 
         
	```yml
	quartz:
	  instanceName: QuartzScheduler
	  threadPoolClass: org.quartz.simpl.SimpleThreadPool
	  threadCount: 10
	  threadPriority: 8
	  providers: [io.robe.quartz.info.annotation,io.robe.admin.quartz]
	  scanPackages: [io.robe.admin.timely]
	  skipUpdateCheck: false
	  jobStore:
		 className: org.quartz.impl.jdbcjobstore.JobStoreTX
		    properties:
		      org.quartz.jobStore.dataSource: QuarztDS
		      org.quartz.dataSource.myDS.driver: com.mysql.jdbc.Driver
		      org.quartz.dataSource.myDS.URL: jdbc:mysql://localhost:3306/robe
		      org.quartz.dataSource.myDS.user: root
		      org.quartz.dataSource.myDS.password:
		      org.quartz.dataSource.myDS.maxConnections: 10
		      org.quartz.jobStore.tablePrefix: QRTZ_
		      org.quartz.jobStore.driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
	```
