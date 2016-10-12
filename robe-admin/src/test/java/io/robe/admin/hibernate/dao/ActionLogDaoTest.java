package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.ActionLog;

import java.util.Date;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class ActionLogDaoTest extends BaseDaoTest<ActionLog, ActionLogDao> {
    @Override
    public ActionLog instance() {

        ActionLog actionLog = new ActionLog();
        actionLog.setDescription("Description");
        actionLog.setActionTime(new Date());
        actionLog.setActionType("TEST");
        actionLog.setPositive(true);
        actionLog.setAdditionalData("NO-DATA");
        actionLog.setRemoteAddr("http:127.0.0.1:8080");
        return actionLog;
    }

    @Override
    public ActionLog update(ActionLog model) {
        model.setAdditionalData("Description updated");
        model.setActionType("TEST-1");
        return model;
    }
}
