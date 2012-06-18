package edu.rpi.communitysensors.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class AboutMessage extends Activity{
	private String activity_title;
	private String name;
	private String version;
	private String about;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//The following creates the custom title bar and sets the title to be from the intent
        //This is based off of code from:
        //http://www.londatiga.net/it/how-to-create-custom-window-title-in-android/
        //http://www.edumobile.org/android/android-programming-tutorials/creating-a-custom-title-bar/
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.about_layout);
	    
	     //The following line is used to get elements from the last activity
	     Bundle extras=getIntent().getExtras();
	     //The following line takes the element with key name "title" and stores it as a String
	     activity_title=extras.getString("title");
	     //The following line takes the element with key name "name_send" and stores it as a String
	     name=extras.getString("name_send");
	     //The following line takes the element with key name "version" and stores it as a String
	     version=extras.getString("version");
	   //The following line takes the element with key name "message" and stores it as a String
	     about=extras.getString("message");
	     
	    
	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
	    TextView title = (TextView) findViewById(R.id.title);
	    title.setText("  "+activity_title);
	    
	    //The following loads about message onto the screen
	    TextView app_about = (TextView) findViewById(R.id.textView_app_about);
	    app_about.setText(name);
	      
	    //The following find the TextView for the version and set it to it.
	    TextView about_version = (TextView) findViewById(R.id.textView_app_about_version);
	    about_version.setText(version);
	    
	    //The following loads about message onto the screen
	    TextView about_message = (TextView) findViewById(R.id.textView_about_message);
	    about_message.setText(about);
	}
}
