package A;
import java.util.Date;
public class Car {
	private String plateNo;
	private int yearManufacture;
	private int monthManufacture;
	private String color;
	private double price;
	private String manufactureBy;
	private int guaranteeDueYear;
	private int guaranteeDueMonth;
	// Below we have two constructors.
	//This one is a no argument constructor with the default values given.
	public Car() {
		Date a = new Date();
		this.plateNo = "0123-A".toUpperCase();
		this.yearManufacture = a.getYear()+1900;
		this.monthManufacture = a.getMonth()+1;
		this.color = "red".toUpperCase();
		this.price = 50000.00;
		this.manufactureBy = "Mercedes".toUpperCase();
		this.guaranteeDueYear = a.getYear()+1900;
		this.guaranteeDueMonth = a.getMonth()+1+6;
		//Checking if months exceeded 12
		if(this.guaranteeDueMonth>12) {
			this.guaranteeDueYear+=this.guaranteeDueMonth/12;
			this.guaranteeDueMonth%=12;
		}
	}
	//This is a constructor with 8 arguments.
	public Car(String plateNo, int yearManufacture, int monthManufacture, String color, double price,
			String manufactureBy, int guaranteeDueYear, int guaranteeDueMonth) {

		this.plateNo = plateNo.toUpperCase();
		this.yearManufacture = yearManufacture;
		this.monthManufacture = monthManufacture;
		this.color = color.toUpperCase();
		this.price = price;
		this.manufactureBy = manufactureBy.toUpperCase();
		this.guaranteeDueYear = guaranteeDueYear;
		this.guaranteeDueMonth = guaranteeDueMonth;
	}
	//We have 9 setters and getters here.
	public int getYearManufacture() {
		return yearManufacture;
	}

	public void setYearManufacture(int year) {
		this.yearManufacture = year;
	}

	public int getMonthManufacture() {
		return monthManufacture;
	}

	public void setMonthManufacture(int month) {
		this.monthManufacture = month;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public double getPrice() {
		return price;
	}

	public String getManufactureBy() {
		return manufactureBy;
	}

	public void setGuarantee(int month, int year) {
		this.guaranteeDueMonth = month;
		this.guaranteeDueYear = year;
	}
	
	//Calculating the age.
	public String getCarAge() {
		// Calculating the age in years and months.
		Date d = new Date();
		int currentYear = d.getYear() + 1900;
		int ageYears = currentYear - yearManufacture;
		int currentMonth = d.getMonth()+1;
		int ageMonths = currentMonth - monthManufacture;
		
		// Correction for age in months if the car didn't complete the whole cycle year.
		if(ageMonths<0) {
			ageYears-=1;
			ageMonths+=12;
		}
		
		//Converting the values to string, and when printed, they will be upper-cased.
		String months = String.valueOf(ageMonths);
		String years = String.valueOf(ageYears);
		
		// Checking if the car is manufactured or not.
		if(ageYears<0) {
			return "notManufactured"; // Returning "notManufactured" here is because when we calculate the guarantee, if it is not
			//manufactured, then we will print the expected information for the car from the moment of
			//manufacturing.
		}
		else {
			return "\nThe car's age is: "+years+" Years and "+months+" Months.";
		}
	}	
	
	public String getCalculateGuarantee() {
		//Calculating the due years and months left(grYear, grMonth).
		Date x = new Date();
		int crntYear = x.getYear()+1900;
		int grYear = guaranteeDueYear - crntYear;
		int manMonth = x.getMonth()+1;
		int grMonth = guaranteeDueMonth - manMonth;
		
		// Correcting the due years and months left if the months are less than zero.
		if(grMonth<0) {
			grMonth = 12 - (grMonth*-1);
			grYear-=1;
		} 
		
		// Checking if the car isn't manufactured yet.
		boolean ok=false;
		if(getCarAge()=="notManufactured") {
			// Changing the left time to the difference between the manufacture and the manufacture.
			grYear = guaranteeDueYear - yearManufacture;
			grMonth = guaranteeDueMonth - monthManufacture;
			//Correcting the months if they went under zero.
			if(grMonth<0) {
				grMonth = 12 - (grMonth*-1);
				grYear-=1;
			}
			ok=true;
		}
		
		// Converting values to strings.
		String monthsLeft = String.valueOf(grMonth);
		String yearsLeft = String.valueOf(grYear);
		
		// Returning the last case (The car isn't manufactured)
		if(ok) {
			return "\nThe car's guarantee is expected to end in "+yearsLeft+" years and "+monthsLeft+
					" months from the manufacture year";
		}
		
		// If the car is manufactured and the guarantee expired.
		else if(grYear<0) {
			return "\nThe car's guarantee has expired.";
		}
		
		// Guarantee left (with no errors).
		else {
			return "\nThe car's guarantee ends in: "+ yearsLeft +" years and " + monthsLeft + " months.";
		}
	}	
	
	public String printCarInfo() {
		//If the months are above 12 or below 1, we display an error message.
		if(monthManufacture>12 || monthManufacture<1) {
			return "The car with plate "+plateNo+" has an invalid input of manufacture month, please enter valid data.\n";
		}
		
		//If years are below zero, we display an error message
		if(yearManufacture<0) {
			return "The car with plate "+plateNo+" has an invalid input of manufacture year, please enter valid data.\n";
		}
		
		//If the months are above 12 or below 1, we display an error message.
		if(guaranteeDueMonth<1 || guaranteeDueMonth>12) {
			return "The car with plate "+plateNo+" has an invalid input of guarantee month, please enter valid data.\n";
		}
		
		//If years are below zero, we display an error message
		if(guaranteeDueYear<0) {
			return "The car with plate "+plateNo+" has an invalid input of guarantee year, please enter valid data.\n";
		}
		if(guaranteeDueYear<yearManufacture || (guaranteeDueYear==yearManufacture && guaranteeDueMonth<monthManufacture)) {
			return "The car with plate "+plateNo+" has an guarantee due date before the manufacture date, please check your data.\n";
		}
		if(price<0) {
			return "The car with plate "+plateNo+" has a negative price, please check your data.\n";
		}
		
		//If the car is not manufactured yet.
		if(getCarAge()=="notManufactured") {
			return "The plate number is expected to be: " +plateNo + 
					"\nThe year of manufacture is expected to be in: " +yearManufacture +
					"\nThe month of manufacture is expected to be in: " +monthManufacture +
					"\nThe car's color is expected to be: " +color +
					"\nThe car's price is expected to be: " +price +
					"\nThe car is expected to be manufactured by: " +manufactureBy +
					"\nThe car's guarantee year is expected to be due to: " +guaranteeDueYear +
					"\nThe car's guarantee month is expected to be due to: " +guaranteeDueMonth 
					+ getCalculateGuarantee() + "\n";
		}
		
		// If the car is manufactured.
		else {
			return "The car's plate number is: " +plateNo + 
					"\nThe year of manufacture is: " +yearManufacture +
					"\nThe month of manufacture is: " +monthManufacture +
					"\nThe car's color is: " +color +
					"\nThe car's price is: " +price +
					"\nThe car is manufactured by: " +manufactureBy +
					"\nThe car's guarantee year is due to: " +guaranteeDueYear +
					"\nThe car's guarantee month is due to: " +guaranteeDueMonth +
					getCarAge() + getCalculateGuarantee() + "\n";
		}
	}	
}
