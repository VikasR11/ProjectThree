// --== CS400 File Header Information ==--
// Name: Arnav Karnik 
// Email: akarnik@wisc.edu
// Team: JC Red
// Role: Data Wrangler
// TA: Xinyi Liu
// Lecturer: Gary Dahl
// Notes to Grader: None


import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.zip.DataFormatException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Class that implements DataReaderInterface. It has only one method, which is critical to functionality 
 * of the entire program. 
 * 
 * @author arnav
 *
 */
public class DataReader implements DataReaderInterface {

  @Override
  public ArrayList<AirportInterface> readDataSet(Reader inputFileReader)
      throws IOException, DataFormatException {
    Hashtable<String, AirportInterface> hash = new Hashtable<String, AirportInterface>();
    if(inputFileReader == null) {
      throw new IOException();
    }
    Scanner scan = new Scanner(inputFileReader); // scanning the CSV file
    String temp = scan.nextLine(); // next line of the CSV
    if(temp != null) {
      temp = scan.nextLine();
      while(scan.hasNextLine()) {
        // stores Airport attribute names into an array of Strings
        String[] header = temp.split(",");
        
        String airportName = header[17]; // name of the airport
        String destinationID = header[3]; // id of the destination
        double cost = Double.parseDouble(header[0]); // cost of the flight
        int distance = (int)Double.parseDouble(header[9]); // distance of the flight
        if(!hash.containsKey(airportName)) {
          ArrayList<String> tempDestID = new ArrayList<String>(); tempDestID.add(destinationID);
          ArrayList<Double> costs = new ArrayList<Double>(); costs.add(cost);
          ArrayList<Integer> distances = new ArrayList<Integer>(); distances.add(distance);
          hash.put(airportName, new Airport(airportName, header[18], tempDestID, distances, costs));
        } else {
          hash.get(airportName).costs().add(cost);
          hash.get(airportName).distances().add(distance);
          hash.get(airportName).reachables().add(destinationID);
        }
      }
    }
    ArrayList<AirportInterface> listAirports = new ArrayList<AirportInterface>(hash.values());
    return listAirports;
  }
}
