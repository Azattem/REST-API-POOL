package model;

public class Order {
private String orderId;
private String date;
private String time;
private int clientId;
    public Order(){}
    public Order(String date,String time,int clientId){
    this.date = date;
    this.time = time;
    this.clientId = clientId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getClientId() {
        return clientId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId+"";
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
