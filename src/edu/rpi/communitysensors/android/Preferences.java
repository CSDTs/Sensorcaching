package edu.rpi.communitysensors.android;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.Window;
import android.widget.TextView;

public class Preferences extends PreferenceActivity{
	
	/** Sets up Preference Screen
	 *  Initializes text so it is white
	 *  Initializes background so it is the @drawable/background
	 *  Finds the item with key "login_button" and sets a listener for it
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*
		//The following creates the custom title bar and sets the title to be "SENSORCACHING"
        //This is based off of code from:
        //http://www.londatiga.net/it/how-to-create-custom-window-title-in-android/
        //http://www.edumobile.org/android/android-programming-tutorials/creating-a-custom-title-bar/        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);     
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("  Settings");*/
		
		setTheme(R.style.Text_White);
		addPreferencesFromResource(R.xml.preferences);
		
		//The following sets the background of preferences to an image
		//http://groups.google.com/group/android-developers/browse_thread/thread/d3bb7af55dba7d8d
		getWindow().setBackgroundDrawableResource(R.drawable.background); 
		      
		Preference login_button = (Preference) findPreference("login_button");		
		login_button.setOnPreferenceClickListener(login_click);
		
	}
	
	private OnPreferenceClickListener login_click = new OnPreferenceClickListener() {
		public boolean onPreferenceClick(Preference preference) {
			Intent intent = new Intent();
			intent.setClass(Preferences.this, LoginActivity.class);
			startActivity(intent);
			return false;
		}
	};
}
