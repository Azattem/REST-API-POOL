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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public record OrderHandlersContainer(OrderService orderService) implements HandlersContainer{
    public static final int workStart = 9;
    public static final int workEnds = 17;
    public static final String rootPath = "/api/v0/pool/timetable";
    public void addHandlers(HttpServer httpServer){
        httpServer.createContext(rootPath+"/reserve", new AddOrderHandler());
        httpServer.createContext(rootPath+"/all", new GetOrderHandler());
        httpServer.createContext(rootPath+"/available", new GetAvailableOrderHandler());
        httpServer.createContext(rootPath+"/cancel", new GetCancelHandler());

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
            ArrayList<String> arrayList = new ArrayList<>();
            HashMap<String, Integer> map = new HashMap<>();
            for (Order order: orderService.readAllByDate(date)) {
                if(map.containsKey(order.getTime())){
                    map.put( order.getTime(),map.get(order.getTime())+1);
                }else {
                    map.put(order.getTime(),1);
                    arrayList.add(order.getTime());
                }
            }
            arrayList.sort(Comparator.naturalOrder());
            for (int i = 0; i < arrayList.size(); i++) {
                stringBuilder.append("{[\n" +
                        "    \"time\": \"" + arrayList.get(i) + "\",\n" +
                        "    \"count\": " + map.get(arrayList.get(i)) + ",\n" +
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
    //Handler для GET getAvailable
    private class GetAvailableOrderHandler implements HttpHandler {
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
            //минимальное допустимое время заказа и максимальное
            final int workDayStart = 9;
            final int lastOrderTime = 17;
            final int maxOrdersInHour = 10;
            for (int i = workDayStart; i <= lastOrderTime; i++) {
                if(map.containsKey(String.valueOf(i))){
                    stringBuilder.append("{[\n" +
                            "    \"time\": \"" + i + "\",\n" +
                            "    \"count\": " + (maxOrdersInHour - map.get(String.valueOf(i))) + ",\n" +
                            "]}\n");
                }else {
                    stringBuilder.append("{[\n" +
                            "    \"time\": \"" + i + "\",\n" +
                            "    \"count\": " + maxOrdersInHour + ",\n" +
                            "]}\n");
                }
            }
            response = stringBuilder.toString();
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(200, response.length());
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
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
            //"8.00,02.05.2025" Пример datetime
            String clientId = requestData.split("\"clientId\": ")[1].split(",")[0];
            String datetime = requestData.split("\"datetime\": \"")[1].split("\"")[0];
            String time = datetime.split(".00")[0].trim();
            String date = datetime.split(",")[1].trim();
            if(orderService.readAllByTimeDate(time,date).size()<10){
            if(Integer.parseInt(time)>=workStart&&Integer.parseInt(time)<=workEnds) {
            orderService.create(new Order(date, time, Integer.parseInt(clientId.trim())));
            }
            }
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(200, 0);
            outputStream.close();
        }
    }
    //Handler для GET cancel
    private class GetCancelHandler implements HttpHandler {
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
            int clientId = Integer.parseInt(requestData.split("\"clientId\": ")[1].split(",")[0].trim());
            String orderId = requestData.split("\"orderId\": \"")[1].split("\"")[0];
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(200, 0);
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }
    //Handler для Get search


}
