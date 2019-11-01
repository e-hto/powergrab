package uk.ac.ed.inf.powergrab;
import java.io.IOException;
import java.net.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	// Input:
    	
    	String day = args[0]; String month = args[1]; String year = args[2];
    	
    	String latitude = args[3]; String longitude = args[4];
    	
    	String seed = args[5];
    	
    	String droneVersion = args[6];

    	
    	// First step: get the map from server like in the slides
    	
    	String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/" 
    			
    					+ year + "/" + month + "/" + day + "/powergrabmap.geojson";
    	
    	URL mapUrl = new URL(mapString);
    	
    	//Connecting to URL:
    	
    	URLConnection conn = mapUrl.openConnection();
    	conn.setReadTimeout(10000); // milliseconds
    	conn.setConnectTimeout(15000); // milliseconds
    	//conn.setRequestMethod("GET");
    	conn.setDoInput(true);
    	conn.connect(); // Starts the query
    	
    	
    	
    	
    	
    	//System.out.println(mapUrl); mapUrl Works!
    	
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
}
