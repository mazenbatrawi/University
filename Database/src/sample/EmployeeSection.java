package sample;

public class EmployeeSection {
    private String id, national_id, name, phone, section_id, shift;
    private double salary;
    private String hire_date, password;
    private String section_name;

    public EmployeeSection(){

    }

    public EmployeeSection(String id, String national_id, String name, String phone, String section_id,
                           String shift, double salary, String hire_date, String password, String section_name) {
        this.id = id;
        this.national_id = national_id;
        this.name = name;
        this.phone = phone;
        this.section_id = section_id;
        this.shift = shift;
        this.salary = salary;
        this.hire_date = hire_date;
        this.password = password;
        this.section_name = section_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getHire_date() {
        return hire_date;
    }

    public void setHire_date(String hire_date) {
        this.hire_date = hire_date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }
}