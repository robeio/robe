package io.robe.admin.view;

import io.dropwizard.views.View;
import io.robe.common.dto.BasicPair;


public class RegisterView extends View {

    private BasicPair mail;
    private BasicPair ticket;
    private BasicPair url;

    public RegisterView(String mail, String ticket, String url) {
        super("Register.ftl");
        this.mail = new BasicPair("mail", mail);
        this.ticket = new BasicPair("ticket", ticket);
        this.url = new BasicPair("url", url);
    }

    public BasicPair getTicket() {
        return ticket;
    }

    public BasicPair getMail() {
        return mail;
    }

    public BasicPair getUrl() {
        return url;
    }
}
