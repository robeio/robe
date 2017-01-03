### By Annotation
```java
@RobeJob(name = "Hello Job", description = "A simple hJobInfo says hello", triggers = {
       @RobeTrigger(cron = "0/5 * * * * ?", name = "Every 5 seconds", group = "Sample", type = TriggerInfo.Type.CRON)
})
public class HelloJob implements Job {
   private static final Logger LOGGER = LoggerFactory.getLogger(HelloJob.class);

   @Override
   public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
       LOGGER.info("-------------------------");
       LOGGER.info("Helllllloooooooo!!!");
       LOGGER.info("-------------------------");
   }
}
```

### By DB (Hibernate)
```java
@RobeJob(name = "Hello Job", description = "A simple hJobInfo says hello",
        provider = HibernateJobInfoProvider.class
)
public class HelloJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("-------------------------");
        LOGGER.info("Helllllloooooooo!!!");
        LOGGER.info("-------------------------");
    }
}
```