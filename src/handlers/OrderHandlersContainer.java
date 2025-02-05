package handlers;

import com.sun.net.httpserver.HttpServer;
import service.OrderService;

public record OrderHandlersContainer(OrderService orderService) {
    public static final String rootPath = "/api/v0/pool/timetable";
    public void addHandlers(HttpServer httpServer){

    }

}
