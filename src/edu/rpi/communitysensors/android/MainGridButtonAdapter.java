package edu.rpi.communitysensors.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//This class is used to place buttons in the main grid
//This class is based on example work provided by:
//http://www.stealthcopter.com/blog/2010/09/android-creating-a-custom-adapter-for-gridview-buttonadapter/
//http://www.androidhive.info/2012/02/android-gridview-layout-tutorial/
//http://www.firstdroid.com/2011/02/06/android-tutorial-gridview-with-icon-and-text/
public class MainGridButtonAdapter extends BaseAdapter{
	private Context mContext;
	
	//Create a string array of button names
	public String[] buttonnames = {
			"Find Nearby",
			"My Sensors",
			"Share",
			"Graph"
	};
	
	//Create a array of button images
	public Integer[] buttonimages = {
			R.drawable.find_button,
			R.drawable.mysensor_button,
			R.drawable.share_button,
			R.drawable.graph_button
	};
	
	//Get context to be used later (constructor of MainGridButtonAdapter)
	public MainGridButtonAdapter(Context c) {
		mContext = c;
	}
	
	//Get total number of buttons contained in adapter
	public int getCount(){
		return buttonnames.length;
	}
	
	//Return button image at location
	public Object getItem(int position) {
		return null;
	}
	
	//Return id of an item in the adapter
	public long getItemId(int position){
		return 0;
	}
	
	//Defines the grid item view
	//@param position - obtain the current position of the item
	//@param convertView - the view that will be populated
	//@param parent -  the parent object holding the view
	//@return - a view with items populated
	public View getView(int position, View convertView, ViewGroup parent) {
		View myView = convertView;
		
		//If no view create a view
		if (convertView == null) {
			//Define the view to display in grid
			//Initialize attributes
			LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			myView = li.inflate(R.layout.grid_item_main, null);
						
		}
		
		//Add Text
		TextView tv = (TextView) myView.findViewById(R.id.grid_item_main_text);
		tv.setText(buttonnames[position]);
		
		//Add Image
		ImageView iv = (ImageView) myView.findViewById(R.id.grid_item_main_image);
		iv.setImageResource(buttonimages[position]);
		
		return myView;	
	}
}
