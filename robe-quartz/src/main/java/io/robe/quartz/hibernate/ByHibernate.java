package io.robe.quartz.hibernate;

import io.robe.hibernate.HibernateBundle;
import io.robe.quartz.CronProvider;
import io.robe.quartz.QuartzBundle;
import io.robe.quartz.QuartzJob;
import io.robe.quartz.QuartzTrigger;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.quartz.Job;

public class ByHibernate implements CronProvider {
    private static final Logger LOGGER = Logger.getLogger(ByHibernate.class);
    private static HibernateBundle hibernateBundle;

    public static void setHibernateBundle(HibernateBundle hibernateBundle) {
        ByHibernate.hibernateBundle = hibernateBundle;
    }


    @Override
    public QuartzJob getQuartzJob(Class<? extends Job> clazz) {
        Session session = hibernateBundle.getSessionFactory().openSession();
        String jobName = clazz.getName();
        JobEntity quartzJob = dbLookup(clazz.getName(), session);


        //Create if it is not managed yet
        if (quartzJob == null) {
            quartzJob = new JobEntity();
            quartzJob.setJobClassName(jobName);
            quartzJob.setSchedulerName(QuartzBundle.DYNAMIC_GROUP);
            quartzJob.setDescription("Default Description");
            session.persist(quartzJob);
            LOGGER.info(jobName + " Job saved to database");
            session.flush();
            session.close();
            return null;
        }
        quartzJob.setClazz(clazz);

        for (QuartzTrigger entity : quartzJob.getTriggers()) {
            ((TriggerEntity) entity).setActive(false);
        }

        session.flush();
        session.close();
        return quartzJob;
    }


    /**
     * If that job parameter exist in database gets old job
     *
     * @param jobClassName Quartz Job class name
     * @param session      Hibernate session
     * @return
     */
    private JobEntity dbLookup(String jobClassName, Session session) {
        return (JobEntity) session.createCriteria(JobEntity.class).add(Restrictions.eq("jobClassName", jobClassName)).uniqueResult();
    }


}
