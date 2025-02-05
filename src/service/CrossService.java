package service;

import model.Order;

import java.util.List;

public interface CrossService {

List<Order> getAllByName(String name,String date);
}
