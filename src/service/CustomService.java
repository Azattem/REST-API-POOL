package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public abstract class CustomService implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange){
        String responseData = "";
        try {
            String requestData;
            if ("GET".equals(httpExchange.getRequestMethod())) {
                requestData = handleGetRequest(httpExchange);
                responseData = handleResponse(httpExchange,requestData);
            }
            if ("POST".equals(httpExchange.getRequestMethod())) {
                requestData = handlePostRequest(httpExchange);
                responseData = handleResponse(httpExchange,requestData);
            }
            httpExchange.sendResponseHeaders(200, responseData.length());
        }catch (IOException e){
            System.out.println("Ошибка считывания данных");
        }
        try {
        OutputStream outputStream = httpExchange.getResponseBody();

        outputStream.write(responseData.getBytes());
        outputStream.flush();
        outputStream.close();
        } catch (IOException e) {
            System.out.println("Ошибка вывода данных");
        }

    }
    public abstract String handleResponse(HttpExchange httpExchange, String requestData) ;
    public abstract String handlePostRequest(HttpExchange httpExchange)throws IOException;
    public abstract String handleGetRequest(HttpExchange httpExchange)throws IOException;

    public String getUri(HttpExchange httpExchange){
    return httpExchange.getRequestURI().toString();
    }
    public String getInput(HttpExchange httpExchange) throws IOException {
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

}
