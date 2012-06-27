package edu.rpi.communitysensors.android;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SensorLists extends ListActivity{
	private int Activity_ID = 0;
	private String Login_ID = null;
	private String Advanced_Search = null;
	private GetCurrentLocation whereami;  //@param whereami - used to obtain current location from GetCurrentLocation class
	private Location local; //@param local - used to store current location
	private double lat = 0; //@param lat - used to store current latitude
	private double lon = 0; //@param lon - used to store current longitude
	
	private ArrayList<SensorGeoCacheData> listitems; //@param listitems - container for all items to be displayed
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     //The following creates the custom title bar and sets the title to be determined from
	     //input from the last activity
	     //This is based off of code from:
	     //http://www.londatiga.net/it/how-to-create-custom-window-title-in-android/
	     //http://www.edumobile.org/android/android-programming-tutorials/creating-a-custom-title-bar/        
	     requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	     setContentView(R.layout.lists);
	     
	     //The following line is used to get elements from the last activity
	     Bundle extras=getIntent().getExtras();
	     //The following line takes the element with key name "activityname" and stores it as a String
	     Activity_ID=extras.getInt("activityid");
	     //The following line takes the element with key name "advsearch" and stores it as a String
	     //Advanced_Search is used to determine which queried items should be returned
	     Advanced_Search=extras.getString("advsearch");
	     getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
	     TextView title = (TextView) findViewById(R.id.title);
	     
	     listitems = new ArrayList<SensorGeoCacheData>();
	     
	     //The following passes the context to GetCurrentLocation
	     whereami = new GetCurrentLocation(this);
	     
	     //We set the title
	     if(Activity_ID==1) {
	    	 title.setText("  My Sensors");	    
	     }
	     else {
	    	 title.setText("  Find Nearby");   
	     }
	     
	     //Send current gps coordinates to database to retrieve list of nearby sensors
    	 local = whereami.getLocation();
    	 lat = whereami.getLatitude(local);
    	 lon = whereami.getLongitude(local);
    	 
    	 //Fill ListArray listitems with items
    	 new QueryDatabase(Activity_ID, Advanced_Search, lat, lon, listitems);
    	 
    	 if(listitems == null) {
    		 Toast.makeText(this, "Unable to Load Sensor Data or Sensors are Out of Range", Toast.LENGTH_LONG).show(); 
    	 }
	       
	     //Here we we start the ListViewAdapter to make the list work
	     // Getting adapter 
	     // Create an adapter variable to use the modified listview structure
		 ListAdapter adapter = new ListViewAdapter(this, listitems, whereami); 
		 setListAdapter(adapter);
		 
	     //Here we would include code for what occurs when the listview is clicked
		 // When the listview is clicked, obtain the item at that location
		 getListView().setOnItemClickListener(new OnItemClickListener() {  
			// Determine the location of the click event and have it do something
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {      
			    // To obtain the information from the class, I need to create another variable
				SensorGeoCacheData sensor = listitems.get(position); 
				onSensorClick(sensor);
			}  
		});	
	}
	
	//Send information to OnSensorListClick activity
	//This activity builds the detailed description of the sensor
	public void onSensorClick(SensorGeoCacheData sensor) {
		Intent i = new Intent(getApplicationContext(), OnSensorListClick.class);
		i.putExtra("loginid", Login_ID);
		i.putExtra("sensor", sensor);
		startActivity(i);
	}
	
	//Determines the functions if the activity is paused or resumed
	@Override
	protected void onResume() {
		//Renew the ListAdapter if user has changed location
		//Fill ListArray listitems with items
		listitems = new ArrayList<SensorGeoCacheData>();
   	    new QueryDatabase(Activity_ID, Advanced_Search, lat, lon, listitems);
		ListAdapter adapter = new ListViewAdapter(this, listitems, whereami); 
		setListAdapter(adapter);
		super.onResume();
		whereami.resumeLocationUpdate();
	}
		
	@Override
	protected void onPause() {
		super.onPause();
		whereami.stopLocationUpdate();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		whereami.stopLocationUpdate();
	}
	
	//The following creates the menu options
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.lists_menu, menu);
    	return true;
    }
    
	//The following determines what occurs when the
	//particular menu option is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.item_sort:
    		showSort();
    		return true;
    	case R.id.item_advanced_search:
    		showAdvanced_Search();
    		return true;
    	case R.id.item_maps:
    		showMap();
    		return true;
    	case R.id.item_save:
    		showSave();
    		return true;    		
    	case R.id.item_help:
    		showHelp();
    		return true;
    	case R.id.item_settings:
    		showSettings();
    		return true;
    	}
    	return true;
    }
    
    private void showSort(){
    	//
    	Toast.makeText(this, "Sorry, this feature is not available yet!", Toast.LENGTH_SHORT).show();
    }
    
    private void showAdvanced_Search(){
    	//
    	Toast.makeText(this, "Sorry, this feature is not available yet!", Toast.LENGTH_SHORT).show();
    }
    
    private void showMap(){
    	Intent i = new Intent(getApplicationContext(), SensorMaps.class);
    	i.putExtra("activityid", Activity_ID);
		i.putExtra("advsearch", Advanced_Search);
		startActivity(i);
    }
    private void showSave(){
    	//
    	Toast.makeText(this, "Sorry, this feature is not available yet!", Toast.LENGTH_SHORT).show();
    }
    private void showHelp(){
    	Intent i = new Intent();
		i.setClass(SensorLists.this, HelpMenu.class);
		startActivity(i);
    }
    private void showSettings(){
    	//
    }
}
