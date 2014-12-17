package io.robe.quartz.annotation;

import io.robe.quartz.common.JobInfo;
import io.robe.quartz.common.JobProvider;
import io.robe.quartz.job.schedule.QJob;
import org.quartz.Job;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Util class that helps converting annotations to quartz objects.
 */
public class AnnotationJobProvider extends JobProvider {

    @Override
    public JobInfo getJob(Class<? extends Job> clazz) {
        QJob jAnn = clazz.getAnnotation(QJob.class);
        checkNotNull(jAnn);
        return new AnnotationJobInfo(jAnn, clazz);
    }
}
