package edu.rpi.communitysensors.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import edu.rpi.communitysensors.android.Road;
import edu.rpi.communitysensors.android.RoadProvider;
import edu.rpi.communitysensors.android.GetCurrentLocation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class GoogleRouteActivity extends MapActivity {

        LinearLayout linearLayout;
        MapView mapView;
        MapController mapController;
        GetCurrentLocation me;
        private Road mRoad;	
    	Context mcontext;
    	
    	private String act_title;
    	private double toLat;
    	private double toLon;
    	private int state;

        @Override
        public void onCreate(Bundle savedInstanceState) {  	        	    
        	    Log.e("logtag","onCreate Called");
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
			    //The following line takes the element with key name "title" and stores it as a String
			    act_title=extras.getString("title");
			    //The following line takes the element with key name "lat" and stores it as a double
			    toLat=extras.getDouble("latitude");
			    //The following line takes the element with key name "lon" and stores it as a double
			    toLon=extras.getDouble("longitude");
			    //The following line takes the element with key name "state" and stores it as a int
			    state=extras.getInt("state");
			    
			    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
			    TextView title = (TextView) findViewById(R.id.title);
		        title.setText("  "+ act_title);
			    
                mapView = (MapView) findViewById(R.id.mapview);
                mapView.setBuiltInZoomControls(true);
                mcontext=getApplication();
                run();
        }
        
        public void run() {
           	   mapView.setBuiltInZoomControls(true);
           	   //Log.e("logtag","setBuiltInZoomControls function called");
           	   me = new GetCurrentLocation(mcontext);
           	   Location hereiam = me.getLocation();     	    
               double fromLat = me.getLatitude(hereiam), fromLon =me.getLongitude(hereiam);
           	  // double fromLat = 50.3453, fromLon = -70;
           	  // toLat = 40.7155;
              // toLon = -74.0100;
             	    
             	/*   drawable = this.getResources().getDrawable(R.drawable.questionmark);
                   itemizedOverlay = new MyItemizedOverlay(drawable);
                   GeoPoint point_src = new GeoPoint((int)(fromLat*1000000),(int)(fromLon*1000000));
                   GeoPoint point_desc = new GeoPoint((int)(toLat*1000000),(int)(toLon*1000000));
                  
                   mapController.animateTo(point);
                  OverlayItem overlayitem = new OverlayItem(point,"", "");
                  
                  GeoPoint point2 = new GeoPoint(35410000, 139460000);
                  OverlayItem overlayitem2 = new OverlayItem(point2, "", "");
                  
                  itemizedOverlay.addOverlay(overlayitem);
                  itemizedOverlay.addOverlay(overlayitem2);
                  
                  mapOverlays.add(itemizedOverlay);*/
             	   
             	               	   
                 String url = RoadProvider.getUrl(fromLat, fromLon, toLat, toLon);
                 InputStream is = getConnection(url);
                 mRoad = RoadProvider.getRoute(is);
                 mHandler.sendEmptyMessage(0);
        }
                
                
        Handler mHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                        TextView textView = (TextView) findViewById(R.id.textView1);
                        textView.setText(mRoad.mName + " " + mRoad.mDescription);
                        //mapController = mapView.getController();                        
                        MapOverlay mapOverlay = new MapOverlay(mRoad, mapView,mcontext , state);
                        //mapController.animateTo(mapOverlay.moveTo);
                        //mapController.setZoom(7);
                        List<Overlay> listOfOverlays = mapView.getOverlays();
                        listOfOverlays.clear();
                        listOfOverlays.add(mapOverlay);
                        listOfOverlays.add(mapOverlay.itemizedOverlay);
                        mapView.invalidate();
                };
        };

        private InputStream getConnection(String url) {
                InputStream is = null;
                try {
                        URLConnection conn = new URL(url).openConnection();
                        is = conn.getInputStream();
                        Log.e("logtag","conn.getInputStream called");
                } catch (MalformedURLException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return is;
        }

        @Override
    	protected void onResume() {       	
    		super.onResume();
    	}
    		
    	@Override
    	protected void onPause() {
    		me.stopLocationUpdate();
    		super.onPause();
    	}
        
        @Override
        protected boolean isRouteDisplayed() {
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
    		i.setClass(GoogleRouteActivity.this, HelpMenu.class);	
    		startActivity(i);
        }
        private void showSettings(){
        	//
        }
} 

