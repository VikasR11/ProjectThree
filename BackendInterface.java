// --== CS400 File Header Information ==--
// Name: Rohan Putcha
// Email: rputcha@wisc.edu
// Team: JC (Red)
// Role: Backend Developer
// TA: Xinyi Liu
// Lecturer: Gary Dahl
// Notes to Grader: None

public interface BackendInterface {
    AirportInterface[] getAirportShortestPath(AirportInterface start, AirportInterface end);
    boolean addAirport(AirportInterface airport);
    boolean addFlight(AirportInterface source, AirportInterface destination, double distance);
    AirportInterface[] getReachableAirports(AirportInterface origin);
    boolean removeAirport(AirportInterface airport);
    boolean removeFlight(AirportInterface origin, AirportInterface destination);
    double getPriceForAirportPath(AirportInterface start, AirportInterface end);
    int getAirportCount();
    int getFlightCount();
}
