package io.robe.admin.quartz.hibernate;

import io.robe.hibernate.HibernateBundle;
import io.robe.quartz.common.JobInfo;
import io.robe.quartz.common.JobProvider;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.quartz.Job;

public class HibernateJobProvider extends JobProvider {


    private static HibernateBundle hibernateBundle;

    public static void setHibernateBundle(HibernateBundle hibernateBundle) {
        HibernateJobProvider.hibernateBundle = hibernateBundle;
    }

    @Override
    public JobInfo getJob(Class<? extends Job> clazz) {
        Session session = hibernateBundle.getSessionFactory().openSession();
        JobEntity quartzJob = (JobEntity) session.createCriteria(JobEntity.class).add(Restrictions.eq("jobClassName", clazz)).uniqueResult();
        if(quartzJob == null)
            return  null;
        Hibernate.initialize(quartzJob.getTriggers());
        session.close();
        return quartzJob;
    }
}
