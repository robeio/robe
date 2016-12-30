package io.robe.admin.quartz;

import io.robe.admin.dto.JobInfoDTO;
import io.robe.admin.hibernate.entity.HibernateJobInfo;
import io.robe.admin.hibernate.entity.HibernateTriggerInfo;
import io.robe.hibernate.RobeHibernateBundle;
import io.robe.quartz.RobeJob;
import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.JobInfoProvider;
import io.robe.quartz.info.TriggerInfo;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Iterator;
import java.util.List;

public class HibernateJobInfoProvider extends JobInfoProvider {

    @Override
    public JobInfo getJob(Class<? extends org.quartz.Job> clazz) {
        RobeJob infoAnn = clazz.getDeclaredAnnotation(RobeJob.class);
        if (infoAnn == null)
            return null;
        Session session = RobeHibernateBundle.getInstance().getSessionFactory().openSession();
        HibernateJobInfo quartzHibernateJobInfo = (HibernateJobInfo) session.createCriteria(HibernateJobInfo.class).add(Restrictions.eq("jobClass", clazz)).uniqueResult();
        if (quartzHibernateJobInfo == null) {
            JobInfoDTO info = new JobInfoDTO();
            info.setName(infoAnn.name());
            info.setDescription(infoAnn.description());
            info.setProvider(infoAnn.provider());
            info.setJobClass(clazz);
            return info;
        }

        List<TriggerInfo> triggerEntities = session.createCriteria(HibernateTriggerInfo.class).add(Restrictions.eq("jobOid", quartzHibernateJobInfo.getOid())).list();
        session.close();

        JobInfoDTO dto = new JobInfoDTO(quartzHibernateJobInfo);

        dto.setTriggers(triggerEntities);

        Iterator<TriggerInfo> iterator = triggerEntities.iterator();

        while (iterator.hasNext()) {
            TriggerInfo info = iterator.next();
            if (info instanceof HibernateTriggerInfo) {
                HibernateTriggerInfo hibernateTriggerInfoEntity = (HibernateTriggerInfo) info;
                if (hibernateTriggerInfoEntity.getType().equals(TriggerInfo.Type.CRON)) {
                    if (!hibernateTriggerInfoEntity.isActive()) {
                        iterator.remove();
                    }
                }
            }
        }

        return dto;
    }
}
