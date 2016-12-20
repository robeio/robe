package io.robe.websocket;

import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class WebSocketServlet extends org.eclipse.jetty.websocket.servlet.WebSocketServlet {
    private final Class webSocketClass;

    public WebSocketServlet(Class<?> webSocketClass) {
        this.webSocketClass = webSocketClass;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(webSocketClass);
    }
}
