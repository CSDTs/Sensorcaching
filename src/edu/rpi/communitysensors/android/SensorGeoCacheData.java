package edu.rpi.communitysensors.android;

import android.os.Parcel;
import android.os.Parcelable;

//This class will hold the variables for each sensor's geocache data
//This class is Parcelable, or it is able to be passed between different activities
//More information can be found at the following websites:
//http://bimbim.in/post/2010/09/27/Android-Passing-object-from-one-activity-to-another.aspx
public class SensorGeoCacheData implements Parcelable{
	//These are the variables that exist in the class
	//These variables are filled via the constructor and
	//called as class elements
	int id;
	String name;
	String type;
	int state;
	
	double lat;
	double lon;
	
	String creator;
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flag) {
		out.writeInt(id);
		out.writeString(name);
		out.writeString(type);
		out.writeInt(state);
		out.writeDouble(lat);
		out.writeDouble(lon);
		out.writeString(creator);
		
	}
	
	public SensorGeoCacheData() {
		// TODO Auto-generated constructor stub
	}
	
	public SensorGeoCacheData(Parcel in) {
		this.id=in.readInt();
		this.name=in.readString();
		this.type=in.readString();
		this.state=in.readInt();
		this.lat=in.readDouble();
		this.lon=in.readDouble();
		this.creator=in.readString();
	}
	
	public SensorGeoCacheData(int mid, String mname, String mtype, int mstate, double mlat, double mlon, String mcreator) {
		id = mid;
		name = mname;
		type = mtype;
		state = mstate;
		lat = mlat;
		lon = mlon;
		creator = mcreator;
	}

	@SuppressWarnings("unchecked")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { 
        public SensorGeoCacheData createFromParcel(Parcel in) 
        { 
            return new SensorGeoCacheData(in); 
        } 
  
        public SensorGeoCacheData[] newArray(int size) 
        { 
            return new SensorGeoCacheData[size]; 
        } 
    };
}
