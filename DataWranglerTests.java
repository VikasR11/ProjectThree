// --== CS400 File Header Information ==--
// Name: Arnav Karnik 
// Email: akarnik@wisc.edu
// Team: JC Red
// Role: Data Wrangler
// TA: Xinyi Liu
// Lecturer: Gary Dahl
// Notes to Grader: None

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

/**
 * This class tests the methods from the Airport class
 * @author arnav
 *
 */
class DataWranglerTests {
  private static List<AirportInterface> list; // List that holds all the Airport objects from the CSV
  
  @BeforeEach
  void setup() throws DataFormatException {
    try {
      // creating new readers to set up the call to readDataSet()
      BufferedReader br = new BufferedReader(new FileReader("src/flights.csv"));
      DataReader reader = new DataReader();
      list = reader.readDataSet(br);
    } 
    catch(FileNotFoundException e) {
      System.out.println(e.getMessage());
    } 
    catch(IOException e) {
      System.out.println(e.getMessage());
    }
  }
  
  /**
   * Testing getName() from Airport Class
   */
  @Test
  void testGetAirportName() {
    assertEquals("Greater Rochester International Airport", list.get(1).getName());
  }

  /**
   * Testing getID() from Airport class
   */
  @Test
  void testGetAirportID() {
    assertEquals("ABQ", list.get(4).getID());
  }
  
  /**
   * Testing distances() from Airport class
   */
  @Test
  void testDistances() {
    List<Integer> distances = new ArrayList<Integer>();
    distances.add(8841); distances.add(485); distances.add(8204);
    assertEquals(distances, list.get(8).distances());
  }
  
  /**
   * Testing costs() from Airport class
   */
  @Test
  void testCosts() {
    List<Double> costs = new ArrayList<Double>();
    costs.add(979.81); costs.add(782.95); costs.add(1061.28); costs.add(919.03); costs.add(991.18);
    assertEquals(costs, list.get(12).costs());
  }
  
  /**
   * Testing reachable() from Airport class
   */
  @Test
  void testReachables() {
    List<String> reachables = new ArrayList<String>();
    reachables.add("MSP"); reachables.add("BG01"); reachables.add("DEL");
    assertEquals(reachables, list.get(20).reachables());
  }
}
