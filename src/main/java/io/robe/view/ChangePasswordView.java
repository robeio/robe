package io.robe.view;

import com.yammer.dropwizard.views.View;
import io.robe.dto.BasicPair;


public class ChangePasswordView extends View {

    private BasicPair ticket;

    public ChangePasswordView(String tickedOid) {
        super("ChangePassword.ftl");
        this.ticket = new BasicPair("tickedOid", tickedOid);
    }

    public BasicPair getTicket() {
        return ticket;
    }
}
