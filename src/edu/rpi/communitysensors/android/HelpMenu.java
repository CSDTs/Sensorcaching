package edu.rpi.communitysensors.android;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HelpMenu extends ListActivity{
	
	public void onCreate (Bundle savedInstanceState) {
		//The following create the View and set the Custom Title bar and orientation
		super.onCreate(savedInstanceState);
		
		//The following creates the custom title bar and sets the title to be "HELP"
        //This is based off of code from:
        //http://www.londatiga.net/it/how-to-create-custom-window-title-in-android/
        //http://www.edumobile.org/android/android-programming-tutorials/creating-a-custom-title-bar/
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.lists);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
		TextView title = (TextView) findViewById(R.id.title);
        title.setText("  Help");
		
        //The following create the list items for the text_list_item view
		String[] HELP_STRING = new String[] { "About Sensorcaching","Tutorial", "Website" };	
		ArrayAdapter<String> help_adapter = new ArrayAdapter<String>(this, R.layout.text_list_item, HELP_STRING);
		setListAdapter(help_adapter);	
	}
	
	//The following waits for a click event.  Depending the click event 
	//the correct activity will be run
	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id) {
		switch (position) {
		case 0: 
			showAbout();
			break;
		case 1:
			showTutorial();
			break;
		case 2:
			showWebsite();
			break;
    	}
	}
	//This brings you to the about page
	private void showAbout(){
		//To get to a new activity I use the following
		Intent i = new Intent();
		i.setClass(HelpMenu.this, AboutMessage.class);
		i.putExtra("title", "About Sensorcaching");
		i.putExtra("name_send", getString(R.string.about_app));
		i.putExtra("version", "Version:  "+Build.VERSION.RELEASE);
		i.putExtra("message",getString(R.string.about_message));
		startActivity(i);
    }
	//This brings you to the tutorial page
	private void showTutorial(){
    	//
    }
	//This brings you to our website
	private void showWebsite(){
		//To get to the website I use the following
		Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.communitysensors.rpi.edu/"));	
		startActivity(i);
    }
}
