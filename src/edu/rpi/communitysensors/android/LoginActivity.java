package edu.rpi.communitysensors.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.insready.drupalcloud.*;

public class LoginActivity extends Activity{
	private EditText user;
	private EditText pw;
	
	private String user_name;
	private String password;
	private Boolean keep_logged;	
	private Boolean am_logged;
	private Boolean is_valid;
	
	private RESTServerClient rsc;
	
	private SharedPreferences login_preferences;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //The following creates the custom title bar and sets the title to be "SENSORCACHING"
        //This is based off of code from:
        //http://www.londatiga.net/it/how-to-create-custom-window-title-in-android/
        //http://www.edumobile.org/android/android-programming-tutorials/creating-a-custom-title-bar/        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.login);     
	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
	    TextView title = (TextView) findViewById(R.id.title);
        title.setText("  Login");
        
        //The following sets up the REST server client init parameters
        rsc = new RESTServerClient(getApplicationContext(), "Session", "http://www.communitysensors.rpi.edu/?q=testserv/", "testserv", new Long(1800000));
        
        //Set the items in the layout
        user = (EditText) findViewById(R.id.editText_username);
        pw = (EditText) findViewById(R.id.editText_password);
        Button login_button = (Button) findViewById(R.id.button_login);
        
        //Initialize variables
        user_name = null;
        password = null;
        keep_logged = false;
        am_logged = false;
        is_valid = false;
        
        //Get the values for the shared preference from the previous time
        //and use as the default values in the EditText boxes      
        //http://marakana.com/forums/android/examples/63.html    
        login_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_name = login_preferences.getString("username", null);
        password = login_preferences.getString("password",null);
        keep_logged = login_preferences.getBoolean("keep_logged", false);
        am_logged = login_preferences.getBoolean("am_logged", false);
        user.setText(user_name, TextView.BufferType.EDITABLE);
        pw.setText(password, TextView.BufferType.EDITABLE);
        
        //When the login button is pressed
        login_button.setOnClickListener(login_clicked);               
    }
    
    //Get the information
    private OnClickListener login_clicked = new OnClickListener(){

		@Override
		public void onClick(View v) {
			user_name = user.getText().toString();
	        password = pw.getText().toString();
			login(user_name,password);
		}
    	
    };
    
    //Verify if login is correct
    private void login(String username, String passwrod) {
    	//check if login is correct
    	try {
			String res = rsc.userLogin(username, password);
			System.out.println(res);
			is_valid = true;
		} catch (ServiceNotAvailableException e) {
			e.printStackTrace();
			is_valid = false;
		}
    	
    	if(is_valid) {
    		SharedPreferences.Editor editor = login_preferences.edit();
            editor.putString("username", user_name);
            editor.putString("password", password);
            editor.commit(); 
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), Main.class);
            startActivity(i);
    	}
    	else{
    		Toast.makeText(this, "Sorry, your username or password is incorrect", Toast.LENGTH_SHORT).show();
    	}
    }
    
    //The below is for when the program is paused or resumed
  	@Override
  	protected void onResume() {
  		super.onResume();
  	}
  		
  	@Override
  	protected void onPause() {  		 
  		if(keep_logged) {
  			user_name = user.getText().toString();
	        password = pw.getText().toString();
	        //The following code stores the login information from preferences      
            SharedPreferences.Editor editor = login_preferences.edit();
            editor.putString("username", user_name);
            editor.putString("password", password);
            editor.commit();        	
        }
  		else {
  			//The following code stores the login information from preferences
  			SharedPreferences.Editor editor = login_preferences.edit();
  			editor.putString("username", "");
            editor.putString("password", "");
            editor.commit();  
  		}
  		super.onPause();	
  	}
}