class MapOverlay extends com.google.android.maps.Overlay {
        Road mRoad;
        ArrayList<GeoPoint> mPoints;
        private Context ctx=null;
        private int ZoomLevel = 13;
        
     //	private MyItemizedOverlay userPicOverlay;
    	//private MyItemizedOverlay nearPicOverlay;
    	private Drawable userPic,atmPic,drawable;
    	MyItemizedOverlay itemizedOverlay;
        
        public MapOverlay(Road road, MapView mv, Context ctx, int state) {
        	    this.ctx=ctx;
                mRoad = road;
                //Log.e("logtag","started mapoverlay");
                Log.e("logtag", "route length:"+ String.valueOf(road.mRoute.length));
                
                if (road.mRoute.length > 0) {
                	    //Log.e("logtag","ArrayList");
                	    
                        mPoints = new ArrayList<GeoPoint>();
                        for (int i = 0; i < road.mRoute.length; i++) {
                                mPoints.add(new GeoPoint((int) (road.mRoute[i][1] * 1000000),
                                                (int) (road.mRoute[i][0] * 1000000)));
                        }
                        
                        int moveToLat = (mPoints.get(0).getLatitudeE6() + (mPoints.get(
                                        mPoints.size() - 1).getLatitudeE6() - mPoints.get(0)
                                        .getLatitudeE6()) / 2);
                        
                        int moveToLong = (mPoints.get(0).getLongitudeE6() + (mPoints.get(
                                        mPoints.size() - 1).getLongitudeE6() - mPoints.get(0)
                                        .getLongitudeE6()) / 2);
                        GeoPoint moveTo = new GeoPoint(moveToLat, moveToLong);

                        //Log.e("logtag","map controller");
                        MapController mapController = mv.getController();
                        
                        //Log.e("logtag","animiate");
                        mapController.animateTo(moveTo);
                        mapController.setZoom(ZoomLevel);
                        
                        if(state == 0)         
                        	drawable = ctx.getResources().getDrawable(R.drawable.new_map);
                        else if (state == 1)
                        	drawable = ctx.getResources().getDrawable(R.drawable.young_map);
                        else
                        	drawable = ctx.getResources().getDrawable(R.drawable.old_map);
                        
                        //Log.e("logtag","my itemizedoverlay");
                        itemizedOverlay = new MyItemizedOverlay(drawable);
                        GeoPoint point_src = mPoints.get(0);
                        GeoPoint point_desc = mPoints.get(mPoints.size()-1);
                        
                        //Log.e("logtag","add new overlayitem");
                        OverlayItem overlayitem = new OverlayItem(point_src,"","");
                        OverlayItem overlayitem2 = new OverlayItem(point_desc,"","");
                        
                        //Log.e("logtag","add overlay");
                        itemizedOverlay.addOverlay(overlayitem);
                        itemizedOverlay.addOverlay(overlayitem2);
                        //mapOverlays.add(itemizedOverlay);
                        
                }
        }

        @Override
        public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
                super.draw(canvas, mv, shadow);
                drawPath(mv, canvas);
                return true;
        }
        
        public void drawPath(MapView mv, Canvas canvas) {
               int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
                Paint paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                
           for (int i = 0; i < mPoints.size(); i++) {
                        Point point = new Point();
                       //Log.e("logtag", "before mPoints.get(i)");
                        GeoPoint points = mPoints.get(i);
                        float lat = points.getLatitudeE6();
                        float lon = points.getLongitudeE6();
                        //Log.e("logtag", "Lat: "+ Float.toString(lat)+" Long: "+Float.toString(lon));
                        //Log.e("logtag", "before the getProjection");
                        mv.getProjection().toPixels(mPoints.get(i), point);
                        x2 = point.x;
                        y2 = point.y;
 
                        if (i > 0) {
                                canvas.drawLine(x1, y1, x2, y2, paint);
                        }
                        x1 = x2;
                        y1 = y2;
                }
        }
}


