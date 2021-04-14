// --== CS400 File Header Information ==--
// Name: Arnav Karnik 
// Email: akarnik@wisc.edu
// Team: JC Red
// Role: Data Wrangler
// TA: Xinyi Liu
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.util.List;

/**
 * Class to implement an Airport object
 * @author arnav
 *
 */
public class Airport implements AirportInterface {
  private String name;
  private String ID;
  private List<String> reachables;
  private List<Integer> distances;
  private List<Double> costs;
  
  /**
   * Constructor to initialize an Airport object
   * @param name
   * @param nameID
   * @param reachables
   * @param dist
   * @param costs
   */
  public Airport(String name, String nameID, List<String> reachables, List<Integer> dist, List<Double> costs) {
    this.name = name;
    this.ID = nameID;
    this.reachables = reachables;
    this.distances = dist;
    this.costs = costs;
  }

  /**
   * @return String - id of the airport
   */
  public String getID() {
    // TODO Auto-generated method stub
    return this.ID;
  }

  /**
   * @return String - the name of the airport
   */
  public String getName() {
    // TODO Auto-generated method stub
    return this.name;
  }

  /**
   * @return List<Integer> - list of distances from an origin airport
   */
  public List<Integer> distances() {
    // TODO Auto-generated method stub
    return this.distances;
  }

  /**
   * @return List<Double> - list of costs from an origin airport
   */
  public List<Double> costs() {
    // TODO Auto-generated method stub
    return this.costs;
  }

  /**
   * @return List<String> - list of all the destination airport ids from the origin airport
   */
  public List<String> reachables() {
    // TODO Auto-generated method stub
    return this.reachables;
  }
  
  
  
}
