package sample;

public class Bill_Product {
    private String bill_id, product_id, product_name;
    private int quantity;
    private double cost;

    public Bill_Product(String bill_id, String product_id, int quantity, double cost, String product_name) {
        this.bill_id = bill_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.cost = cost;
        this.product_name = product_name;
    }

    public Bill_Product() {

    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
