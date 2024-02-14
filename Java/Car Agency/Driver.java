package A;

import java.io.*;
import java.util.*;

public class Driver {

    public static void main(String[] args) {
        // Defining the array list that we will store the information from the file in it.
        ArrayList <Vehicles> vehicles = new ArrayList<>();

        // Calling the function that will display the menu after every operation.
        displayMenu(vehicles);
    }
    public static void displayMenu(ArrayList<Vehicles> vehicles) {
        // This is the menu that the user will see at the beginning and after every operation.
        Scanner inp = new Scanner(System.in);
        System.out.println("""

                1. Read the data about objects from the file “inputdata.txt” and store them in Arraylist.
                2. Set prices of petroleum.
                3. Print sorted order Vehicles in an ascending order based on costFor100Km.
                4. Print sorted order Vehicles in an ascending order based on owner name.
                5. Print sorted order Vehicles in an descending order based on vehicle brand.
                6. Clone Vehicle without owner.
                7. Turn air-condition on
                8. Write Output on the “output.txt” file after sorting them
                9. Exit from System
                Enter your choice:\s""");
        int n = -1;
        // In this try catch, the user is asked to enter a number between 1 and 9 as displayed in the menu.
        // If not, an error will be displayed.
        try {
            String checkChoice = inp.nextLine();
            n = Integer.parseInt(checkChoice);
            if(n < 1 || n > 9){
                System.out.println("The number must be between 1 and 9");
                displayMenu(vehicles);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        // This a switch for the number selected by the user if it is in the correct format, every case calls the function
        // that is specified for it.
        switch (n) {
            case 1 -> readData(vehicles);
            case 2 -> setPetroleumPrices(vehicles);
            case 3, 8 -> printDataCostBased(vehicles, true);

            case 4 -> printDataNameBased(vehicles, true);

            case 5 ->  printDataBrandBased(vehicles, true);

            case 6 -> {
                try {
                    cloneVehicle(vehicles);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            case 7 -> changeAirConditionStatus(vehicles);

            case 9 -> {
                printStatistics(vehicles);
            }

        }
    }
    // This is the function that reads the data from the file
    public static void readData(ArrayList<Vehicles> vehicles){
        Scanner read = null;
        // The try catch is for checking if the file exists.
        try {
            read = new Scanner(new FileReader("inputdata.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // While the file has a new line of input, it will be read by the reader.
        while (read.hasNextLine()) {
            String line = read.nextLine(); // Reading the whole line.
            StringBuilder sb = new StringBuilder(line);
            String temp = sb.toString();
            String[] tokens = temp.split(","); // Splitting the line to words every time we find the char ',' in it.

            if (tokens[0].equalsIgnoreCase("car")) { // If it is not car it checks the others.
                if(tokens.length != 9){ // The form of data my project accepts for vehicle of type car must have 9 words only.
                    throw new IllegalArgumentException("Error in car with model number " + tokens[2].trim() + ", check your data");
                }
                // Adding the data to the array list after saving it in the tokens array.
                Owner temp2 = new Owner(tokens[4].trim(), "A", "A", "a", new GregorianCalendar(2013,7,15));
                Car temp1 = new Car(tokens[1].trim(), tokens[2].trim(), tokens[3].trim(), tokens[5].trim(),
                        Double.parseDouble(tokens[6].trim()), Double.parseDouble(tokens[7].trim()),
                        temp2, Integer.parseInt(tokens[8].trim()));

                vehicles.add(temp1);
            }
            else if(tokens[0].equalsIgnoreCase("truck")) { // If it is not truck it checks the others.
                if(tokens.length != 10){ // The form of data my project accepts for vehicle of type truck must have 10 words only.
                    throw new IllegalArgumentException("Error in truck with model number " + tokens[2] + ", check your data");
                }
                // Adding the data to the array list after saving it in the tokens array.
                Owner temp2 = new Owner(tokens[4].trim(), "A", "A", "a", new GregorianCalendar(2015,7,20));
                Truck temp1 = new Truck(tokens[1].trim(), tokens[2].trim(), tokens[3].trim(), tokens[5].trim(),
                        Double.parseDouble(tokens[6].trim()), Double.parseDouble(tokens[7].trim()), temp2,
                        Integer.parseInt(tokens[9].trim()), Integer.parseInt(tokens[8].trim()));
                vehicles.add(temp1);
            }
            else if(tokens[0].equalsIgnoreCase("minivan")){ // If it is here and it is not minivan it throws an exception.
                // The form of data my project accepts for vehicle of type minivan must have 10 words only.
                if(tokens.length != 10 || (!tokens[9].trim().equals("true") && !tokens[9].trim().equals("false"))){
                    throw new IllegalArgumentException("Error in minivan with model number " + tokens[2] + ", check your data");
                }
                // Adding the data to the array list after saving it in the tokens array.
                Owner temp2 = new Owner(tokens[4].trim(), "A", "A", "a", new GregorianCalendar());
                Minivan temp1 = new Minivan(tokens[1], tokens[2].trim(), tokens[3].trim(), tokens[5].trim(),
                        Double.parseDouble(tokens[6].trim()), Double.parseDouble(tokens[7].trim()), temp2,
                        Integer.parseInt(tokens[8].trim()), Boolean.parseBoolean(tokens[9].trim()));
                vehicles.add(temp1);
            }
            // If it is not car or truck or minivan, it displays a exception.
            else{
                throw new IllegalArgumentException("Error, a field may not be a vehicle, check your data");
            }
        }
        // If it is here, the reading operation was done successfully, it calls the function responsible of displaying the menu
        System.out.println("\nSuccess!");
        displayMenu(vehicles);
    }
    // This is the function that sets new prices for the petroleum.
    public static void setPetroleumPrices(ArrayList<Vehicles> vehicles){
        // The user enters either 1 or 2 or 3, otherwise, it is displayed again.
        // Where 1 is for diesel, 2 is for gasoline, 3 is for both.
        PetroleumType o = new PetroleumType();
        System.out.println(o.toString());
        Scanner inp = new Scanner(System.in);
        int x = inp.nextInt();
        // If the value is equal to the final integer of diesel, it changes the diesel price.
        if(x == PetroleumType.DIESEL) {
            System.out.println("Enter the new price of diesel: ");
            double diesel = inp.nextDouble();
            PetroleumType.setDieselPrice(diesel);
        }
        // If the value is equal to the final integer of gasoline, it changes the gasoline price.
        else if(x == PetroleumType.GASOLINE){
            System.out.println("Enter the new price of gasoline: ");
            double gasoline = inp.nextDouble();
            PetroleumType.setGasolinePrice(gasoline);
        }
        // If it is 3, which is the sum of the final integers of gasoline and diesel, it changes both of them.
        else if(x == PetroleumType.GASOLINE + PetroleumType.DIESEL){
            System.out.println("Enter the new price of diesel: ");
            double diesel = inp.nextDouble();
            PetroleumType.setDieselPrice(diesel);
            System.out.println("Enter the new price of gasoline: ");
            double gasoline = inp.nextDouble();
            PetroleumType.setGasolinePrice(gasoline);
        }
        // Otherwise, error is displayed and calls the function again.
        else{
            System.out.println("Invalid selection");
            setPetroleumPrices(vehicles);
        }
        System.out.println("The new prices are set successfully !");
        // As always, if the procedure is done successfully, it displays the menu again.
        displayMenu(vehicles);
    }
    public static void printDataInFile(Vehicles o, ArrayList<Vehicles> vehicles, String s) {
        // This is the function that is responsible of printing the data in the output file.
        try {
            FileWriter writer = new FileWriter("output.txt", true);
            PrintWriter outFile = new PrintWriter(writer);
            outFile.println(s);
            // We have some special cases, if the string was coming from the function that clones a vehicle, it does a certain operation.
            if(s.equalsIgnoreCase("\nCloned Vehicle: ")){
                // Printing the cloned vehicle only.
                outFile.println(o);
                outFile.print("**************************************\n");
                outFile.close();
                // As always, if the procedure is done successfully, it displays the menu again.
                System.out.println("\nSuccess!");
                displayMenu(vehicles);
            }
            if(s.equalsIgnoreCase("\nThe data after sorting them depending on the name of the brand: ")){
                // If the function was called from the brand sorting function, it will have the above string, and prints the data
                // in descending order based on brand name.
                for(int i = vehicles.size() - 1; i >= 0; i--){
                    outFile.println(vehicles.get(i));
                }
                // Specifying what were the petroleum prices when the data was printed
                outFile.println("\nThe Diesel price was: " + PetroleumType.getDieselPrice() + "\nThe Gasoline price was: " + PetroleumType.getGasolinePrice());
                outFile.print("**************************************\n");
                outFile.close();
                // As always, if the procedure is done successfully, it displays the menu again.
                System.out.println("\nSuccess!");
                displayMenu(vehicles);
            }
            // It will reach this far in the function if it came from sorting by cost or owner's name.
            for (Vehicles vehicle : vehicles) {
                outFile.println(vehicle);
            }
            outFile.println("\nThe Diesel price was: " + PetroleumType.getDieselPrice() + "\nThe Gasoline price was: " + PetroleumType.getGasolinePrice());
            outFile.print("**************************************\n");
            outFile.close();
            // As always, if the procedure is done successfully, it displays the menu again.
            System.out.println("\nSuccess!");
            displayMenu(vehicles);
            // Exception if the output file is not found.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // This is the function that sorts the data by cost using the interface comparable using the function compareTo implemented
    // in vehicle class.
    // The boolean specifies whether the function was called to print(True) or to re-calculate the data(False) after turning the condition on.
    public static void printDataCostBased(ArrayList<Vehicles> vehicles ,boolean ok){
        // The sorting happens here.
        Collections.sort(vehicles);
        if(ok){
            String s = "\nThe Data after sorting them depending on the cost for 100 Km: ";
            printDataInFile(null, vehicles, s);
        }
        System.out.println("\nSuccess!");
        displayMenu(vehicles);
    }
    // This is the function that sorts the data by the name of the owner using the bubble sort method
    // The boolean specifies whether the function was called to print(True) or to re-calculate the data(False) after turning the condition on.
    public static void printDataNameBased(ArrayList<Vehicles> vehicles, boolean ok){
        for(int i = 0; i < vehicles.size(); i++){
            for(int j = 0; j < vehicles.size(); j++){
                if(vehicles.get(i).getOwner().getName().compareToIgnoreCase(vehicles.get(j).getOwner().getName()) < 1){
                    Vehicles temp = vehicles.get(i);
                    vehicles.set(i,vehicles.get(j));
                    vehicles.set(j,temp);
                }
            }
        }
        if(ok){
            String s = "\nThe data after sorting them depending on the name of the owner: ";
            printDataInFile(null, vehicles, s);
        }
    }
    // This is the function that sorts the data by the brand of the vehicle using the bubble sort method
    // The boolean specifies whether the function was called to print(True) or to re-calculate the data(False) after turning the condition on.
    public static void printDataBrandBased(ArrayList<Vehicles> vehicles, boolean ok){
        for(int i = 0; i < vehicles.size(); i++){
            for(int j = 0; j < vehicles.size(); j++){
                if(vehicles.get(i).getBrand().compareToIgnoreCase(vehicles.get(j).getBrand()) < 1){
                    Vehicles temp = vehicles.get(i);
                    vehicles.set(i,vehicles.get(j));
                    vehicles.set(j,temp);
                }
            }
        }
        if(ok){
            String s = "\nThe data after sorting them depending on the name of the brand: ";
            printDataInFile(null, vehicles, s);
        }
        else{
            displayMenu(vehicles);
        }
    }
    // This function uses deep cloning to clone a vehicle selected by the user, by calling the method overridden in the
    // vehicles class.
    public static void cloneVehicle(ArrayList<Vehicles> vehicles) throws CloneNotSupportedException{
        try {
            System.out.println("Select a Vehicle to clone: ");
            for (int i = 0; i < vehicles.size(); i++) {
                System.out.println(i + 1 + "- " + vehicles.get(i).toString());
            }
            Scanner inp = new Scanner(System.in);
            int x = inp.nextInt();
            // In general, the function throws an exception if the clone is not supported, and throws an exception here
            // if the user selects a vehicle that doesn't exist.
            if (x < 1 || x > vehicles.size()) {
                System.out.println("Invalid selection");
                cloneVehicle(vehicles);
            }
            --x;
            String s = "\nCloned Vehicle: ";
            System.out.println("\nThe owner whose vehicle will be cloned has the following information: " + vehicles.get(x).owner.toString());
            Vehicles clonedVehicle = (Vehicles) (vehicles.get(x)).clone();
            printDataInFile(clonedVehicle, vehicles, s);
        }
        catch (CloneNotSupportedException ex){
            ex.printStackTrace();
        }
    }
    // Method to set the condition for all vehicles on or off.
    public static void changeAirConditionStatus(ArrayList <Vehicles> vehicles){
        System.out.println("\nEnter 1 to set the condition on or 2 to set it off");
        Scanner inp = new Scanner(System.in);
        int x = inp.nextInt();
        if(x != 1 && x != 2){
            System.out.println("Invalid selection.");
            changeAirConditionStatus(vehicles);
        }
        if(x == 1){
            for (Vehicles value : vehicles) {
                value.setAirConditionOn();
            }
        }
        else{
            for (Vehicles value : vehicles) {
                value.setAirConditionOff();
            }
        }
        System.out.println("Success!");
        printDataCostBased(vehicles, false);
        

    }
    // The method that is responsible of printing the statistics while doing the operations.
    public static void printStatistics(ArrayList <Vehicles> vehicles){
        if(!vehicles.isEmpty()) {
            System.out.println("Here are some interesting statistics from the data: ");
            System.out.println(Truck.powerAverage());
            System.out.println(Minivan.hasAutoPercentage());
            System.out.println(PetroleumType.getMinMaxPrices());

            PetroleumType temp = new PetroleumType();
            double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
            String maxName = "", minName = "";

            for (Vehicles vehicle : vehicles) {
                if (vehicle.costFor100Km(temp) > max) {
                    max = vehicle.costFor100Km(temp);
                    maxName = vehicle.owner.getName();
                }
                if (vehicle.costFor100Km(temp) < min) {
                    min = vehicle.costFor100Km(temp);
                    minName = vehicle.owner.getName();
                }
            }
            System.out.println("\nThe owner " + maxName + " spends the most on his vehicle with cost for 100 km: " + max + " NIS");
            System.out.println("The owner " + minName + " spends the least on his vehicle with cost for 100 km: " + min + " NIS");
            System.out.println("GoodBye!");

            System.exit(1);
        }
    }
}



