package io.robe.admin.quartz.hibernate;

import io.robe.hibernate.RobeHibernateBundle;
import io.robe.quartz.common.JobInfo;
import io.robe.quartz.common.JobProvider;
import io.robe.quartz.common.TriggerInfo;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.quartz.Job;

import java.util.Iterator;
import java.util.List;

public class HibernateJobProvider extends JobProvider {

    @Override
    public JobInfo getJob(Class<? extends Job> clazz) {
        Session session = RobeHibernateBundle.getInstance().getSessionFactory().openSession();
        JobEntity quartzJob = (JobEntity) session.createCriteria(JobEntity.class).add(Restrictions.eq("jobClass", clazz)).uniqueResult();
        if (quartzJob == null)
            return null;


        List<TriggerInfo> triggerEntities = session.createCriteria(TriggerEntity.class).add(Restrictions.eq("jobOid", quartzJob.getOid())).list();
        session.close();

        quartzJob.setTriggers(triggerEntities);

        Iterator<TriggerInfo> iterator = triggerEntities.iterator();

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
