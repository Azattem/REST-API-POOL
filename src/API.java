import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import handlers.ClientHandlersContainer;
import model.Client;
import service.ClientService;
import service.ClientServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class API {
    public final static ClientService clientService = new ClientServiceImpl();
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost",8080),0);
        new ClientHandlersContainer(clientService).addHandlers(server);

        server.setExecutor(null);
        server.start();


        System.out.println("Hello world!");
    }





}