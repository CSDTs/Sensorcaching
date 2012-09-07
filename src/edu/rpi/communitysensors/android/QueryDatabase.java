package edu.rpi.communitysensors.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.insready.drupalcloud.RESTServerClient;
import com.insready.drupalcloud.ServiceNotAvailableException;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;

//The purpose of this function is to Query the Database
//And create an ArrayList of variables in return
public class QueryDatabase {
	
	private RESTServerClient rsc;
	
	public QueryDatabase(int user, String Query, double lat, double lon, ArrayList<SensorGeoCacheData> data, Context ctx){	
		
		SensorGeoCacheData g;
		
		/*The following is dummy test data
		g = new SensorGeoCacheData(0,"FirstSensorDemo","Temp", 0, 42.729700, -73.680196, "Chris");
		data.add(g);
		g = new SensorGeoCacheData(1,"SensorDemoAgain","C02", 2, 42.735508, -73.681655, "Louis");
		data.add(g);
	    g = new SensorGeoCacheData(2,"ThirdDemoSensor","Temp", 1, 42.733861, -73.682159, "Jie");
	    data.add(g);
	    g = new SensorGeoCacheData(3,"Somewhere far away","Light", 0, 40.753434, -73.983135, "NYC");
	    data.add(g);
	    */

	    JSONArray jArray;
		String result = null;
		InputStream is = null;
		StringBuilder sb=null;
	    
		int threshold = 1000; //this is the radius in miles (1 kilometer = 0.621371192 miles)
		
		//Fill an ArrayList with values for lat, lon, & threshold
		ArrayList<NameValuePair> mylatlong = new ArrayList<NameValuePair>();
		mylatlong.add(new BasicNameValuePair("c_latitude",Double.toString(lat)));
		mylatlong.add(new BasicNameValuePair("c_longitude",Double.toString(lon)));
		mylatlong.add(new BasicNameValuePair("c_threshold",Integer.toString(threshold)));
		//try{
		     /*HttpClient httpclient = new DefaultHttpClient();
		     //The below is for the test site
		     //HttpPost httppost = new HttpPost("http://mobsourcing.cs.rpi.edu/sensorcaching/query.php");
		     
		     //The below is the real site
		     HttpPost httppost = new HttpPost("http://www.communitysensors.rpi.edu/sites/all/libraries/query2.php");
		     
		     httppost.setEntity(new UrlEncodedFormEntity(mylatlong));
		     HttpResponse response = httpclient.execute(httppost);
		     HttpEntity entity = response.getEntity();
		     is = entity.getContent();
		     }catch(Exception e){
		         Log.e("log_tag", "Error in http connection"+e.toString());
		    }
		//convert response to string
		try{
		      BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		       sb = new StringBuilder();
		       sb.append(reader.readLine() + "\n");

		       String line="0";
		       while ((line = reader.readLine()) != null) {
		                      sb.append(line + "\n");
		        }
		        is.close();
		        result=sb.toString();
		        }catch(Exception e){
		              Log.e("log_tag", "Error converting result "+e.toString());
		        } */
			
		try {
			//The following sets up the REST server client init parameters
		    rsc = new RESTServerClient(ctx, "communitysensors.RSC", "http://www.communitysensors.rpi.edu/testserv/", "testserv", new Long(1800000));
		    result = rsc.getFilteredNodes("type", "sensor_caches");
		} catch(Exception e) {
			Log.e("log_tag", "Error getting nodes" + e.toString());
		}
		
		//parsing data
		//the below variables are used to store data temporarily before
		//adding it to the SensorGeoCache listarray
		int sensor_id;
		String sensor_name;
		String sensor_type;
		int sensor_state;
		double sensor_lat;
		double sensor_lon;
		String sensor_creator;
		
/*		The below is used in the test case without the listarray
 * 		variables for return values from servers, limit 5, if you increase array size, need to do so in query as well
		
		int[] sc_id = new int[5];
		String[] sc_name= new String[5];
		String[] sc_type= new String[5];
		int[] sc_state= new int[5];
		double[] sc_long = new double[5];
		double[] sc_lat = new double[5];
		String[] sc_owner= new String[5];
		int[] sc_distance = new int[5];
*/		
		try {
			  Log.e("QD_tag",result);
		      jArray = new JSONArray(result);
		      JSONObject json_data=null;
		      JSONObject jo = null;
		      JSONArray ja = null;
		      for(int i=0;i<jArray.length();i++){
		             json_data = jArray.getJSONObject(i);
		             //Below is for the communitySensors data from the REST server
	            	 sensor_id = json_data.getInt("nid");
	            	 sensor_name = json_data.getString("title");
	            	 String node = rsc.nodeGet(sensor_id, "field_sensors_name,field_sensor_type,field_sensor_state,field_sensor_lat,field_sensor_lon,field_sensor_owner");
	            	 JSONObject nodeJSON = new JSONObject(node);
	            	 Log.d("QD_tag", node);
	            	 
	            	 jo = nodeJSON.getJSONObject("field_sensor_type");
	            	 ja = jo.getJSONArray("und");
	            	 sensor_type = ((JSONObject) ja.get(0)).getString("value");
	            	 
	            	 jo = nodeJSON.getJSONObject("field_sensor_state");
	            	 ja = jo.getJSONArray("und");
	            	 sensor_state=((JSONObject) ja.get(0)).getInt("value");
	            	 
	            	 jo=nodeJSON.getJSONObject("field_sensor_lat");
	            	 ja = jo.getJSONArray("und");
	            	 sensor_lat=((JSONObject) ja.get(0)).getDouble("value");
	            	 
	            	 jo=nodeJSON.getJSONObject("field_sensor_long");
	            	 ja = jo.getJSONArray("und");
	            	 sensor_lon = ((JSONObject) ja.get(0)).getDouble("value");
	            	 
	            	 jo=nodeJSON.getJSONObject("field_sensor_owner");
	            	 ja = jo.getJSONArray("und");
	            	 sensor_creator= ((JSONObject) ja.get(0)).getString("value");
	            	 
		             //The below is on real server 
		             /*sensor_id=json_data.getInt("field_id_value");
		             sensor_name=json_data.getString("field_name_value");
		             sensor_type=json_data.getString("field_type_value");
		             sensor_state=json_data.getInt("field_state_value");
		             sensor_lat=json_data.getDouble("field_lat_value");
		             sensor_lon=json_data.getDouble("field_long_value");
		             sensor_creator=json_data.getString("field_owner_value");*/
		             //
		             /*The below is on the test server
		             sensor_id=json_data.getInt("sensor_id");
		             sensor_name=json_data.getString("sensor_name");
		             sensor_type=json_data.getString("sensor_type");
		             sensor_state=json_data.getInt("sensor_state");
		             sensor_lat=json_data.getDouble("sensor_lat");
		             sensor_lon=json_data.getDouble("sensor_long");
		             sensor_creator=json_data.getString("sensor_owner");
		             */ 
		             /*The below is to create dummy data
		             sensor_id = i;
		             sensor_name = "Test Sensor" + Int.toString(i);
		             sensor_type = "Test";
		             sensor_state = 2;
		             sensor_lat = 42.729700;
		             sensor_lon = -73.680196;
		             sensor_creator = "Testor";*/
		             g = new SensorGeoCacheData(sensor_id,sensor_name,sensor_type, sensor_state, sensor_lat, sensor_lon, sensor_creator);
		     	     data.add(g);
		     	     Log.d("QD_tag",sensor_name);
		      	}
		      } catch (JSONException e){
		    	 Log.e("QD_tag","JSONException");
		    	 e.printStackTrace();
		    	 data = new ArrayList<SensorGeoCacheData>();
		      }  catch (ParseException e) {
				Log.e("QD_tag","ParseException");
				e.printStackTrace();
			  } catch (ServiceNotAvailableException e) {
				  Log.e("QD_tag","ServiceNotAvailableException");
				e.printStackTrace();
			}
		}
}
