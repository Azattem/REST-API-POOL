import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Client;
import service.ClientService;
import service.ClientServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public class API {
    public final static ClientService clientService = new ClientServiceImpl();
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost",8080),0);
        server.createContext("/api/v0/pool/client/add", new AddClientHandler());
        server.setExecutor(null);
        server.start();


        System.out.println("Hello world!");
    }

    private static class AddClientHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestData = null;
            if("POST".equals(httpExchange.getRequestMethod())) {
            requestData = handleRequest(httpExchange);
            }
            handleResponse(httpExchange,requestData);

        }
        //Метод преобразует request в String
        private String handleRequest(HttpExchange httpExchange) throws IOException {
            InputStreamReader inputStreamReader = new InputStreamReader( httpExchange.getRequestBody());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder httpRequest = new StringBuilder();
            while (bufferedReader.ready()){
                httpRequest.append((char)bufferedReader.read());
            }
            bufferedReader.close();
            inputStreamReader.close();
            return httpRequest.toString();
        }
        //Метод генерует и отправялет response на основании данных request
        private void handleResponse(HttpExchange httpExchange,String requestData) throws IOException {
            String Response = null;
            String name = requestData.split("\"name\": ")[1].split(",")[0];
            String phone = requestData.split("\"phone\": ")[1].split(",")[0];
            String email = requestData.split("\"email\": ")[1].split(",")[0];
            clientService.create(new Client(name,phone,email));

            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(200, Response.length());
            outputStream.write(Response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }

}