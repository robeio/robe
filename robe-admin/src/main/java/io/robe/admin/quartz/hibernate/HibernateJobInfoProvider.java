package io.robe.admin.quartz.hibernate;

import io.robe.admin.dto.JobEntityDTO;
import io.robe.hibernate.RobeHibernateBundle;
import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.JobInfoProvider;
import io.robe.quartz.info.TriggerInfo;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.quartz.Job;

import java.util.Iterator;
import java.util.List;

public class HibernateJobInfoProvider extends JobInfoProvider {

    @Override
    public JobInfo getJob(Class<? extends Job> clazz) {
        Session session = RobeHibernateBundle.getInstance().getSessionFactory().openSession();
        JobEntity quartzJob = (JobEntity) session.createCriteria(JobEntity.class).add(Restrictions.eq("jobClass", clazz)).uniqueResult();
        if (quartzJob == null)
            return null;

        List<TriggerInfo> triggerEntities = session.createCriteria(TriggerEntity.class).add(Restrictions.eq("jobOid", quartzJob.getOid())).list();
        session.close();

        JobEntityDTO dto = new JobEntityDTO(quartzJob);

        dto.setTriggers(triggerEntities);

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

        return dto;
    }
}
