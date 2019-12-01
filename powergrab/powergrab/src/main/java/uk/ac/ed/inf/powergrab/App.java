package uk.ac.ed.inf.powergrab;
import java.io.IOException;
import java.net.*;
import java.util.Random;

/**
 * Hello world!
 * Give a minus bias for negatives at a position and plus for positives
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	// Sort input:
    	String day = args[0]; String month = args[1]; String year = args[2];
    	String latitude = args[3]; String longitude = args[4];
    	String seed = args[5];
    	String droneVersion = args[6];
    	System.out.println("Drone type: " + droneVersion + "\nStarting at Latitude: " + latitude + " Longitude: " + longitude );
    	
        //Receiving map from server:
    	String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/" + year + "/" + month + "/" + day + "/powergrabmap.geojson";
    	URL mapUrl = new URL(mapString);
    	URLConnection conn = mapUrl.openConnection();
    	conn.setReadTimeout(10000); // milliseconds
    	conn.setConnectTimeout(15000); // milliseconds
    	//conn.setRequestMethod("GET");
    	conn.setDoInput(true);
    	conn.connect(); // Starts the query
    	
    	//Set up the drone conditions:
    	Position drone = new Position(Double.parseDouble(latitude), Double.parseDouble(longitude));
    	Random random = new Random(Integer.parseInt(seed)); // Initialise random number generator from seed
        Direction directions[] = Direction.values(); // Create an Array of possible directions to randomly select from
        //End of setup
        
        
        
        // Choosing a direction:
        
        int min = 0; int max = 15;
        Direction chosenDirection = directions[ random.nextInt( (max - min) + 1) + min ]; 
        drone = drone.nextPosition(chosenDirection);
        System.out.println("Randomly chosen direction: " + chosenDirection);
        System.out.println("New position is " + drone.getPosition());
        
        // Searching for local stations:
        
        
        
        
    	

    	
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    	//Output:
    	
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
