// --== CS400 File Header Information ==--
// Name: Arnav Karnik 
// Email: akarnik@wisc.edu
// Team: JC Red
// Role: Data Wrangler
// TA: Xinyi Liu
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.util.List;

public interface AirportInterface {

  public String getFlightWeight(String node1, String node2);
  public String getAirport();
  public List<String> flightPath(String dest1, String dest2);
  public boolean addFlight(String flight);
  public void setFlight(String flight);
}
