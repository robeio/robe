package io.robe.admin.websocket;

import io.dropwizard.auth.AuthenticationException;
import io.robe.admin.hibernate.dao.*;
import io.robe.auth.data.store.*;
import io.robe.auth.token.TokenAuthenticator;
import io.robe.guice.GuiceBundle;
import io.robe.websocket.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import io.robe.auth.token.BasicToken;

import java.net.HttpCookie;
import java.util.Optional;

public abstract class AuthenticatedWebSocket extends WebSocket {

    private static TokenAuthenticator authenticator;
    private static SessionFactory sessionFactory;

    public TokenAuthenticator getAuthenticator() {
        if (authenticator == null) {
            sessionFactory = GuiceBundle.getInjector().getInstance(SessionFactory.class);
            ServiceStore serviceStore = new ServiceDao(sessionFactory);
            UserStore userStore = new UserDao(sessionFactory);
            RoleStore roleStore = new RoleDao(sessionFactory);
            PermissionStore permissionStore = new PermissionDao(sessionFactory);
            RoleGroupStore roleGroupStore = new RoleGroupDao(sessionFactory);
            this.authenticator = new TokenAuthenticator(userStore, serviceStore, roleStore, permissionStore, roleGroupStore);
        }
        return authenticator;
    }

    @Override
    public String onConnect(Session session) {
        for (HttpCookie cookie : session.getUpgradeRequest().getCookies()) {
            if ("auth-token".equals(cookie.getName())) {
                String authToken = cookie.getValue();
                TokenAuthenticator authenticator = getAuthenticator();
                org.hibernate.Session hSession = sessionFactory.openSession();
                ManagedSessionContext.bind(hSession);
                Optional<BasicToken> token;
                try {
                    token = authenticator.authenticate(authToken);
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                    return null;
                }
                if (!token.isPresent()) {
                    return null;
                }
                hSession.close();
                return token.get().getUserId();
            }
        }
        return null;
    }
}
