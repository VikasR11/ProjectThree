// --== CS400 File Header Information ==--
// Name: Jacopo Franciosi
// Email: jfranciosi@wisc.edu
// Team: JC/Red
// Role: Frontend
// TA: Xinyi
// Lecturer: Gary D.
// Notes to Grader: N/A

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The Frontend class connects the backend implementation to the user through commands. The user may add Pokemon
 * and search (using various criteria) for Pokemon, accessing the backend through Frontend commands in this class.
 *
 * @author Jacopo Franciosi
 */
public class Frontend {

    public BufferedReader inputFile; // refers to the variable holding the input file for the backend instantiation. It is public so that the test suite can access it.
    private Scanner scanner; // the scanner used throughout the program maintaining the same input buffer

    /**
     * Constructor takes no arguments and instantiates a scanner to be used in prompts and
     * the starting ID for all new Pokemon. The constructor is essential in providing the path for the CSV
     * file where all existing pokemon data is accessed. This should always be run first in order to use
     * frontendObj.inputFile to use as a parameter in creating a Backend object. An example of this is in
     * the main method of the application.
     */
    public Frontend() {
        BufferedReader filein = null;
        try {
            FileReader reader = new FileReader("src/flights.csv"); // path to csv
            filein = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        inputFile = filein;
        scanner = new Scanner(System.in);
    }

    /**
     * Main method within the frontend which instantiates the frontend object and runs it.
     *
     * @param args input arguments when running the program
     */
    public static void main(String [] args) {
        Frontend frontend = new Frontend();
        frontend.run(new Backend(frontend.inputFile));
    }

    /**
     * The run method takes a backend object as a parameter to run the program with. This method simply
     * prints the welcome message and calls the first state of the application.
     *
     * @param backend the backend object that the application is based upon
     */
    public void run(BackendInterface backend) {
        System.out.println("Welcome to the Flight Tracker!");
        this.mainScreen(backend);
    }

    /**
     * This is the main menu where all options can be accessed. From here, the program may enter 3 other states:
     * add mode, search mode, exit program screen. Depending on user input, these different states may be accessed.
     * There is a while loop that is continuously run so that other states can simply return and the application
     * will proceed to run again and again without needing inefficient recursion (very large call stacks).
     *
     * @param backend the backend object that is the backbone of the application is not accessed here but is passed
     *                along to other methods for use
     */
    public void mainScreen(BackendInterface backend) {
        while (true) {
            // while loop runs while program runs
            System.out.println("\nMain screen");
            System.out.println("-----------");
            System.out.println("You can add your airport, delete an existing one, find the shortest"
                    + " path between 2 airports,\nfind reachable airports from another airport, "
                    + "and find out the ticket price between 2 airports!");
            System.out.println("\nWhat would you like to do?");
            System.out.println("\t1) Enter 'a' to go to add mode");
            System.out.println("\t2) Enter 'd' to go to delete mode");
            System.out.println("\t2) Enter 'f' to find the shortest path");
            System.out.println("\t3) Enter 'g' to find reachable airports from an airport");
            System.out.println("\t4) Enter 'y' to display the current existing airports!");
            System.out.println("\t5) Enter 'x' to exit the program");
            System.out.println();

            String command = scanner.next();
            if (command.equals("x"))
                break;
            else if (command.equals("a"))
                addAirport(backend);
            else if (command.equals("d"))
                deleteAirport(backend);
            else if (command.equals("f"))
                findShortestPath(backend);
            else if (command.equals("g"))
                findReachableAirports(backend);
            else if (command.equals("y"))
                backend.getGraph();
        }
        // once while loop exits (x is entered), call end screen
        endScreen();
    }

    /**
     * The program enters add mode when 'a' is entered from the main screen. Here, the user is able to create
     * a new airport by providing all the needed information to add to the backend/graph. The user is prompted for required
     * fields in order to create an airport object. Entering 'x' at any time will exit the method.
     *
     * @param backend the backend object's addAirport method is used to add the new airport which was created
     *                by combining all the given attributes into the constructor
     */
    public void addAirport(BackendInterface backend) {
        System.out.println("\nAdd Mode");
        System.out.println("--------");
        System.out.println("You will now add information for the aiport to be added."
                + " Type 'x' at any point to exit add mode.\n");
        // Add an Airport's name, abbreviation, list of reachable airports, distance of reachable
        // airports, and ticket price.
        System.out.println("Enter Airport's abbreviation: ");
        String abbr = scanner.next();
        if (abbr.equals("x"))
            return;
        
        scanner.useDelimiter(System.lineSeparator());
        System.out.println("\nEnter Airport's name: ");
        String name = scanner.next();
        if (name.equals("x"))
            return;

        System.out.println("\nEnter Airport's list of reachable airports (by abbreviation)"
                + " like so: SFO,TUL,YWG");
        String reachAirports = scanner.next();
        reachAirports = reachAirports.replaceAll(" ","");
        String[] arrAirports = reachAirports.split(",");
        List<String> arrAirportsList = Arrays.asList(arrAirports);
        if (reachAirports.equals("x"))
            return;

        System.out.print("\nEnter Airport's list of distances(km) for each reachable airports like so"
                + "(RESPECTIVELY): 25546,3454,985\n");
        String reachDist = scanner.next();
        reachDist = reachDist.replaceAll(" ","");
        String[] arrDist = reachDist.split(",");
        Integer[] arrDistInt = new Integer[arrDist.length];
        for (int i = 0; i < arrDistInt.length; i++) {
            arrDistInt[i] = Integer.parseInt(arrDist[i]);
        }
        List<Integer> arrDistIntList = Arrays.asList(arrDistInt);
        if (reachDist.equals("x"))
            return;

        System.out.print("\nEnter Airport's list of ticket prices for each reachable airports like"
                + "so(RESPECTIVELY): 432.00,566.44,199.54\n");
        String reachPrice = scanner.next();
        reachPrice = reachPrice.replaceAll(" ","");
        String[] arrPrice = reachPrice.split(",");
        Double[] arrPriceDouble = new Double[arrPrice.length];
        for (int i = 0; i < arrPriceDouble.length; i++) {
            arrPriceDouble[i] = Double.parseDouble(arrPrice[i]);
        }
        List<Double> arrPriceDoubleList = Arrays.asList(arrPriceDouble);
        if (reachDist.equals("x"))
            return;


        // Use airport constructor to create a new airport out of given attributes:
        AirportInterface airport = new Airport(name, abbr, arrAirportsList, arrDistIntList, arrPriceDoubleList);
        // Add airport to graph, and check if it was successfully added:
        boolean added = backend.addAirport(airport);
        if(!added) { System.out.println("***Unable to add airport!***\n\t1) Check that your inputted list of "
                + "reachable airports in abbreviated form (ATL) all exist!\n\t2) Check that your airport"
                + " has not already been added!"); }
        else {
            System.out.println("***Added Airport Successfully!***");
        }


        // Now the user is asked to connect flights to the new airport
        // User is asked if they want to add flights arriving to the new airport
        scanner.reset();
        boolean goodIn = true;
        while (goodIn) {
            System.out.print("\nFor any reachable airport entered: " + "\"" + reachAirports + "\"" + ". Would"
                    + " you like to add a flight going TO the new airport? (y/n)");
            String ans = scanner.next();
            if (ans.equals("x"))
                return;
            if (ans.equalsIgnoreCase("y")) {
                for (int i = 0; i < arrAirports.length; i++) {
                    System.out.println("Do you want to add a departing flight from " + arrAirports[i] +"?(y/n)");
                    ans = scanner.next();
                    if (ans.equals("x"))
                        return;
                    if (ans.equalsIgnoreCase("y")) {
                        backend.addFlight(arrAirports[i], abbr, arrDistInt[i]);
                        System.out.println("======ADDED======");
                    }else {
                        //guarantees the while loop runs if its not yes.
                    }
                    System.out.println();
                }
                System.out.println(" --- Completed (1/2) --- \n");
                goodIn = false;
            }else if (ans.equalsIgnoreCase("n")) {
                System.out.println(" --- Completed (1/2) --- \n");
                goodIn = false;
            }else {
                if (goodIn)System.out.println("Enter a valid command!");
            }
        }

        // User is asked if they want to add flights arriving from the new airport
        goodIn = true;
        while (goodIn) {
            System.out.print("For any reachable airport entered: " + "\"" + reachAirports + "\"" + ". Would you"
                    + " you like to add a flight arriving from the new airport? (y/n)");
            String ans = scanner.next();
            if (ans.equals("x"))
                return;
            if (ans.equalsIgnoreCase("y")) {
                for (int i = 0; i < arrAirports.length; i++) {
                    System.out.println("Do you want to add an arrival flight to " + arrAirports[i] +"?(y/n)");
                    ans = scanner.next();
                    if (ans.equals("x"))
                        return;
                    if (ans.equalsIgnoreCase("y")) {
                        backend.addFlight(abbr, arrAirports[i], arrDistInt[i]);
                        System.out.println("======ADDED======");
                    }else {
                        //guarantees the while loop runs if its not yes.
                    }
                    System.out.println();
                }
                System.out.println(" --- Completed (2/2) --- \n");
                goodIn = false;
            }else if (ans.equalsIgnoreCase("n")) {
                System.out.println(" --- Completed (2/2) --- \n");
                goodIn = false;
            }else {
                if (goodIn)System.out.println("Enter a valid command!");
            }
        }


    }

    /**
     * The program enters delete mode when 'd' is entered from the main screen. Here, the user is able to delete
     * an airport by providing the airportID. The user is prompted for this field. Entering 'x' at any time will exit the method.
     *
     * @param backend the backend object's deleteAirport method is used to add the new airport which was created
     *                by combining all the given attributes into the constructor.
     */
    public void deleteAirport(BackendInterface backend) {
        System.out.println("\nDelete Mode");
        System.out.println("--------");
        System.out.println("You will now enter information for the aiport to be deleted."
                + " Type 'x' at any point to exit delete mode.");
        // Enter the airport's ID to be deleted
        System.out.print("\nEnter Airport's abbreviation to be deleted: ");
        String abbr = scanner.next();
        if (abbr.equals("x"))
            return;
        boolean removed = backend.removeAirport(abbr);
        if(!removed) System.out.println("Airport was unable to be removed. Possible issue:"
                + " Check ID spelling");
    }

    /**
     * The program enters findShortestPath mode when 'f' is entered. Here, the user is able to
     * retrieve the shortest path between 2 airports. Entering 'x' at any time will exit the method.
     *
     * @param backend the backend object is used to access the shortest paths for aiport objects
     */
    public void findShortestPath(BackendInterface backend) {
        while (true) {
            // while loop runs so that this mode is only exited using 'x'
            System.out.println("\nSearch for a Shortest Path");
            System.out.println("-----------");
            System.out.println("You will be able to search for the shortest path between 2 "
                    + "airports (Type 'x' at any point to exit this mode):");
            System.out.println();
            System.out.println("Enter the airport ID of the departure airport:");

            // Asks user for departure and arrival airports
            String departure = scanner.next();
            if (departure.equals("x"))
                return;
            System.out.println("Enter the airport ID of the arrival airport:");
            String arrival = scanner.next();
            if (arrival.equals("x"))
                return;

            // Retrieves the shortest path from inputted airports
            // if user enters inexistent IDs, exception is thrown & caught
            List<AirportInterface> shortestPathList;
            try {
                shortestPathList = backend.getAirportShortestPath(departure, arrival);
            }catch (NoSuchElementException e) {
                System.out.println("Error: Invalid airport ID(s) submitted"); return;
            }
            // Some airports have no flights that can connect them (really rare)
            if(shortestPathList.size() == 0) {
                System.out.println("Error: Unable to find shortest path between airports. Check that"
                        + " a reachable path exists from the departure airport "
                        + "(see \"Getting Reachable Airport\" Mode)");
                return;
            } else {
                // If there's no error retrieving shortest paths, the airports are displayed
                System.out.println("----------------------------------");
                System.out.println("Shortest path from " + departure + " to " + arrival + ":");
                String conc = "Start:";
                int dist = 0;
                for (AirportInterface air: shortestPathList) {
                    conc += air.getID() + " -> ";
                    dist += backend.getDistanceForAirportPath(departure, arrival);
                }
                System.out.println(conc.substring(0,conc.length()-4));
                System.out.println("Total Distance: " + dist + "km");
            }
        }
    }

    /**
     * The program enters findReachableAirports mode when 'g' is entered. Here, the user is able to
     * find the reachable airports from an airport of their choice. Entering 'x' at any time will exit the method.
     *
     * @param backend the backend object is used to access is used to access the reachable airports for this airport object
     */
    public void findReachableAirports(BackendInterface backend) {
        while (true) {
            // while loop runs so that this mode is only exited using 'x'
            System.out.println("\nSearch for reachable airports");
            System.out.println("-----------");
            System.out.println("You will be able to search reachable airports from any airport of choice"
                    + " (Type 'x' at any point to exit this mode):");
            System.out.println();
            System.out.println("Enter the airport ID of the airport to look through and find "
                    + "reachable airports:");

            // Asks user for departure and arrival airports
            String departure = scanner.next();
            if (departure.equals("x"))
                return;

            // Retrieves the shortest path from inputted airports
            // if user enters inexistent IDs, exception is thrown & caught
            List<AirportInterface> ReachableAirportList;
            try {
                ReachableAirportList = backend.getReachableAirports(departure);
            }catch (NoSuchElementException e) {
                System.out.println("Error: Invalid airport ID submitted"); return;
            }
            if(ReachableAirportList.size() == 0) {
                System.out.println("No airports can be reached from " + departure);
            } else {
                // If there's no error retrieving shortest paths, the airports are displayed
                System.out.println("----------------------------------");
                System.out.println("Reachable airports from " + departure + ": ");
                String conc = "";
                int counter = 1;
                for (AirportInterface air: ReachableAirportList) {
                    conc += counter + ") ID: " + air.getID() + " Distance: " +
                            backend.getDistanceForAirportPath(departure, air.getID()) + "km\n";
                    counter++;
                }
                System.out.println(conc);
                return;
            }
        }
    }


    /**
     * This method is used to display a list of pokemon in a visually pleasing manner
     *
     * @param pokemon the list of pokemon that are to be displayed as search results
     */
    /*
    public void displayPokemon(List<PokemonInterface> pokemon) {
        System.out.println("----------------------------------");
        System.out.println("Pokemon matching search results:");
        String builder;
        // iterate through the pokemon in the list (based on whatever parameter) and print info
        for (PokemonInterface p : pokemon) {
            builder = "ID#"+p.getID() + "\t" + p.getName();
            builder = builder + "\n\tCP:\t"+ p.getCP();
            builder = builder + "\n\tRegion: " + p.getRegion();
            builder = builder + "\n\tType(s): " + p.getTypes()[0];
            if (p.getTypes()[1] != null) // if the second type exists, add it to the output
                builder = builder + "\t" + p.getTypes()[1];
            System.out.println(builder);
            System.out.println();
        }
        System.out.println("---------------------------------");
    }
    */


    /**
     * After the program has finished executing (x is entered from the main screen), the
     * program will call this method to display the thank you message and end the program.
     */
    public void endScreen() {
        System.out.println("\nThank you for using Flight Tracker!");
    }
}
