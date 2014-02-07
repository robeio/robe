package io.robe.view;

import com.yammer.dropwizard.views.View;


public class ChangePasswordView extends View {

    private String tickedOid;

    public ChangePasswordView(String tickedOid) {
        super("ChangePassword.ftl");
        this.tickedOid = tickedOid;
    }

    public String getTickedOid() {
        return this.tickedOid;
    }

}
