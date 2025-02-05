package service;

import model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

//Затычка для базы данных Заказов
public class OrderServiceImpl implements OrderService{
    private static final AtomicInteger ORDER_ID_HOLDER = new AtomicInteger();
    private static final Map<String,Order> ORDER_MAP = new HashMap<>();
    @Override
    public void create(Order order) {
    order.setOrderId(ORDER_ID_HOLDER.getAndIncrement());
    ORDER_MAP.put(order.getOrderId(),order);
    }

    @Override
    public boolean delete(String orderId, int clientId) {
        if(ORDER_MAP.containsKey(orderId)){
            if(ORDER_MAP.get(orderId).getClientId()==clientId){
                ORDER_MAP.remove(orderId);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Order> readAllByDate(String date) {
        List<Order> list = new ArrayList<>();
        for (Order order : ORDER_MAP.values()) {
        if(order.getDate().equals(date)){
        list.add(order);
        }
        }
        return list;
    }

    @Override
    public List<Order> readAllByTimeDate(String time, String date) {
        List<Order> list = new ArrayList<>();
        for (Order order : ORDER_MAP.values()) {
            if(order.getDate().equals(date)&&order.getTime().equals(time)){
                list.add(order);
            }
        }
        return list;
    }

    @Override
    public boolean containsClientIdByDate(int clientId,String date) {
        List<Order> list = readAllByDate(date);
        for (Order order : list) {
            if(order.getClientId()==clientId){
                return true;
            }
        }
        return false;
    }

}
