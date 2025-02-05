package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Order;
import service.CrossService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

public record CrossHandlersContainer(CrossService crossService) implements HandlersContainer{

    @Override
    public void addHandlers(HttpServer httpServer) {
        httpServer.createContext("/api/v0/pool/client"+"/search",new SearchClientsHandler());
    }

    //Handler для Get Search
    private class SearchClientsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestData = null;
            if ("GET".equals(httpExchange.getRequestMethod())) {
                requestData = handleRequest(httpExchange);
            }
            handleResponse(httpExchange, requestData);
        }

        //Метод преобразует request в String
        private String handleRequest(HttpExchange httpExchange) throws IOException {
            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder httpRequest = new StringBuilder();
            while (bufferedReader.ready()) {
                httpRequest.append((char) bufferedReader.read());
            }
            bufferedReader.close();
            inputStreamReader.close();
            return httpRequest.toString();
        }

        //Метод генерует и отправялет response на основании данных request
        private void handleResponse(HttpExchange httpExchange, String requestData) throws IOException {

            String response = "";
            String name = requestData.split("\"name\": \"")[1].split("\",")[0];
            String date = requestData.split("\"date\": \"")[1].split("\"")[0];

            StringBuilder orderList = new StringBuilder();
            List<Order> list = crossService.getAllByName(name,date);
            for (Order order : list) {
                String s = "{[\n" +
                        "    \"orderId\": \"" + order.getOrderId() + "\",\n" +
                        "    \"date\": \"" + order.getDate() + "\"\n" +
                        "    \"time\": \"" + order.getTime() + "\"\n" +
                        "    \"clientId\": " + order.getClientId() + "\n" +
                        "]}\n";
                orderList.append(s);
            }
            response = orderList.toString();
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(200, response.length());
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }
}
