package io.robe.timely;

import io.robe.mail.MailSender;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by sinanselimoglu on 14/02/14.
 */

@Timely(cron="0 0/33 * * * ?")
public class HelloQuartz implements Job {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public HelloQuartz() {
    };

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey datakey = context.getJobDetail().getKey();
        JobDataMap map = context.getMergedJobDataMap();
        LOGGER.info("FENEBRBAHÇE  :  " +  map.get("Says")+" Key : "+datakey.getName());
    }
}
