package io.robe.admin.quartz;

import io.robe.admin.hibernate.dao.JobDao;
import io.robe.admin.hibernate.dao.TriggerDao;
import io.robe.admin.hibernate.entity.HTriggerInfo;
import io.robe.admin.hibernate.entity.HJobInfo;
import io.robe.guice.GuiceBundle;
import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.TriggerInfo;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Persists all discovered Job's info to the DB.
 * Helps to manage and view them.
 */
public class JobPersister {
    public JobPersister(ConcurrentHashMap<String, JobInfo> jobs) {
        SessionFactory sessionFactory = GuiceBundle.getInjector().getInstance(SessionFactory.class);
        ManagedSessionContext.bind(sessionFactory.openSession());
        JobDao jobDao = new JobDao(sessionFactory);
        TriggerDao triggerDao = new TriggerDao(sessionFactory);
        for (JobInfo info : jobs.values()) {
            insertOrUpdate(jobDao, info, triggerDao);
        }
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().close();
        ManagedSessionContext.unbind(sessionFactory);
    }

    private void insertOrUpdate(JobDao jobDao, JobInfo info, TriggerDao triggerDao) {
        HJobInfo record = jobDao.findByJobClass(info.getJobClass());
        if (record == null) {
            record = new HJobInfo();
            record.setJobClass(info.getJobClass());
            record.setProvider(info.getProvider());
        }
        record.setName(info.getName());
        record.setDescription(info.getDescription());
        record.setGroup(info.getGroup());
        jobDao.create(record);
        if (!info.getProvider().equals(HibernateJobInfoProvider.class)) {
            for (TriggerInfo triggerInfo : info.getTriggers()) {
                insertOrUpdate(record.getOid(), triggerDao, triggerInfo);
            }
        }
    }

    private void insertOrUpdate(String jobOid, TriggerDao dao, TriggerInfo info) {
        HTriggerInfo record = dao.findByJobOidAndName(jobOid, info.getName());
        if (record == null) {
            record = new HTriggerInfo();
            record.setActive(true);
            record.setJobOid(jobOid);
        }
        record.setStartTime(info.getStartTime());
        record.setEndTime(info.getEndTime());
        record.setName(info.getName());
        record.setCron(info.getCron());
        record.setRepeatCount(info.getRepeatCount());
        record.setRepeatInterval(info.getRepeatInterval());
        record.setGroup(info.getGroup());
        record.setType(info.getType());
        dao.create(record);
    }
}
