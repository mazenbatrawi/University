package sample;

public class Product {
    private String barcode, name, company_id, section_id;
    private double price;
    private int total_quantity;
    private String production_date, expire_date;
    private double selling_price;

    public Product(String barcode, String name, String company_id, String section_id, double price, int total_quantity,
                   String production_date, String expire_date, double selling_price) {
        this.barcode = barcode;
        this.name = name;
        this.company_id = company_id;
        this.section_id = section_id;
        this.price = price;
        this.total_quantity = total_quantity;
        this.production_date = production_date;
        this.expire_date = expire_date;
        this.selling_price = selling_price;
    }

    public Product(){

    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(int total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getProduction_date() {
        return production_date;
    }

    public void setProduction_date(String production_date) {
        this.production_date = production_date;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public double getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }
}
