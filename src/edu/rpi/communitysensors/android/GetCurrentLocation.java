package edu.rpi.communitysensors.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

//This class is meant to handle GPS coordinates.
//This class is implemented based on information from:
//http://stackoverflow.com/questions/4870667/how-can-i-use-getsystemservice-in-a-non-activity-class-locationmanager
public class GetCurrentLocation {
	private LocationManager locationManager;
	private LocationListener listener;
	private Context mContext;
	private static final long minDistance = 1; //in meters
	private static final long minTime = 10000; //in milliseconds
	
	public GetCurrentLocation(Context c) {
		mContext = c;
	}
	
	//This function gets the location and returns a location value
	//@return - current location value in location format
	public Location getLocation(){		
		locationManager=(LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		listener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, listener);
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		return location;
	}
	
	public void stopLocationUpdate(){
		locationManager.removeUpdates(listener);
	}
	
	//This class takes in a location and returns latitude as a double
	//@param location - obtain current location
	//@return - return current latitude
	public double getLatitude(Location location){
		double lat;
		if (location != null) {
			lat = location.getLatitude();
		}
		else {
			lat = 42.729700; 
		}
		return lat;
	}
	
	//This class takes in a location and returns longitude as a double
	//@param location - obtain current location
	//@return - return current longitude
	public double getLongitude(Location location){
		double lon;
		if (location != null) {
			lon = location.getLongitude();
		}
		else {
			lon = -73.680196;
		}
		return lon;
	}
	
	//This function returns the GPS distance and bearing of two different points
	//This android function is based off of the Vincenty formulation of distance
	//http://www.movable-type.co.uk/scripts/latlong-vincenty.html
	//@param start - start location
	//@param destination - end location
	//@param data - float of size 3 {distance (meters), bearing_start (degrees), bearing_end (degrees)}
	public void twoPointData(Location start, Location destination, float[] data) {
		float[] d = {-1, -1, -1};
		//Check to make sure there is nothing wrong with the location values
		if (start == null || destination == null) {
			Toast.makeText(mContext, "Error with GPS provider.", Toast.LENGTH_SHORT).show();			
			data = d;
			return;
		}
		else {
			Location.distanceBetween(start.getLatitude(), start.getLongitude(), destination.getLatitude(), destination.getLongitude(), data);
			return;
		}
	}
	
	//This function returns a string value of the direction in state and actual direction parameter
	//@param direction_in_degrees - value of direction in degrees obtained from twoPointData (Vincenty formulation)
	//								North is at 0 degrees, West at -90, East at 90 degrees
	//@return - string array containing state and string direction (e.g. {0,N} or {1,NE})
	public String[] getDirection(float direction_in_degrees) {
		String[] data = {"9","-1"}; 
		if(direction_in_degrees <= 22.5 && direction_in_degrees > -22.5) {
			data[0] = "0"; data[1] = "N"; // Direction is North
		}
		else if (direction_in_degrees <= 67.5 && direction_in_degrees > 22.5 ) {
			data[0] = "1"; data[1] = "NE"; // Direction is Northeast
		}
		else if (direction_in_degrees <= 112.5 && direction_in_degrees > 67.5) {
			data[0] = "2"; data[1] = "E"; // Direction is East
		}
		else if(direction_in_degrees <= 157.5 && direction_in_degrees > 112.5) {
			data[0] = "3"; data[1] = "SE"; // Direction is Southeast
		}
		else if(direction_in_degrees <= -157.5 || direction_in_degrees > 157.5) {
			data[0] = "4"; data[1] = "S"; // Direction is South
		}
		else if(direction_in_degrees <= -112.5 && direction_in_degrees > -157.5) {
			data[0] = "5"; data[1] = "SW"; // Direction is Southwest
		}
		else if(direction_in_degrees <= -67.5 && direction_in_degrees > -112.5) {
			data[0] = "6"; data[1] = "W"; // Direction is West
		}
		else if(direction_in_degrees <= -22.5 && direction_in_degrees > -67.5) {
			data[0] = "7"; data[1] = "NW"; // Direction is Northwest
		}
		else {
			data[0] = "9"; data[1] = "-1"; // Direction is Error
		}
		return data;
	}
	
	//This function returns only the distance between two objects
	//This android function is based off of the vincenty formulation of distance
	//http://www.movable-type.co.uk/scripts/latlong-vincenty.html
	//@param start
	//@param destination
	//@return - float of value in meters
	public float remainingDistance(Location start, Location destination) {
		if (start == null || destination == null) {
			Toast.makeText(mContext, "Error with GPS provider.", Toast.LENGTH_SHORT).show();
			return -1;
		}
		else {
			float distance = start.distanceTo(destination);
			return distance;
		}
	}
	
/*	//This function is a haversine formula  was found on:
	//http://stackoverflow.com/questions/8725283/distance-between-geopoints
	//http://www.movable-type.co.uk/scripts/latlong.html
	public static float remainingDistance(double firstlat, double firstlon, double secondlat, double secondlon){
		double earthRadius = 3958.75;  // in meters
		double distanceLatitude = Math.toRadians(secondlat-firstlat);
		double distanceLongitude = Math.toRadians(secondlon-firstlon);
		double angle = Math.sin(distanceLatitude/2)*Math.sin(distanceLatitude/2)+Math.cos(distanceLongitude/2)*Math.sin(distanceLongitude/2)*Math.cos(Math.toRadians(firstlat))*Math.cos(Math.toRadians(secondlat));
		double curve = 2*Math.atan2(Math.sqrt(angle),Math.sqrt(1-angle));
		double distance = earthRadius * curve;
		
		int meterConversion = 1609;
		
		return new Float(distance*meterConversion).floatValue();
	}
*/	
	
	//This function returns a string value of the current location
	//@param location - obtain current location
	//@return - string statement of latitude & longitude
	public String LatLong(Location location){
		String LatLong = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());
		return LatLong;
	}
	
	//This class is meant to handle the LocationListener
	//It was implemented based off of code from:
	//http://www.javacodegeeks.com/2010/09/android-location-based-services.html
	//http://www.firstdroid.com/2010/04/29/android-development-using-gps-to-get-current-location-2/
	//http://catchmayuri.blogspot.com/2011/05/get-current-location-gps-position-in.html
	public class MyLocationListener implements LocationListener {	
		@Override
		public void onLocationChanged(Location loc) {
			//String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s", loc.getLongitude(), loc.getLatitude());
			//Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();		
		}
	
		@Override
		public void onProviderDisabled(String provider) {
			//Toast.makeText(mContext, "GPS is disabled by user.", Toast.LENGTH_SHORT).show();
		}
	
		@Override
		public void onProviderEnabled(String provider) {
			//Toast.makeText(mContext, "GPS is enabled by user.", Toast.LENGTH_SHORT).show();
		}
	
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//Toast.makeText(mContext, "Provider status has changed.", Toast.LENGTH_SHORT).show();
		}	
	}
}
