package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Client;
import service.ClientService;
import service.CustomService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

//вынесенная логика и добавлние HttpHandler к серверу для обработки клиентов
public record ClientHandlersContainer(ClientService clientService) implements HandlersContainer{
    public static final String rootPath = "/api/v0/pool/client";
    public void addHandlers(HttpServer httpServer) {
        httpServer.createContext(rootPath+"/all", new GetClientsHandler());
        httpServer.createContext(rootPath+"/add", new AddClientHandler());
        httpServer.createContext(rootPath+"/get", new GetClientHandler());
        httpServer.createContext(rootPath+"/update", new UpdateClientHandler());
    }

    //Handler для POST addClient
    private class AddClientHandler extends CustomService {
        //Метод генерует и отправялет response на основании данных request
        public String handleResponse(HttpExchange httpExchange, String requestData) {
            String response = "";
            String name = requestData.split("\"name\": \"")[1].split("\",")[0];
            String phone = requestData.split("\"phone\": \"")[1].split("\",")[0];
            String email = requestData.split("\"email\": \"")[1].split("\"\n")[0];
            clientService.create(new Client(name, phone, email));
            return response;
        }

        @Override
        public String handlePostRequest(HttpExchange httpExchange) throws IOException {
            return super.getInput(httpExchange);
        }

        @Override
        public String handleGetRequest(HttpExchange httpExchange) throws IOException {
            throw new IOException();
        }
    }

    //Handler для Get getClients
    private class GetClientsHandler implements HttpHandler {
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
            StringBuilder clientList = new StringBuilder();
            List<Client> list = clientService.readAll();
            for (Client client : list) {
                String s = "{[\n" +
                        "    \"id\": " + client.getId() + ",\n" +
                        "    \"name\": \"" + client.getName() + "\"\n" +
                        "]}\n";
                clientList.append(s);
            }
            response = clientList.toString();
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(200, response.length());
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }

    //Handler для Get getClient
    private class GetClientHandler implements HttpHandler {
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
            int id;

            id = Integer.parseInt(requestData.split("id=")[1]);
            Client client = clientService.read(id);
            String response = "{\n" +
                    "    \"id\": " + client.getId() + ",\n" +
                    "    \"name\": \"" + client.getName() + "\",\n" +
                    "    \"phone\": \"" + client.getPhone() + "\",\n" +
                    "    \"email\": \"" + client.getEmail() + "\"\n" +
                    "}";
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(200, response.length());
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }

    //Handler для POST updateClient
    private class UpdateClientHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            String requestData = null;
            try {
                if ("POST".equals(httpExchange.getRequestMethod())) {
                    requestData = handleRequest(httpExchange);
                    handleResponse(httpExchange, requestData);
                }
            }catch (IOException e){
                System.out.println(e.fillInStackTrace());
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
            String id = requestData.split("\"id\": ")[1].split(",")[0].trim();
            String name = requestData.split("\"name\": \"")[1].split("\",")[0];
            String phone = requestData.split("\"phone\": \"")[1].split("\",")[0];
            String email = requestData.split("\"email\": \"")[1].split("\"\n")[0];
            clientService.update(new Client(name, phone, email), Integer.parseInt(id));
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(200, 0);
            outputStream.close();
        }
    }


}
