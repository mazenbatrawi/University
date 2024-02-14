package sample;

public class Bill {
    private String id, emp_id, date;
    private double cost;

    public Bill(String id, String emp_id, String date, double cost) {
        this.id = id;
        this.emp_id = emp_id;
        this.date = date;
        this.cost = cost;
    }

    public Bill(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
}
