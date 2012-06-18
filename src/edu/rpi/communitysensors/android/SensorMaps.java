package edu.rpi.communitysensors.android;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

public class SensorMaps extends MapActivity {
	
	private int Activity_ID = 0;
	private String Advanced_Search = null;
	
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private MapController mapController;
	
	/** Called when the activity is first created. **/
	@Override
	public void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     //The following creates the custom title bar and sets the title to be determined from
	     //input from the last activity
	     //This is based off of code from:
	     //http://www.londatiga.net/it/how-to-create-custom-window-title-in-android/
	     //http://www.edumobile.org/android/android-programming-tutorials/creating-a-custom-title-bar/        
	     requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	     setContentView(R.layout.maps);    
     
	     //The following line is used to get elements from the last activity
	     Bundle extras=getIntent().getExtras();
	     //The following line takes the element with key name "activityname" and stores it as a String
	     Activity_ID=extras.getInt("activityid");
	     //The following line takes the element with key name "advsearch" and stores it as a String
	     Advanced_Search=extras.getString("advsearch");
	     getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
	     TextView title = (TextView) findViewById(R.id.title);
     
	     //The following identify the map and determine the controls
	     mapView = (MapView) findViewById(R.id.mapview);
	     mapView.setBuiltInZoomControls(true);
	     mapView.setSatellite(true);
	     /*   
	     //The following create an overlay for my current location
	     myLocationOverlay = new MyLocationOverlay(this,mapView);
	     myLocationOverlay.runOnFirstFix(new Runnable() {
	      	@Override
	      	public void run() {
	      		mapController.animateTo(myLocationOverlay.getMyLocation());
	      	}
	     });
	       
	     mapView.getOverlays().add(myLocationOverlay);
	     mapView.postInvalidate();
	        
	     //The following set the current zoom level
	     mapController = mapView.getController();
	     mapController.setZoom(16);      
	     */
	     //The following sets gathers the data and sets the name
	     if(Activity_ID==1) {
	    	 title.setText("  My Sensors");
	    	 //Send loginid to database to retrieve list of user's sensors
	    	 
	     }
	     else {
	    	 title.setText("  Find Nearby");
	    	 //Send current gps coordinates to database to retrieve list of nearby sensors
	    	 
	     }
	     
	     //Here we would include code for the overlay of map icons
	     
	     //Here we would include code for what happens when a map icon is clicked
	     
	     //Here we would include code for updating the current location
	     
	}
	
	//The below is needed for typical map code. This is so the map can be
	//turned off and on to save information use
	@Override
	protected void onResume() {
		super.onResume();
		//myLocationOverlay.enableMyLocation();
	}
		
	@Override
	protected void onPause() {
		super.onPause();
		//myLocationOverlay.disableMyLocation();
	}
		
	//The following extends mylocationoverlay to add some customized
	//feature to it.  In this case, when a person moves, so does the
	//cursor.
	/*
	private class MyLocationOverlayExtension extends MyLocationOverlay {
		public MyLocationOverlayExtension(Context context, MapView mapView) {
			super(context, mapView);
		}
		@Override
		public synchronized void onLocationChanged(Location location) {
			super.onLocationChanged(location);
			GeoPoint point = new GeoPoint((int) (location.getLatitude()*1E6), (int) (location.getLongitude()*1E6));
			mapController.animateTo(point);
		}
	}
*/
	//The following needs to be generated for every mapView
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//The following creates the menu options
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.map_menu, menu);
    	return true;
    }
    
	//The following determines what occurs when the
	//particular menu option is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.item_mylocation:
    		showmyLocation();
    		return true;
    	case R.id.item_mapmode:
    		showMapMode();
    		return true;
    	case R.id.item_search:
    		showSearch();
    		return true;
    	case R.id.item_advanced_search:
    		showAdvanced_Search();
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
    
    private void showmyLocation(){
    	//
    }
    
    private void showMapMode(){
    	//
    }
    
    private void showSearch(){
//
    }
    private void showAdvanced_Search(){
    	//
    }
    private void showHelp(){
    	Intent i = new Intent();
		i.setClass(SensorMaps.this, HelpMenu.class);	
		startActivity(i);
    }
    private void showSettings(){
    	//
    }
}