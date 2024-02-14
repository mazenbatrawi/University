package A;

import java.util.GregorianCalendar;

// Owner class (implements Cloneable to copy a vehicle without the owner).
public class Owner implements Cloneable{
    // Attributes of the class
    private  String name, registrationNo, address, tel;
    private GregorianCalendar dateOfRegistration;

    // No arg. constructor.
    public Owner(){

    }
    // Arg. constructor
    public Owner(String name, String registrationNo, String address, String tel, GregorianCalendar dateOfRegistration) {
        this.name = name;
        this.registrationNo = registrationNo;
        this.address = address;
        this.tel = tel;
        this.dateOfRegistration = dateOfRegistration;
    }

    // To complete the process of cloning, we need a clone method here.
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // Setters and getters
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public GregorianCalendar getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(GregorianCalendar dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    @Override
    public String toString() {
        return "\nOwner's name: " + getName()
                + "\nRegistration number: " + getRegistrationNo()
                + "\nAddress: " + getAddress()
                + "\nTelephone number: " + getTel()
                + "\nDate of registration: " + getDateOfRegistration().getTime();
    }
}
