package A;
public class Driver {
	public static void main(String[] args)
	   {
		   //Declaring array and initializing the values
	       Car [] cars = new Car[5];
	       cars[0] = new Car();
	       cars[1] = new Car ("a",2016,2,"Blue",2000,"Mercedes",2023,2);
	       cars[2] = new Car ("3",2016,12,"White",3000,"Toyota",2024,12);
	       cars[3] = new Car ("4",2022,5,"Black",4000,"Kia",2025,4);
	       cars[4] = new Car ("x",2015,12,"Yellow",5000,"Mitsubishi",2016,6);
	       //Calling the function that we will use to print the cars information
	       printCarsInfo(cars);
	       //Calling the function that will return the car object with the maximum price
	       System.out.println("The car with max price has the following information:\n");
	       System.out.println(maxPrice(cars).printCarInfo());
	   }
	
	public static void printCarsInfo(Car [] cars) {
		for(int i=0;i<cars.length;i++) {
			//Printing the cars info using the method declared in class Car
			System.out.println(cars[i].printCarInfo());
			System.out.println("**********************************************************************************************\n");
		}
	}
	
	public static Car maxPrice(Car[]cars) {
		double maximumPrice = cars[0].getPrice(); // Assuming the first price is the the maximum
		int indexSaver = 0; // The index that we want to return for the car with maximum price
		int i;
		for(i=1;i<cars.length - 1;i++) {
			if(cars[i].getPrice()>maximumPrice) { // If we find a price higher than the current one
				maximumPrice = cars[i].getPrice(); // We change it here
				indexSaver=i; // And save it's index here
			}
		}
		return cars[indexSaver]; // Then we return it here
	}
}
