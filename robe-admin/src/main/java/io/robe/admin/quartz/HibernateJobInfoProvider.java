package io.robe.admin.quartz;

import io.robe.admin.dto.JobInfoDTO;
import io.robe.admin.hibernate.entity.HJobInfo;
import io.robe.admin.hibernate.entity.HTriggerInfo;
import io.robe.hibernate.RobeHibernateBundle;
import io.robe.quartz.RobeJob;
import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.JobInfoProvider;
import io.robe.quartz.info.TriggerInfo;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class HibernateJobInfoProvider extends JobInfoProvider {

    @Override
    public JobInfo getJob(Class<? extends org.quartz.Job> clazz) {
        RobeJob infoAnn = clazz.getDeclaredAnnotation(RobeJob.class);
        if (infoAnn == null)
            return null;
        Session session = RobeHibernateBundle.getInstance().getSessionFactory().openSession();
        HJobInfo quartzHJobInfo = (HJobInfo) session.createCriteria(HJobInfo.class).add(Restrictions.eq("jobClass", clazz)).uniqueResult();
        if (quartzHJobInfo == null) {
            JobInfoDTO info = new JobInfoDTO();
            info.setName(infoAnn.name());
            info.setDescription(infoAnn.description());
            info.setProvider(infoAnn.provider());
            info.setJobClass(clazz);
            info.setGroup(infoAnn.group());
            return info;
        }

        List<TriggerInfo> triggerEntities = session.createCriteria(HTriggerInfo.class).add(Restrictions.eq("jobOid", quartzHJobInfo.getOid())).list();
        session.close();

        JobInfoDTO dto = new JobInfoDTO(quartzHJobInfo);

        dto.setTriggers(triggerEntities);

        //TODO: This code will be active later.
//        Iterator<TriggerInfo> iterator = triggerEntities.iterator();

//        while (iterator.hasNext()) {
//            TriggerInfo info = iterator.next();
//            if (info instanceof HTriggerInfo) {
//                HTriggerInfo hibernateTriggerInfoEntity = (HTriggerInfo) info;
//                if (hibernateTriggerInfoEntity.getType().equals(TriggerInfo.Type.CRON)) {
//                    if (false) {
//                        iterator.remove();
//                    }
//                }
//            }
//        }

        return dto;
    }
}
