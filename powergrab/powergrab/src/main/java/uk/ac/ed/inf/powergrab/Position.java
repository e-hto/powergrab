package uk.ac.ed.inf.powergrab;
public class Position {

	public double latitude;

	public double longitude;

	
	public Position(double la, double lo) { 
		
		latitude = la;
		
		longitude = lo;
				
		}

	
	public Position nextPosition(Direction direction) { 
		
		double newLatitude = 0;   
		double newLongitude = 0;
		
		double degrees = 0;
				
		switch(direction) {
		
		case N : degrees = 0;       break;    
		case NNE : degrees = 22.5;  break;
		case NE : degrees = 45;     break;
		case ENE : degrees = 67.5;  break;
		case E : degrees = 90;      break;
		case ESE : degrees = 112.5; break;
		case SE : degrees = 135;    break;
		case SSE : degrees = 157.5; break;
		case S	 : degrees = 180;   break;
		case SSW : degrees = 202.5; break;
		case SW : degrees = 225;    break;
		case WSW : degrees = 247.5; break;
		case W	 : degrees = 270;   break;
		case WNW : degrees = 292.5; break;
		case NW : degrees = 315;    break;
		case NNW : degrees = 337.5; break;
		
		
		// Breaks are included for a faster runtime, so that the
		// switch will stop searching once it finds the correct
		// direction to degrees conversion
		}
		
		// Using trigonometry to calculate new position:
		double hypotenuse = 0.0003; // This is how far the drone travels per move
		
		
		// Adjacent is the horizontal distance travelled:
		double adjacent = hypotenuse * Math.cos(Math.toRadians(degrees) ); 
				
		
		// Opposite is the vertical distance travelled:
		double opposite = hypotenuse * Math.sin(Math.toRadians(degrees) );
		
		
		// Adding the horizontal and vertical distances moved on to the current position:
	    newLatitude = latitude + adjacent;  
		newLongitude = longitude + opposite; // Only using + since the distance travelled can be negative 
		
		 
	    return new Position (newLatitude, newLongitude );
		
	}

	public boolean inPlayArea() {

		return ( (latitude < 55.946233 ) && (latitude > 55.942617) 
				
			 && (longitude > -3.192473) && (longitude < -3.184319) );
 				
	}
	
	public String getPosition() {
		
		// Helper function for testing
		
		return( "Latitude: " + latitude + " Longitude: " + longitude);
	}

}

