package uk.ac.ed.inf.powergrab;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Hello world!
 * Geo-json maps: http://homepages.inf.ed.ac.uk/stg/powergrab/
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	// Classifying input arguments:
    	
    	String day = args[0]; String month = args[1]; String year = args[2];
    	
    	String latitude = args[3]; String longitude = args[4];
    	
    	String seed = args[5];
    	
    	String droneVersion = args[6];

    	
    		// First step: get the map from server like in the slides
    	
    	String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/" 
    			
    					+ year + "/" + month + "/" + day + "/powergrabmap.geojson";
    	//For testing:
    	//System.out.println(mapString); 
    	
    	URL mapUrl = new URL(mapString);
    	
    		// Second step connecting to URL to retrive map:
    	
    	URLConnection conn = mapUrl.openConnection();
    	conn.setReadTimeout(10000); // milliseconds
    	conn.setConnectTimeout(15000); // milliseconds
    	//conn.setRequestMethod("GET");
    	conn.setDoInput(true);
    	conn.connect(); // Starts the query
     	
    	InputStream stream = conn.getInputStream();
    	
    	String mapSource = streamTostring(stream);
 
    	//For testing: 
    	//System.out.println(mapSource);
    	
    	
    		// Third step :initialising the random seed
    	
    	//private java.util.Random rnd;
    	Random rnd = new Random(Integer.parseInt(seed));	
    	
    	//Testing the consistency of the seed output (1 to 16 for each direction)
    	//for(int i = 0 ; i<10 ; i++) // in method
    		//System.out.print(rnd.nextInt(16) +" ");
    	
    	
    	//Output files:
    	
    	//'dronetype-DD-MM-YYYY.txt' dronetype is 'stateful' or 'stateless'
    	
    	/*It contains each move of the drone in terms of the latitude and longitude
    	of the drone before the move, the direction it chose to move, the latitude and longitude of the drone
    	after the move, and the values of the coins and power for the drone after the move.
    	
    	Ex:
    	
    	55.944425,-3.188396,SSE,55.944147836140246,-3.1882811949702905,0.0,248.75 */
    	
    	//'dronetype-DD-MM-YYYY.geojson'
    	
    	// Copy of the map 
    	}
    
    static String streamTostring(java.io.InputStream is) {
        java.util.Scanner scanner = new java.util.Scanner(is);
		java.util.Scanner s = scanner.useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    
  
}