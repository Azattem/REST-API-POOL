package handlers;

import com.sun.net.httpserver.HttpServer;

public interface HandlersContainer {
    public void addHandlers(HttpServer httpServer);
}
