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
 * Interface to be implemented by the Data Wrangler to create an Airport object
 * @author arnav
 *
 */
public interface AirportInterface {

  public String getID();
  public String getName();
  public List<Integer> distances();
  public List<Double> costs();
  public List<String> reachables();
}
