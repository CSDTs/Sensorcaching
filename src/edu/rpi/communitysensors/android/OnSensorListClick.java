package edu.rpi.communitysensors.android;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class OnSensorListClick extends Activity {
	private String Login_ID = null;
	private SensorGeoCacheData Sensor_Data = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //The following creates the custom title bar and sets the title to be "SENSORCACHING"
        //This is based off of code from:
        //http://www.londatiga.net/it/how-to-create-custom-window-title-in-android/
        //http://www.edumobile.org/android/android-programming-tutorials/creating-a-custom-title-bar/        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.on_sensor_list_click);     

        //The following line is used to get elements from the last activity
	    Bundle extras=getIntent().getExtras();
	    //The following line takes the element with key name "loginid" and stores it as a String
	    Login_ID=extras.getString("loginid");
	    //The following line takes the element with key name "sensor" and stores it as a SensorGeoCacheData variable
	    Sensor_Data=(SensorGeoCacheData) extras.getParcelable("sensor");
	    
	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
	    TextView title = (TextView) findViewById(R.id.title);
        title.setText("  Sensor Details");
        
        //Identify the static layout items
	    TextView name = (TextView) findViewById(R.id.click_item_name); 
	    TextView type = (TextView) findViewById(R.id.click_item_type);
	    TextView creator = (TextView) findViewById(R.id.click_item_creator);
	    ImageView icon = (ImageView) findViewById(R.id.click_item_icon);
	    TextView lat = (TextView) findViewById(R.id.click_item_lat);
	    TextView lon = (TextView) findViewById(R.id.click_item_lon);
	    TextView direction = (TextView) findViewById(R.id.click_item_direction);
	    TextView distance = (TextView) findViewById(R.id.click_item_distance);
	    TextView hide_date = (TextView) findViewById(R.id.click_item_hide_date);
	    TextView last_date = (TextView) findViewById(R.id.click_item_last_date);
	    
	    //Identify the clickable layout items
	    TableRow navigate = (TableRow) findViewById(R.id.click_tableRow_navigate);
	    TableRow description = (TableRow) findViewById(R.id.click_tableRow_description);
	    TableRow hint = (TableRow) findViewById(R.id.click_tableRow_hint);
	    TableRow attributes = (TableRow) findViewById(R.id.click_tableRow_attributes);
	    TableRow photos = (TableRow) findViewById(R.id.click_tableRow_photo);
	    TableRow save = (TableRow) findViewById(R.id.click_tableRow_save);
	    TableRow share = (TableRow) findViewById(R.id.click_tableRow_share);
	    TableRow graph = (TableRow) findViewById(R.id.click_tableRow_graph);
	    TableRow nearby = (TableRow) findViewById(R.id.click_tableRow_nearby);
	    TableRow web = (TableRow) findViewById(R.id.click_tableRow_web);
	     
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
	    
	    //Setting all values in listview 
	    name.setText(Sensor_Data.name); 
	    type.setText("Type: "+Sensor_Data.type);
	    creator.setText("By: "+Sensor_Data.creator);
	    lat.setText("Latitude: "+Sensor_Data.lat);
	    lon.setText("Longitude: "+Sensor_Data.lon);
	    hide_date.setText("Date Hidden:\n");
	    last_date.setText("Last Found:\n");
	    
	    //The following finds the state icon to place.
	    icon.setImageResource(images[Sensor_Data.state]);   
	    
	    //The following finds the distance and direction the sensor is located
	    //First create the class GetCurrentLocation and set a current location
	    GetCurrentLocation whereami = new GetCurrentLocation(this);
	    Location hereiam = whereami.getLocation();
	    
	    //Next set sensor_location
	    Location sensor_location = new Location("");
	    sensor_location.setLatitude(Sensor_Data.lat);
	    sensor_location.setLongitude(Sensor_Data.lon);
	    
	    //Create some dummy parameters for finding the distance and direction
	    float[] remaining_distance = {-1, -1, -1};
	    String[] compass_direction = {"9", "9"};    
	    whereami.twoPointData(hereiam,  sensor_location, remaining_distance);
	    
	    //set the distance text to meters or kilometers
	    if(remaining_distance[0] >= 1000) {  	
	    	distance.setText(Float.toString(remaining_distance[0]/1000)+" km");
	    }
	    else {
	    	distance.setText(Float.toString(remaining_distance[0])+" m");
	    }
	    
	    //Set the compass direction
	    compass_direction = whereami.getDirection(remaining_distance[2]);	    
	    direction.setText(compass_direction[1]);
	    direction.setCompoundDrawablesWithIntrinsicBounds(compass_images[Integer.parseInt(compass_direction[0])],0,0,0);
	    	    
	    //The following set all of the button actions
	    navigate.setOnClickListener(GoNavigate);	 
	    description.setOnClickListener(GoDescription);	
	    hint.setOnClickListener(GoHint);	 
	    attributes.setOnClickListener(GoAttribute);	 
	    photos.setOnClickListener(GoPhotos);	 
	    save.setOnClickListener(GoSave);	 
	    share.setOnClickListener(GoShare);	 
	    graph.setOnClickListener(GoGraph);	 
	    nearby.setOnClickListener(GoNearby);
	    web.setOnClickListener(GoWeb);	 	 
	    
    }
	
    //The below is needed for typical map code. This is so the map can be
  	//turned off and on to save information use
  	@Override
  	protected void onResume() {
  		super.onResume();
  	}
  		
  	@Override
  	protected void onPause() {
  		super.onPause();
  	}
    
    public OnClickListener GoNavigate = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//DoToast("navigate");
    		//once this button is clicked a
    		Intent i = new Intent();
    		i.setClass(OnSensorListClick.this, GoogleRouteActivity.class);
    		i.putExtra("title", "Navigate to "+Sensor_Data.name);
    		i.putExtra("latitude", Sensor_Data.lat);
    		i.putExtra("longitude", Sensor_Data.lon);
    		i.putExtra("state", Sensor_Data.state);
    		startActivity(i);
    	}
    };
    
    public OnClickListener GoDescription = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//DoToast("description");
    		Intent i = new Intent();
    		i.setClass(OnSensorListClick.this, AboutMessage.class);
    		i.putExtra("title", "Sensor Description");
    		i.putExtra("name_send", Sensor_Data.name);
    		i.putExtra("version", "By: "+Sensor_Data.creator);
    		i.putExtra("message","Here a description of the sensor will exist");
    		startActivity(i);
    	}
    };
    
    public OnClickListener GoHint = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//DoToast("hint");
    		Intent i = new Intent();
    		i.setClass(OnSensorListClick.this, AboutMessage.class);
    		i.putExtra("title", "Hints");
    		i.putExtra("name_send", "Hints for "+Sensor_Data.name);
    		i.putExtra("version", "By: "+Sensor_Data.creator);
    		i.putExtra("message","Here hints for finding the sensor will be located");
    		startActivity(i);
    	}
    };
    
    public OnClickListener GoAttribute = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//DoToast("attribute");
    	}
    };
    
    public OnClickListener GoPhotos = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//DoToast("photos");
    	}
    };
    
    public OnClickListener GoSave = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//DoToast("save");
    	}
    };
    
    public OnClickListener GoShare = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//DoToast("share");
    	}
    };
    
    public OnClickListener GoGraph = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//DoToast("graph");
    	}
    };
    
    public OnClickListener GoNearby = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//DoToast("nearby");
    		Intent i = new Intent(getApplicationContext(), SensorLists.class);
    		i.putExtra("activityid", 0);
			i.putExtra("loginid", Login_ID);
			i.putExtra("advsearch", "");
			startActivity(i);
    	}
    };
    
    public OnClickListener GoWeb = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//DoToast("Web");
    	}
    };
    
  //The following function creates the main menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_menu, menu);
    	return true;
    }
    
    //The following function determine what happens when the menu is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.item_help:
    		showHelp();
    		return true;
    	case R.id.item_settings:
    		showSettings();
    		return true;
    	}
    	return true;
    }
    
    //The following function starts the Help Menu activity
    private void showHelp(){
    	Intent intent = new Intent();
		intent.setClass(OnSensorListClick.this, HelpMenu.class);
		startActivity(intent);
    }
    
    //The following function starts the Settings activity
    private void showSettings(){
    	//
    }
    
    public void DoToast(String type){
    	Toast.makeText(this, "You pressed the "+type+" button", Toast.LENGTH_SHORT).show();	
    }
}
