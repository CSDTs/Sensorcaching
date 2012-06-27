package edu.rpi.communitysensors.android;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//This custom listviewadapter takes the data from a SensorGeoCacheData class to create
//properly use the list_items layout.  For example, it can set the image
//to the right color and sets the text in the right format.
public class ListViewAdapter extends BaseAdapter {

	 private Activity activity; 
	 private ArrayList<SensorGeoCacheData> data; 
	 private static LayoutInflater inflater=null;
	 private GetCurrentLocation whereami;
	 
	 //This takes a SensorGeoCacheData and stores it as data
	 public ListViewAdapter(Activity a, ArrayList<SensorGeoCacheData> d, GetCurrentLocation w) { 
	     activity = a; 
	     data=d; 
	     inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	     whereami = w;
	 } 
	
	 // This returns the size of the SensorGeoCacheData
	 public int getCount() { 
	     return data.size(); 
	 } 
	
	 // This returns the position of the item
	 public Object getItem(int position) { 
	     return position; 
	 } 
	
	 // This returns the position of an id
	 public long getItemId(int position) { 
	     return position; 
	 } 
	
	 // This fills the list_item layout view in the right format
	 public View getView(int position, View convertView, ViewGroup parent) { 
	     View vi=convertView; 
	     if(convertView==null) 
	         vi = inflater.inflate(R.layout.sensor_list_item, null); 
	
	     //Identify the layout items
	     TextView name = (TextView)vi.findViewById(R.id.item_name); 
	     TextView type = (TextView)vi.findViewById(R.id.item_type);
	     ImageView icon = (ImageView)vi.findViewById(R.id.item_icon);
	     TextView distance = (TextView)vi.findViewById(R.id.item_distance);
	     TextView direction = (TextView)vi.findViewById(R.id.item_direction);
	     
	     //Here we define the images to be set
	     int[] images = new int[3];
	     images[0] = R.drawable.new_sensor;
	     images[1] = R.drawable.young_sensor;
	     images[2] = R.drawable.old_sensor;
	     
	     int[] compass_images = new int[8];
		 compass_images[0] = R.drawable.north;
		 compass_images[1] = R.drawable.northeast;
		 compass_images[2] = R.drawable.east;
		 compass_images[3] = R.drawable.southeast;
		 compass_images[4] = R.drawable.south;
		 compass_images[5] = R.drawable.southwest;
		 compass_images[6] = R.drawable.west;
		 compass_images[7] = R.drawable.northwest;
	
	     //We obtain the SensorGeoCacheData for a particular location
	     SensorGeoCacheData item = null; 
	     item = data.get(position); 
	 
	     
	     //Setting all values in listview 
	     name.setText(item.name); 
	     type.setText("Type: "+item.type);
	     icon.setImageResource(images[item.state]);
	     
	     //The following finds the distance and direction the sensor is located
		 //First create the class GetCurrentLocation and set a current location
		 Location hereiam = whereami.getLocation();
		    
		 //Next set sensor_location
		 Location sensor_location = new Location("");
		 sensor_location.setLatitude(item.lat);
		 sensor_location.setLongitude(item.lon);
		    
		 //Create some dummy parameters for finding the distance and direction
		 float[] remaining_distance = {-1, -1, -1};
		 String[] compass_direction = {"9", "9"};    
		 whereami.twoPointData(hereiam,  sensor_location, remaining_distance);
		    
		 //set the distance text to meters or kilometers
		 if(remaining_distance[0] >= 1000) {  	
		  	distance.setText("Distance to:\n"+Float.toString(remaining_distance[0]/1000)+" km");
		 }
		 else {
		  	distance.setText("Distance to:\n"+Float.toString(remaining_distance[0])+" m");
		 }
		    
	    //Set the compass direction
		compass_direction = whereami.getDirection(remaining_distance[2]);	    
		direction.setText(compass_direction[1]);
		direction.setCompoundDrawablesWithIntrinsicBounds(compass_images[Integer.parseInt(compass_direction[0])],0,0,0);
	     
	    return vi; 
	 } 
}
