package io.robe.view;

import com.yammer.dropwizard.views.View;


public class ChangePasswordView extends View {

    public ChangePasswordView(String tickedOid) {
        super("ChangePassword.ftl");
    }

}
