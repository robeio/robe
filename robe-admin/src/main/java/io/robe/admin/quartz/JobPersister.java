package io.robe.admin.quartz;

import io.dropwizard.hibernate.HibernateBundle;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.admin.hibernate.entity.HibernateJobInfo;
import io.robe.admin.hibernate.entity.HibernateTriggerInfo;
import io.robe.guice.GuiceBundle;
import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.TriggerInfo;
import org.hibernate.Session;
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
        QuartzJobDao jobDao = new QuartzJobDao(sessionFactory);
        QuartzTriggerDao triggerDao = new QuartzTriggerDao(sessionFactory);
        for (JobInfo info : jobs.values()) {
            insertOrUpdate(jobDao, info, triggerDao);
        }
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().close();
        ManagedSessionContext.unbind(sessionFactory);
    }

    private void insertOrUpdate(QuartzJobDao jobDao, JobInfo info, QuartzTriggerDao triggerDao) {
        HibernateJobInfo record = jobDao.findByJobClass(info.getJobClass());
        if (record == null) {
            record = new HibernateJobInfo();
            record.setJobClass(info.getJobClass());
            record.setProvider(info.getProvider());
        }
        record.setName(info.getName());
        record.setDescription(info.getDescription());
        jobDao.create(record);
        if (!info.getProvider().equals(HibernateJobInfoProvider.class)) {
            for (TriggerInfo triggerInfo : info.getTriggers()) {
                insertOrUpdate(record.getOid(), triggerDao, triggerInfo);
            }
        }
    }

    private void insertOrUpdate(String jobOid, QuartzTriggerDao dao, TriggerInfo info) {
        HibernateTriggerInfo record = dao.findByJobOidAndName(jobOid, info.getName());
        if (record == null) {
            record = new HibernateTriggerInfo();
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
        record.setType(record.getType());
        dao.create(record);
    }
}
