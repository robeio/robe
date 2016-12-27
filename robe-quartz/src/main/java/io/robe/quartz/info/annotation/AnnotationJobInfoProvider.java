package io.robe.quartz.info.annotation;

import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.JobInfoProvider;
import io.robe.quartz.RobeJob;
import org.quartz.Job;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Util class that helps converting annotations to quartz objects.
 */
public class AnnotationJobInfoProvider extends JobInfoProvider {

    @Override
    public JobInfo getJob(Class<? extends Job> clazz) {
        RobeJob jAnn = clazz.getAnnotation(RobeJob.class);
        checkNotNull(jAnn);
        return new AnnotationJobInfo(jAnn, clazz);
    }
}
