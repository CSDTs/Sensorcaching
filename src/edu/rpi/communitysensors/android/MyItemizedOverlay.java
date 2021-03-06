package edu.rpi.communitysensors.android;

import java.util.ArrayList;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.GeoPoint;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem>{
	private ArrayList<OverlayItem> myOverlays;
	
	public MyItemizedOverlay(Drawable defaultMarker){
		super(boundCenterBottom(defaultMarker));
		myOverlays = new ArrayList<OverlayItem>();
		populate();
	}
	
	public void addOverlay(OverlayItem overlay){
		myOverlays.add(overlay);
		populate();
	}
	
	@Override
	protected OverlayItem createItem(int i){
		return myOverlays.get(i);
	}
	
	public void removeItem(int i){
		myOverlays.remove(i);
		populate();
	}
	
	@Override
	public int size(){
		return myOverlays.size();
	}
	
	public void addOverlayItem(OverlayItem overlayItem){
		myOverlays.add(overlayItem);
		populate();
	}
	
	public void addOverlayItem(int lat,int lon, String title){
		try {
            GeoPoint point = new GeoPoint(lat, lon);
            OverlayItem overlayItem = new OverlayItem(point, title, null);
            addOverlayItem(overlayItem);    
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
	}
	
	@Override
	protected boolean onTap(int index){
		String title = myOverlays.get(index).getTitle();
	//	Toast.makeText(GoogleRouteActivity.context, title, Toast.LENGTH_LONG).show();
        return super.onTap(index);
	}
}

