package sample;

import java.util.Date;

public class Order {
    private String id, employee_id, company_id, expenses_id;
    private Date date;
    private double cost;

    public Order(String id, String employee_id, String company_id, String expenses_id, Date date, double cost, Company company) {
        this.id = id;
        this.employee_id = employee_id;
        this.company_id = company_id;
        this.expenses_id = expenses_id;
        this.date = date;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getExpenses_id() {
        return expenses_id;
    }

    public void setExpenses_id(String expenses_id) {
        this.expenses_id = expenses_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
