// --== CS400 File Header Information ==--
// Name: Rohan Putcha
// Email: rputcha@wisc.edu
// Team: JC (Red)
// Role: Backend Developer
// TA: Xinyi Liu
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.util.NoSuchElementException;

public interface BackendInterface {
    AirportInterface[] getAirportShortestPath(String startAirportID, String endAirportID) throws NoSuchElementException;
    boolean addAirport(AirportInterface airport);
    boolean addFlight(String sourceID, String destinationID, double distance);
    AirportInterface[] getReachableAirports(String origin) throws NoSuchElementException;
    boolean removeAirport(String airportID);
    boolean removeFlight(String origin, String destinationID);
    double getPriceForAirportPath(String startAirportID, String endAirportID) throws NoSuchElementException;
    int getAirportCount();
    int getFlightCount();
}
