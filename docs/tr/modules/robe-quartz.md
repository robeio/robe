# robe-quartz
---
Dropwizard için Quartz modulüdür.

## Motivasyon
Yarattığınız bir quartz bundle aşağıdaki özellikleri sağlar.
 
* Kolay kullanım
* Kolay yapılandırma 
* Kullanıma hazır
* Çoklu trigger desteği
* Cron,Uygulama açıldığında,Uygulama Kapandığında ve Temel olmak üzere 4 farklı tip.

## Başlarken 
Quartz modülünü kullanmak için 4 adımı tamamlamanız gerekmektedir.

* Bağımlılığı ekleyin (Örnek:Maven)

```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-quartz</artifactId>
  <version>0.5.0</version>
</dependency>
```

* Özelliklerini kendinize göre yml içinde yapılandırın.

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
   
* Uygulamanız içerisinde bootstrap e yeni bir bundle olarak ekleyin.

```java
bootstrap.addBundle(new QuartzBundle<T>());
```

* `@QJob` annotation ile job sınıfınızın otomatik yapılandırılmasını sağlayın. Örnek çoklu trigger job sınıfı:

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
Artık 4 faklı trigger tipi ile çalıştırabilirsiniz.

## Detay
Yapılandırma, kullanım, varsayılan ayarlar aşağıda açıklanacaktır.
### Yapılandırma
Yapılandırma alanları iki grubu içerir:
 
* robe-quartz yapılandırması. Paket ismi ile ortaya çıkarmak(bulmak).
 * `providers`: **JobProviders** paket listesidir. Bu sağlayıcıları kendisi arayacak . *__!!Daha sonra açıklanacak!!__*
 * `scanPackages`: Bütün job ların paket listesi.Bu paket altındaki bütün job lar otomatik ortaya çıkarılacaktır(bulunacaktır).
* quartz yapılandırması. Quartz yapılandırma dönüşümleri:
 * `instanceName`: `org.quartz.scheduler.instanceName`
 * `threadCount`: `org.quartz.threadPool.threadCount`
 * `threadPriority`:`org.quartz.threadPool.threadPriority`
 * `skipUpdateCheck`:`org.quartz.scheduler.skipUpdateCheck`
 * `jobStore`:
 * `className`: org.quartz.jobStore.class 
 * `properties`: jobstore özellikleri

Extra özelliklere sahip alternatif jobstore özellikleri
 
         
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
