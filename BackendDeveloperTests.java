// --== CS400 File Header Information ==--
// Name: Rohan Putcha
// Email: rputcha@wisc.edu
// Team: JC (Red)
// Role: Backend Developer
// TA: Xinyi Liu
// Lecturer: Gary Dahl
// Notes to Grader: None

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the functionality of the Backend class. Uses JUnit testing suite to assert expected values and ensure
 * full accuracy of the Backend class.
 *
 * @author Rohan Putcha
 */
public class BackendDeveloperTests {
    BackendInterface backend;

    /**
     * Runs before each test method to create a new backend object that will be tested. It includes a try-catch
     * block to create the needed reader object. This method also demonstrates the fact that the constructor
     * for the backend object is fully functional.
     */
    @BeforeEach
    public void instantiateBackend() {
        FileReader reader;
        try {
            reader = new FileReader("flights.csv");
            backend = new Backend(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the addAirport and addFlight methods of the backend. This is done by creating a new airport and adding
     * it to the backend using addAirport. Then, it is connected to DTW and SFO (existing airports) using addFlight.
     * Finally, we call getReachableAirports to get a list of the reachable airports and ensure that the addition of
     * the new airport into the graph was done properly.
     */
    @Test
    public void testAddAirportAddFlight() {

        // set up data for new airport creation:
        String name  = "Anytown International Airport";
        String airportID = "AIAPT";
        ArrayList<String> reachableAirports = new ArrayList<>();
        reachableAirports.add("DTW"); // added Detroit Airport
        reachableAirports.add("SFO"); // added San Fran Airport
        ArrayList<Integer> distances = new ArrayList<>();
        distances.add(355); // added 255 km distance to Detroit
        distances.add(2342); // added 2342 km distance to San Fran
        ArrayList<Double> costs = new ArrayList<>();
        costs.add(89.99); // added $89.99 as the plane ticket cost to Detroit
        costs.add(432.33); // added $432.33 as the plane ticket cost to San Fran

        // create new airport and flights with these parameters:
        AirportInterface airport = new Airport(name, airportID, reachableAirports, distances, costs);

        // add the airports and flights into the backend system (will be tested):
        backend.addAirport(airport);
        backend.addFlight(airportID, "DTW", distances.get(0));
        backend.addFlight(airportID, "SFO", distances.get(1));

        // now, we know that the new airport, AIAPT, is connected to DTW and SFO. Therefore, AIAPT
        // is now connected to all the reachable airports of DTW and SFO. DTW is connected to 14
        // airports and SFO is connected to none. Therefore, AIAPT will be connected to all of DTW's
        // connections + DTW + SFO. So, there should be 16 distinct reachable airports. We can test these 16:

        List<AirportInterface> connections = backend.getReachableAirports(airportID);
        ArrayList<String> connectionIDs = new ArrayList<>();
        // convert list of airport objects into list of string IDs of airports:
        for (AirportInterface airportObj : connections) {
            connectionIDs.add(airportObj.getID());
        }

        // test the 16 reachable airports by number and values:
        assertEquals("[SHA, YWG, VR10, PI05, XHBU, PVG, SFO, HND, DTW, VIE, TO11," +
                " XIY, WAW, ICT, SVO, ZRH]", connectionIDs.toString());
        assertEquals(16, connectionIDs.size());
    }

    /**
     * Tests the getAirportShortestPath method of the backend class. After physically finding the shortest paths for
     * going from a few airports to others, the method's returned list is compared with manually found solutions. The
     * getAirportShortestPath method returns the list of AirportInterface objects that the shortest path goes through.
     * After converting these objects into just their string IDs, they will be compared with the correct list of airport
     * IDs. This will ensure the accuracy of the getAirportShortestPath method.
     */
    @Test
    public void testGetAirportShortestPath() {
        // we know that the shortest paths between the following are as follows:
        // MAN - ZRH [MAN, SVO, VR10, ZRH]
        // CTS - XIY [CTS, PWM, HYD, XIY]
        // VR10 - ICT [VR10, ICT]
        // we will now test these paths using the shortest path method in the flight tracker
        List<AirportInterface> airportsShortestPath1 = backend.getAirportShortestPath("MAN", "ZRH");
        List<AirportInterface> airportsShortestPath2 = backend.getAirportShortestPath("CTS", "XIY");
        List<AirportInterface> airportsShortestPath3 = backend.getAirportShortestPath("VR10", "ICT");

        // these string lists will hold the list of IDs of the airports that the shortest path goes through
        List<String> stringAirportShortestPath1 = new ArrayList<>();
        List<String> stringAirportShortestPath2 = new ArrayList<>();
        List<String> stringAirportShortestPath3 = new ArrayList<>();

        // these for loops fill the string lists with the appropriate IDs
        for (AirportInterface a : airportsShortestPath1) {
            stringAirportShortestPath1.add(a.getID());
        }
        for (AirportInterface a : airportsShortestPath2) {
            stringAirportShortestPath2.add(a.getID());
        }
        for (AirportInterface a : airportsShortestPath3) {
            stringAirportShortestPath3.add(a.getID());
        }

        assertEquals("[MAN, SVO, VR10, ZRH]", stringAirportShortestPath1.toString());
        assertEquals("[CTS, PWM, HYD, XIY]", stringAirportShortestPath2.toString());
        assertEquals("[VR10, ICT]", stringAirportShortestPath3.toString());

    }

    /**
     * Tests the getPriceForAirportPath and getDistanceForAirportPath methods of the backend class. After physically
     * finding the costs to travel between each airport and the distance covered, this method is used to compare
     * solutions and ensure that the output by the program is accurate.
     */
    @Test
    public void testGetPriceForAirportPathAndGetDistanceForAirportPath() {
        // we know that the prices for the following paths are as follows:
        // MAN - ZRH $2519.65
        // CTS - XIY $2347.50
        // VR10 - ICT $919.03
        //
        // we know that the distances for the following paths are as follows:
        // MAN - ZRH 5024km
        // CTS - XIY 26034km
        // VR10 - ICT 8351km

        // find the price values
        double price1 = backend.getPriceForAirportPath("MAN", "ZRH");
        double price2 = backend.getPriceForAirportPath("CTS", "XIY");
        double price3 = backend.getPriceForAirportPath("VR10", "ICT");

        // find the distances for the paths
        int distance1 = backend.getDistanceForAirportPath("MAN", "ZRH");
        int distance2 = backend.getDistanceForAirportPath("CTS", "XIY");
        int distance3 = backend.getDistanceForAirportPath("VR10", "ICT");

        // assert price values as expected
        assertEquals(2519.65, price1);
        assertEquals(2347.5, price2);
        assertEquals(919.03, price3);

        // assert distance values as expected
        assertEquals(5024, distance1);
        assertEquals(26034, distance2);
        assertEquals(8351, distance3);
    }

    /**
     * Tests the accurate population of the graph and consequently getFlightCount and getAirportCount methods. These
     * methods should initially output 138 and 304 respectively. After adding an airport and a flight, they should
     * change accordingly by 1.
     */
    @Test
    public void testFlightAndAirportCount() {

        // initial values:
        assertEquals(138, backend.getAirportCount());
        assertEquals(304, backend.getFlightCount());

        // add an airport and a flight:

        // set up data for new airport creation:
        String name  = "Anytown International Airport";
        String airportID = "AIAPT";
        ArrayList<String> reachableAirports = new ArrayList<>();
        reachableAirports.add("DTW"); // added Detroit Airport
        ArrayList<Integer> distances = new ArrayList<>();
        distances.add(355); // added 255 km distance to Detroit
        ArrayList<Double> costs = new ArrayList<>();
        costs.add(89.99); // added $89.99 as the plane ticket cost to Detroit

        // create new airport and flights with these parameters:
        AirportInterface airport = new Airport(name, airportID, reachableAirports, distances, costs);

        // add the airports and flights
        backend.addAirport(airport);
        backend.addFlight(airportID, "DTW", distances.get(0));

        // final values (should increase to 139 and 305:
        assertEquals(139, backend.getAirportCount());
        assertEquals(305, backend.getFlightCount());
    }

}
