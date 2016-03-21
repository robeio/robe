package io.robe.admin.quartz.hibernate;

import io.robe.hibernate.RobeHibernateBundle;
import io.robe.quartz.common.JobInfo;
import io.robe.quartz.common.JobProvider;
import io.robe.quartz.common.TriggerInfo;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.quartz.Job;

import java.util.Iterator;

public class HibernateJobProvider extends JobProvider {

    @Override
    public JobInfo getJob(Class<? extends Job> clazz) {
        Session session = RobeHibernateBundle.getInstance().getSessionFactory().openSession();
        JobEntity quartzJob = (JobEntity) session.createCriteria(JobEntity.class).add(Restrictions.eq("jobClass", clazz)).uniqueResult();
        if (quartzJob == null)
            return null;
        Hibernate.initialize(quartzJob.getTriggers());
        session.close();
        Iterator<TriggerInfo> iterator = quartzJob.getTriggers().iterator();

        while (iterator.hasNext()) {
            TriggerInfo info = iterator.next();
            if (info instanceof TriggerEntity) {
                TriggerEntity triggerEntity = (TriggerEntity) info;
                if (triggerEntity.getType().equals(TriggerInfo.Type.CRON)) {
                    if (!triggerEntity.isActive()) {
                        iterator.remove();
                    }
                }
            }
        }

        return quartzJob;
    }
}
