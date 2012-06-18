package edu.rpi.communitysensors.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity {
	private Button LoginButton;
	private ImageView UserImage;
	private TextView UserText;
	private EditText AdvancedSearch=null;
	private String advanced_search = null;
	private ImageButton SearchButton;
	
	private String user_name = null;
	private String password = null;
	private Boolean keep_logged = false;
	private Boolean login_valid = false;
	private SharedPreferences login_preferences;
	
	private GridView gridView;
	
    /** Called when the activity is first created. */
	/** Creates a custom title bar overlay,
	 *  Locates all items on the layout
	 *  Checks to see if login information exists
	 *  Starts the gridview adapter and loads gridview item	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //The following creates the custom title bar and sets the title to be "SENSORCACHING"
        //This is based off of code from:
        //http://www.londatiga.net/it/how-to-create-custom-window-title-in-android/
        //http://www.edumobile.org/android/android-programming-tutorials/creating-a-custom-title-bar/        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);     
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("  SENSORCACHING");
        
        //The following finds the various items on the layout
        LoginButton = (Button) findViewById(R.id.main_button_login);
        UserImage = (ImageView) findViewById(R.id.image_userimage);
        UserText = (TextView) findViewById(R.id.text_userlogin);
        AdvancedSearch = (EditText) findViewById(R.id.Search_Text);
        SearchButton = (ImageButton) findViewById(R.id.imageButton_search);
        
        //Get the values for the shared preference from the previous time
        //and use them to set the preference screen
        login_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_name = login_preferences.getString("username", null);
        password = login_preferences.getString("password", null);
        keep_logged = login_preferences.getBoolean("keep_logged", false);
                     
        if(keep_logged){
    		login_valid = login(user_name, password);
    	}
        
        if(user_name != null){
        	LoginButton.setVisibility(View.GONE);
        	UserImage.setVisibility(View.VISIBLE);
        	UserText.setVisibility(View.VISIBLE);
        	
        	UserText.setText(user_name);
        }
        else{
        	LoginButton.setVisibility(View.VISIBLE);
    		UserImage.setVisibility(View.GONE);
    		UserText.setVisibility(View.GONE);
        }
        
        //The following finds the GridView
        gridView = (GridView) findViewById(R.id.main_grid);

        //Instance of the Button Adapter
        //Loads the buttons into the gridview
        gridView.setAdapter(new MainGridButtonAdapter(this));
        
        //Check if a button is pressed
        LoginButton.setOnClickListener(login_clicked);  
        SearchButton.setOnClickListener(search_clicked);
        gridView.setOnItemClickListener(Button_Pressed);
    }
    
    
    
    //The following function handles the selection of a grid button
    private OnItemClickListener Button_Pressed = new OnItemClickListener() {    		
    	@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {   
    		if (position==3) {
				//
			}
			else if (position==2){
				Intent i = new Intent(getApplicationContext(), BluetoothChat.class);
				startActivity(i);
			}
			else {
				StartListActivities(position);
			}
			
		}        	
	};
	
	private void StartListActivities(int position) {
		Intent i = new Intent(getApplicationContext(), SensorLists.class);
		if (position == 1) {
			i.putExtra("activityid", position);
			i.putExtra("advsearch", advanced_search);
			startActivity(i);
		}
		else {
			i.putExtra("activityid", position);
			i.putExtra("advsearch", advanced_search);
			startActivity(i);
		}
	}
	
	private OnClickListener login_clicked = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Main.this, LoginActivity.class);
			startActivity(intent);
		} 		
	};
    
	private Boolean login(String username, String passwrod) {
    	return true;
    }
	
	private OnClickListener search_clicked = new OnClickListener(){

		@Override
		public void onClick(View v) {
			advanced_search=AdvancedSearch.getText().toString().trim(); 
			Intent i = new Intent(getApplicationContext(), SensorLists.class);
			i.putExtra("activityid", 0);
			i.putExtra("advsearch", advanced_search);
			startActivity(i);
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
		intent.setClass(Main.this, HelpMenu.class);
		startActivity(intent);
    }
    
    //The following function starts the Settings activity
    private void showSettings(){
    	Intent intent = new Intent();
		intent.setClass(Main.this, Preferences.class);
		startActivity(intent);
    }
    
  //The below is needed for typical map code. This is so the map can be
  	//turned off and on to save information use
  	@Override
  	protected void onResume() {
  		user_name = login_preferences.getString("username", "");
        password = login_preferences.getString("password","");
        keep_logged = login_preferences.getBoolean("keep_logged", false);
                
        if(keep_logged){
        	LoginButton.setVisibility(View.GONE);
        	UserImage.setVisibility(View.VISIBLE);
        	UserText.setVisibility(View.VISIBLE);
        	login(user_name, password);
        	UserText.setText(user_name);
        }
        else {
        	LoginButton.setVisibility(View.VISIBLE);
        	UserImage.setVisibility(View.GONE);
        	UserText.setVisibility(View.GONE);
        }
  		super.onResume();
  	}
  		
  	@Override
  	protected void onPause() {  		 
  		super.onPause();	
  	}
}