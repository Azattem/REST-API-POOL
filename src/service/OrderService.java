package service;

import model.Order;

import java.util.List;

public interface OrderService
{
void create(Order order);
boolean delete(String orderId,int clientId);
List<Order> readAllByDate(String date);
List<Order> readAllByTimeDate(String time,String date);
boolean containsClientIdByDate(int clientId,String date);
}
