// --== CS400 File Header Information ==--
// Name: Arnav Karnik 
// Email: akarnik@wisc.edu
// Team: JC Red
// Role: Data Wrangler
// TA: Xinyi Liu
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

/**
 * Interface to be implemented by the Data Wrangler. It contains only method called readDataSet()
 * @author arnav
 *
 */
public interface DataReaderInterface {
  public List<AirportInterface> readDataSet(File inputFileReader) throws IOException,
  DataFormatException;
}
