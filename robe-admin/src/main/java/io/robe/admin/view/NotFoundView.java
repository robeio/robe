package io.robe.admin.view;

import io.dropwizard.views.View;
import io.robe.common.dto.BasicPair;


public class NotFoundView extends View {

    private BasicPair status;

    public NotFoundView(String status) {
        super("NotFound.ftl");
        this.status = new BasicPair("status", status);
    }

    public BasicPair getStatus() {
        return status;
    }
}
