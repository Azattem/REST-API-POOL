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
boolean delete(Order order,int clientId);
List<Order> readAllByDate(String date);
List<Order> readAllByTimeDate(String time,String date);
List<Order> readAllByClientId(int clientId);
}
