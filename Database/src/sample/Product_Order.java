package sample;

import java.util.Date;

public class Product_Order {
    private String order_id, product_id;
    private Date production_date, expire_date;
    private int quantity;
    private double cost;

    public Product_Order(String order_id, String product_id, Date production_date, Date expire_date, int quantity, double cost) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.production_date = production_date;
        this.expire_date = expire_date;
        this.quantity = quantity;
        this.cost = cost;
    }

    public Date getProduction_date() {
        return production_date;
    }

    public void setProduction_date(Date production_date) {
        this.production_date = production_date;
    }

    public Date getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(Date expire_date) {
        this.expire_date = expire_date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
