package service;

import model.Client;
import model.Order;

import java.util.ArrayList;
import java.util.List;

public class CrossServiceImpl implements CrossService{
    private final ClientService clientService;
    private final OrderService orderService;
    public CrossServiceImpl(ClientService clientService1, OrderService orderService1) {
        this.clientService = clientService1;
        this.orderService = orderService1;
    }

    @Override
    public List<Order> getAllByName(String name, String date) {
    List<Client> clientList = clientService.readAllByName(name);
    List<Order> orderList = orderService.readAllByDate(date);
    List<Order> suggestibleClients = new ArrayList<>();
    for (Client client : clientList) {
        for (Order order : orderList) {
        if(client.getId()==order.getClientId()){
            suggestibleClients.add(order);
        }
        }
    }
    return suggestibleClients;
    }
}
