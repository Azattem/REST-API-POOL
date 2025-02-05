package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Client;
import model.Order;
import service.OrderService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public record OrderHandlersContainer(OrderService orderService) {
    public static final String rootPath = "/api/v0/pool/timetable";
    public void addHandlers(HttpServer httpServer){
        httpServer.createContext(rootPath+"/reserve", new AddOrderHandler());
        httpServer.createContext(rootPath+"/all", new GetOrderHandler());
    }

    //Handler для POST reserve
    private class AddOrderHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestData = null;
            if ("POST".equals(httpExchange.getRequestMethod())) {
                requestData = handleRequest(httpExchange);
                handleResponse(httpExchange, requestData);
            }

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
            String response = null;
            String clientId = requestData.split("\"clientId\": ")[1].split(",")[0];
            //"8.00,02.05.2025" Пример datetime
            String datetime = requestData.split("\"datetime\": \"")[1].split("\"")[0];
            String time = datetime.split(".00")[0].trim();
            String date = datetime.split(",")[1].trim();
            System.out.println(time+date);
            orderService.create(new Order(date,time,Integer.parseInt(clientId.trim())));
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(200, 0);
            outputStream.close();
        }
    }

    //Handler для GET getAll
    private class GetOrderHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestData = null;
            if ("GET".equals(httpExchange.getRequestMethod())) {
                requestData = handleRequest(httpExchange);
                handleResponse(httpExchange, requestData);
            }

        }

        //Метод преобразует request в String
        private String handleRequest(HttpExchange httpExchange) throws IOException {
            return httpExchange.getRequestURI().toString();
        }

        //Метод генерует и отправялет response на основании данных request
        private void handleResponse(HttpExchange httpExchange, String requestData) throws IOException {
            String date = requestData.split("date=")[1].trim();
            String response = null;
            StringBuilder stringBuilder = new StringBuilder();
            HashMap<String, Integer> map = new HashMap<>();
            for (Order order: orderService.readAllByDate(date)) {
                if(map.containsKey(order.getTime())){
                map.put( order.getTime(),map.get(order.getTime())+1);
                }else {
                map.put(order.getTime(),1);
                }
            }
            for (Map.Entry<String,Integer> entry:map.entrySet() ) {
                stringBuilder.append("{[\n" +
                        "    \"time\": \"" + entry.getKey() + "\",\n" +
                        "    \"count\": " + entry.getValue() + ",\n" +
                        "]}\n");
            }
            response = stringBuilder.toString();
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(200, response.length());
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }

}
