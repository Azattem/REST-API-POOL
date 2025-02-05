package service;

import model.Client;
import model.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface OrderService
{
void create(Order order);
boolean delete(String orderId,int clientId);
List<Order> readAllByDate(String date);
List<Order> readAllByTimeDate(String time,String date);
boolean containsClientIdByDate(int clientId,String date);
}
