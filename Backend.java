// --== CS400 File Header Information ==--
// Name: Rohan Putcha
// Email: rputcha@wisc.edu
// Team: JC (Red)
// Role: Backend Developer
// TA: Xinyi Liu
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.DataFormatException;

/**
 * Backend class handles all airports and flights (and related data), storing them in a directed, weighted
 * graph data structure. The methods of the backend will be used by the frontend to do all the functions that the
 * Flight Tracker application must do. This includes adding/removing airports/flights, finding the shortest 
 * path (by distance) between two airports, getting all reachable airports from a given airport, and 
 * finding the cost of traveling through the shortest path between two airports.
 * 
 * @author Rohan Putcha
 */
public class Backend implements BackendInterface {

    private CS400Graph<AirportInterface> airportSystem;
    private List<AirportInterface> listOfAirports;

    // this hash table will be used to access any airport reference as needed efficiently without any search algorithm.
    // it will be used to convert provided strings (airport ID) into the actual airport object efficiently.
    private Hashtable<String, AirportInterface> stringToAirport;

    /**
     * Constructor instantiates the backend object using a Reader passed as a parameter.
     * The constructor will then extract information from the file in the reader using
     * readDataSet() written by the Data Wrangler. This information is then used to populate
     * a directed, weighted graph that stores all the needed data as the backend of the flight
     * tracker system.
     *
     * @param r - the reader from the front end that facilitates the extraction of the airports
     */
    public Backend(Reader r) {
        // graph storing airport objects is instantiated
        airportSystem = new CS400Graph<>();

        // hash table used to access any airport (with ID) in O(1) is instantiated
        stringToAirport = new Hashtable<>();

        // instantiate DataReader object to get data from Reader object
        DataReaderInterface airportDataReader = new DataReader();

        // Handling exceptions when calling readDataSet
        try {
            listOfAirports = airportDataReader.readDataSet(r);
        } catch (DataFormatException | IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Populating the weighted, directed graph using a private helper method
        populateGraph();
    }

    /** Returns the list of airports that the user must go through in order to go from one airport
     * to another (provided as parameters). This is the shortest path that the user can take to
     * go from one airport to another.
     *
     * @param startAirportID the ID of the starting airport provided as a string
     * @param endAirportID the ID of the destination airport provided as a string
     * @return the list of airports (in order) that the user must go through for the shortest path from one place
     * to another (decided by total distance of path).
     */
    @Override
    public List<AirportInterface> getAirportShortestPath(String startAirportID, String endAirportID) {
        // if the source and destination airports don't exist, throw appropriate exception
        if (!stringToAirport.containsKey(startAirportID) || !stringToAirport.containsKey(endAirportID))
            throw new NoSuchElementException("Provided origin or destination airport do not exist!");
        List<AirportInterface> shortestPath;
        try {
            shortestPath = airportSystem.shortestPath(
                    stringToAirport.get(startAirportID), stringToAirport.get(endAirportID));
        } catch (NoSuchElementException e) {
            // if this exception is thrown, that means that there is no path between the provided airports. It does
            // not mean that one of the vertices does not exist in the graph, because that is handled in the if statement
            // above this try-catch block
            return new ArrayList<>(); // this empty list means there is no flight connection - frontend handles this
        }
        return shortestPath;
    }

    /**
     * Adds a provided airport (created in the frontend by the user) to the flight tracker. If
     * the airport ID already exists in the graph, it will not be added.
     *
     * @param airport the Airport object to be added to the flight tracker
     * @return true if airport was added to the flight tracker, false otherwise
     */
    @Override
    public boolean addAirport(AirportInterface airport) {
        // if the airport ID already exists in the graph, the method returns false and the airport is not added
        if (stringToAirport.containsKey(airport.getID()))
            return false;

        // this boolean is used so that adding to the hash table and list are only done if
        // adding to the graph is done successfully
        boolean added = airportSystem.insertVertex(airport);
        if (added) {
            listOfAirports.add(airport); // add to linked list
            stringToAirport.put(airport.getID(), airport); // add to hashtable
        }
        return added;
    }

    /**
     * Adds a flight to the flight tracker. In regards to the backend, this means that an edge is inserted
     * in the graph to connect airport vertices with a weight representing the distance between the airports.
     *
     * @param sourceID the ID of the source airport
     * @param destinationID the ID of the destination airport
     * @param distance the distance from the source to the destination airport in km as a double
     * @return true if the flight is added to the flight tracker, false otherwise
     */
    @Override
    public boolean addFlight(String sourceID, String destinationID, int distance) {
        // if the source and destination airports don't exist, return false since flight is not added
        if (!stringToAirport.containsKey(sourceID) || !stringToAirport.containsKey(destinationID))
            return false;
        AirportInterface sourceAirport = stringToAirport.get(sourceID);
        AirportInterface destinationAirport = stringToAirport.get(sourceID);

        // inserting an edge returns a boolean, so that will be returned to the frontend as well to be accurate about
        // whether the flight was actually added or not
        return airportSystem.insertEdge(sourceAirport, destinationAirport, distance);
    }

    /**
     * Finds and returns a list of all the reachable airports from the provided origin airport. This includes all
     * airports no matter the cost and distance. If there is no path to an airport, that airport will not be returned
     * in the list of airports.
     *
     * @param origin the origin airport's string ID to find reachable airports
     * @return a list of reachable airports from the provided origin airport
     */
    @Override
    public List<AirportInterface> getReachableAirports(String origin) {
        if (!stringToAirport.containsKey(origin))
            // if the airport does not exist, throw exception
            throw new NoSuchElementException("Provided origin airport does not exist!");
        AirportInterface originAirport = stringToAirport.get(origin);
        ArrayList<AirportInterface> reachableAirports = new ArrayList<>();
        for (AirportInterface airport : listOfAirports) {
            try {
                airportSystem.shortestPath(originAirport, airport);
                // at this point, if the dijkstras path throws a NoSuchElementException, that
                // means that there is no path. Therefore, this airport is not a reachable
                // airport for the origin airport and goes to the catch block
                reachableAirports.add(airport);
            } catch (NoSuchElementException e) {
                // this means that there is no path to 'airport' in the list
                // nothing further has to be done since it is not reachable
            }
        }
        return reachableAirports;
    }

    /**
     * Fully removes a specific airport and any of its connecting flights from the flight tracker.
     *
     * @param airport the airport to be removed from the flight tracker
     * @return true if the airport was removed from the system, false otherwise
     */
    @Override
    public boolean removeAirport(String airport) {
        // if the airport doesn't exist, return false since airport is not removed
        if (!stringToAirport.containsKey(airport))
            return false;

        // this boolean is used so that removal from the hash table and list are only done if the
        // removal from the graph is done successfully
        boolean removed = airportSystem.removeVertex(stringToAirport.get(airport));
        if (removed) {
            listOfAirports.remove(stringToAirport.get(airport)); // remove from linked list
            stringToAirport.remove(airport); // remove from hash table
        }
        return removed;
    }

    /**
     * Fully removes a connecting flight between two airports from the flight tracker.
     *
     * @param origin the string ID of the origin airport (of the flight)
     * @param destination the string ID of the destination (of the flight)
     * @return true if the flight was removed from the system, false otherwise
     */
    @Override
    public boolean removeFlight(String origin, String destination) {
        // if the origin and destination airports don't exist, return false since flight is not removed
        if (!stringToAirport.containsKey(origin) || !stringToAirport.containsKey(destination))
            return false;
        AirportInterface sourceAirport = stringToAirport.get(origin);
        AirportInterface destinationAirport = stringToAirport.get(destination);

        // removing an edge returns a boolean, so that will be returned to the frontend as well to be accurate about
        // whether the flight was actually removed or not
        return airportSystem.removeEdge(stringToAirport.get(origin), stringToAirport.get(destination));
    }

    /**
     * Finds and returns the total price to travel from one airport to another through the shortest path. This is done
     * by getting the shortest path first. Then, the price is found for each flight (edge in the graph) between
     * each airport in the lis that represents the shortest path.
     *
     * @param startAirportID the string ID that represents the starting airport
     * @param endAirportID the string ID that represents the destination airport
     * @return the cost of travelling in the shortest path between the two airports as a double
     */
    @Override
    public double getPriceForAirportPath(String startAirportID, String endAirportID) {
        // if the source and destination airports don't exist, throw appropriate exception
        if (!stringToAirport.containsKey(startAirportID) || !stringToAirport.containsKey(endAirportID))
            throw new NoSuchElementException("Provided origin or destination airport do not exist!");
        List<AirportInterface> shortestPath;
        try {
            shortestPath = airportSystem.shortestPath(
                    stringToAirport.get(startAirportID), stringToAirport.get(endAirportID));
        } catch (NoSuchElementException e) {
            // if this exception is thrown, that means that there is no path between the provided airports. It does
            // not mean that one of the vertices does not exist in the graph, because that is handled in the if statement
            // above this try-catch block
            return -1; // this -1 means there is no flight connection - frontend handles this
        }
        double priceCounter = 0; // this will keep track of the total cost to travel
        for (int i = 0; i < shortestPath.size() - 1; i++) { 
            // this for loop goes through each airport in the list of airports for the calculated shortest path
            List<String> destinationIDs = shortestPath.get(i).reachables(); // holds all destinations from source
            for (int j = 0; j < destinationIDs.size(); j++) {
                // this for loop goes through all possible destinations from each airport in the shortest path
                String destination = destinationIDs.get(j); // holds an individual destination from each airport
                if (destination.equals(shortestPath.get(i + 1).getID())) { 
                    // this if statement is entered if the next airport in the (shortest) path of airports 
                    // is equal to an individual destination. This means the target is found, and the price may
                    // be updated accordingly. Then, since the next airport in the shortest path list has been found,
                    // we may break and repeat this process with the next airport in the list
                    priceCounter += shortestPath.get(i).costs().get(j);
                    break;
                }
            }

        }
        return priceCounter;
    }

    /**
     * Returns the total distance (in km) for the shortest path from one airport to another.
     * This utilizes dijkstra's algorithm to find the shortest distance between two vertices.
     *
     * @param startAirportID the string ID of the starting airport
     * @param endAirportID the string ID of the destination airport
     * @return an integer representing the total distance between the two airports in km.
     * @throws NoSuchElementException if the provided string
     */
    @Override
    public int getDistanceForAirportPath(String startAirportID, String endAirportID) throws NoSuchElementException {
        // if the source and destination airports don't exist, throw appropriate exception
        if (!stringToAirport.containsKey(startAirportID) || !stringToAirport.containsKey(endAirportID))
            throw new NoSuchElementException("Provided origin or destination airport do not exist!");

        int cheapestDistance;
        try {
            cheapestDistance = airportSystem.getPathCost(
                    stringToAirport.get(startAirportID), stringToAirport.get(endAirportID));
        } catch (NoSuchElementException e) {
            // if this exception is thrown, that means that there is no path between the provided airports. It does
            // not mean that one of the vertices does not exist in the graph, because that is handled in the if statement
            // above this try-catch block
            return -1; // this -1 means there is no flight connection - frontend handles this
        }
        return cheapestDistance;
    }

    /**
     * Returns the total number of airports in the flight tracker including those added by the user
     *
     * @return an integer representing the total number of airports in the system.
     */
    @Override
    public int getAirportCount() {
        return airportSystem.getVertexCount();
    }

    /**
     * Returns the total number of flights in the flight tracker including those added by the user
     *
     * @return an integer representing the total number of flights in the system.
     */
    @Override
    public int getFlightCount() {
        return airportSystem.getEdgeCount();
    }

    /**
     * This method is essential to completing the process that the Data Wrangler started with readDataSet.
     * It takes all the data provided as a list of Airport objects and populates the graph data structure.
     * First, each airport is added as a vertex to the graph and added to the class hash table which is
     * used to convert any string airport ID into the actual airport object. Then, each vertex (airport)
     * is connected using the target airports IDs for all the connecting flights (each ID is changed to an airport
     * using the hash table and the flight is added to the graph as an edge).
     */
    private void populateGraph() {
        // start by adding individual vertexes/airports
        for (AirportInterface airport : listOfAirports) {
            airportSystem.insertVertex(airport);
            stringToAirport.put(airport.getID(), airport);
        }

        // connect airports using flight data (this is done separately from the
        // previous step to avoid null pointer exceptions of connecting a non-existent/null airport)
        for (AirportInterface airport : listOfAirports) {
            // for each airport in the list of airports, get the list of origin airports to for loop through
            List<String> destinationIDs = airport.reachables();
            // this for loop goes through each 'flight' for each airport and creates a new edge in the graph for it
            for (int i = 0; i < destinationIDs.size(); i++) {
                airportSystem.insertEdge(airport, stringToAirport.get(destinationIDs.get(i)),
                        airport.distances().get(i));
            }
        }
    }
}
