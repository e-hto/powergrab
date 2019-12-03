package uk.ac.ed.inf.powergrab;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.google.gson.JsonElement;
//import com.mapbox.geojson.*;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Geometry;

/**
 * Hello world!
 * Give a minus bias for negatives at a position and plus for positives
 * Also assign a priority bias for fuel based on current fuel level
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {   //INITIAL SETUP:************
    	// ************************
    	// Sort input:
    	String day = args[0]; String month = args[1]; String year = args[2];
    	String latitude = args[3]; String longitude = args[4];
    	String seed 
    	= args[5];
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
    	InputStream mapSource = conn.getInputStream(); 
    	Scanner s = new Scanner(mapSource).useDelimiter("\\A"); //Convert from inputStream to String
    	String map = s.hasNext() ? s.next() : ""; // Final map
    	//System.out.println(map);
    	FeatureCollection fc = FeatureCollection.fromJson(map);
    	
    	//Set up the drone conditions:
    	Position startPosition = new Position(Double.parseDouble(latitude), Double.parseDouble(longitude));
    	Drone drone = new Drone(startPosition);
    	Random random = new Random(Integer.parseInt(seed)); // Initialise random number generator from seed
        Direction directions[] = Direction.values(); // Create an Array of possible directions to randomly select from
        //END OF INITIAL SETUP ***************        
        //************************************
        
        
        
        
        // CLOSE FEATURES SETUP:

       HashSet<Feature> closeFeatures = new HashSet(); // Keep this list updated every 5 moves with close features
       int moves = 0;
       while( drone.power > 0) {
        	  
            //*******************************************
            if ( moves / 5 == 0) { //Update close features every 5 moves
              for (Feature f : fc.features()) {
        	
            		Double distance = calculateDistance(drone, f);
            
            		if ( distance <= 0.0015 ) {
            			
            			closeFeatures.add(f);}
                                                 }
                                  } 
            //*************Every 5 moves***************
            //*****************************************              
            
                 
                    //MOVE SELECTION:
                 
                 HashSet<Feature> inRangeFeatures = new HashSet();
                 for (Feature f : closeFeatures) {
                	  
                	 Double distance = calculateDistance(drone, f);
        	   
                     if (distance <= 0.0003 + 0.00025) {//Search for features within moving distance 0.0003 + absorbing distance 0.00025 
                    	
                    	 //Get coins:
                    	Double coins = Double.parseDouble(f.getProperty("coins").getAsString());
                    	System.out.println("In range with coins: " + coins );
                    	
                         //Get power:
                    	Double power = Double.parseDouble(f.getProperty("power").getAsString());
                    	System.out.println("and power: " + power );
                    	
                		//System.out.println("Feature found in range: " + f);
                		inRangeFeatures.add(f); //Only add if inRangeFeatures doesn't already contain f: change to using a set instead
                		
                		
                                              }
                 	}
                 
                 
                     Feature maxScoringF = null;
                     Double maxScore = 0.0;
                 for (Feature f : inRangeFeatures) {
                	 
                	 
                	 Double coins = Double.parseDouble(f.getProperty("coins").getAsString());
                	 Double power = Double.parseDouble(f.getProperty("power").getAsString());
                	 
                	 
                	 // Give each inRangeFeature a score
                	 // Save the f with max score as list progresses
                	 
                	 Double powerMultiplier = 250.0/drone.power; // Drone will prioritise high power stations when it is running low
                	 Double score  = coins + (power * powerMultiplier); // Test and edit this to boost powerMultiplier to balance with coins
                	 
                	 if ( score > maxScore ) {
                		 
                		 maxScore = score;
                		 maxScoringF = f;
                		 //System.out.println("MAX: " + maxScoringF);
                		 
                	 }
                    }
                 
                 
                 //If  maxScoringF exists, move to it

                        if ( maxScoringF != null ) {
                    	   
                       
                        moveTo( drone, directionToFeature(drone, maxScoringF) ) ; 
                        } else {
                       moveRandom(drone, directions, random); // If no good moves are found, move at random
                        }
                      //Move completed!
                 		moves++;

    }
       
    System.out.println("Run out of fuel!"); //Finished
    }

    
    
    public static double calculateDistance(Drone drone, Feature f){
    	
    	Double dLa = drone.position.latitude;
		Double dLo = drone.position.longitude;
    	
    	Double fLa = ((Point)f.geometry()).coordinates().get(1);
    	Double fLo = ((Point)f.geometry()).coordinates().get(0);

		Double x1 = dLa; Double y1 = dLo; Double x2 = fLa; Double y2 = fLo;
		Double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));;
    	
        return distance;
    }
    public static void moveTo(Drone drone, Direction direction) {
    	
    	drone.position = drone.position.nextPosition(direction);
    	drone.power = drone.power - 1.25;
    	System.out.println("Selected max scoring direction: " + direction);
        System.out.println("New position is " + drone.position.getPosition());
    	
    }

 	public static void moveRandom(Drone drone, Direction[] directions, Random random) { //Always stays within bounds
 		
        int min = 0; int max = 15; // Bounds for possible directions
        Boolean nextMoveInBounds = false;
        
        while ( !nextMoveInBounds )  { 
       // If the randomly chosen next move doesn't stay inside play area, keep picking another until it does
       
        Direction randomDirection = directions[ random.nextInt( (max - min) + 1) + min ]; // Random direction from seed
        Position nextPosition = drone.position.nextPosition( randomDirection );
        
        if (nextPosition.inPlayArea()) {
        	
        nextMoveInBounds = true;	
        drone.position = nextPosition;
        drone.power = drone.power - 1.25; 
        System.out.println("Randomly chosen direction: " + randomDirection);
        System.out.println("New position is " + drone.position.getPosition());
        }
        
 	}
        

 	}
 	
 	 public static Direction directionToFeature(Drone drone, Feature f) {
 		 
 		 Double dLa = drone.position.latitude;
 		 Double dLo  = drone.position.longitude;
 		 
 		 Double fLa = ((Point)f.geometry()).coordinates().get(1);
 		 Double fLo = ((Point)f.geometry()).coordinates().get(0);
 		 
 		 Double x1 = dLa; Double y1 = dLo; Double x2 = fLa; Double y2 = fLo;
 		
 		 Double theta = Math.atan2(x1 - y1 , x2 - y2);
 		 
 		 //Now find the direction to travel which will lead to the feature:
 		 if ( (theta > 348.75 && theta <= 360) || (theta < 11.25 && theta >= 0) ) {
 			 
 			 return Direction.N;
 			 
 		 }
 		 if ( theta >= 11.25 && theta < 33.75) {
 			 
 			 return Direction.NNE;
 		 }
 		 if ( theta >= 33.75 && theta < 56.25) {
			 
			 return Direction.NE;
		 }
 		 if ( theta >= 56.25 && theta < 78.75) {
			 
			 return Direction.ENE;
		 }
 		 if ( theta >= 78.75 && theta < 111.25) {
			 
 			return Direction.E;
		 }
 		 if ( theta >= 111.25 && theta < 123.75) {
			 
 			return Direction.ESE;
		 }
 		 if (theta >= 123.75 && theta < 146.25) {
 			 
 			return Direction.SE;
 		 }
 		 if (theta >= 146.25 && theta < 168.75) {
 			 
 			 return Direction.SSE;
 		 }
 		 if (theta >= 168.75 && theta < 191.25) {
 			 
 			 return Direction.S;
 		 }	
 	     if ( theta >= 191.25 && theta < 213.75) {
 	 			 
 	 	     return Direction.SSW;
 	    }
 		 if ( theta >= 213.75 && theta < 236.25) {
			 
 			return Direction.SW;
		 }
 		 if (theta >= 236.25 && theta < 258.75) {
 			 
 			return Direction.WSW;
 		 }
 		 if (theta >= 258 && theta < 281.25) {
 			 
 			 return Direction.W;
 		 }
 		 if (theta >= 281.25 && theta < 303.75) {
 			 
 			 return Direction.WNW;
 		 }	
 	     if ( theta >= 303.75 && theta < 326.25) {
 	 			 
 	 	     return Direction.NW;
 	     }
 	     if ( theta >= 326.25 && theta < 348.75) {
 	    	 
 	    	 return Direction.NNW;
 	     }
 	 
 		 return Direction.N;
 	 }
}

// NOTES: ***************************************************

//TO DO:
// Calculate degrees from drone to feature method
// After every move, absorb the nearby stations


//What to do with station after visiting:
//For example, if a user with 35.5 coins and 15.0 power comes within 0.00025 degrees of a charging
//station with −10.2 coins and −20.8 power then afterwards the user will have 25.3 coins and 0.0 power
//whereas the charging station will have 0.0 coins and −5.8 power

//  0.0015 Is the farthest the drone could travel in 5 moves
//  0.0003 distance travelled every move
// //Absorbing distance is 0.00025

//Ideas: 
//Iterate through entire list of features then rank them from closest to farthest??? prob not
//Every 5 moves or so, iterate through all feature and add those fairly close (maybe no farther than could be travelled
//within 5 moves) to a 'local station shortlist'
//Consult the shortlist to see which of the local features are in range of one move and which station is best
//
//Every feature has ( f.geometry() ).coordinates() ; and 
// getAsString( f.getProperty("coins")) ; // Coins at f - String
// getAsFloat( f.getProperty("coins") ) ; // Coins at f - Float
	
//Distance testing: 
//Double Tx1 = Double.parseDouble(latitude); Double Ty1 = Double.parseDouble(longitude); // Starting position
//Double Tx2 = drone.latitude; Double Ty2 = drone.longitude; // Final position
//Double distanceTest = Math.sqrt((Ty2 - Ty1) * (Ty2 - Ty1) + (Tx2 - Tx1) * (Tx2 - Tx1)); // Distance travelled
//System.out.println(distanceTest);        

//Final Output:

//'dronetype-DD-MM-YYYY.txt' dronetype is 'stateful' or 'stateless'

/*It contains each move of the drone in terms of the latitude and longitude
of the drone before the move, the direction it chose to move, the latitude and longitude of the drone
after the move, and the values of the coins and power for the drone after the move.

Ex:

55.944425,-3.188396,SSE,55.944147836140246,-3.1882811949702905,0.0,248.75 */

//'dronetype-DD-MM-YYYY.geojson'

// Copy of the map 

//Get power: (Old method)
//JsonElement p = f.getProperty("power");
//String powerString = p.getAsString();
//Double power = Double.parseDouble(powerString);
//System.out.println("In range with power: " + powerString );
    
//************************************************************
 


