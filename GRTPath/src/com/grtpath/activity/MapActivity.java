package com.grtpath.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grtpath.R;
import com.grtpath.database.DatabaseAssetHelper;

public class MapActivity extends Activity implements OnInfoWindowClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		// Get a handle to the Map Fragment
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        
        map.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);
        
        double latitude;
        double longitude;
        
        // move camera to location if possible
        if (myLocation != null) {
        	// Get latitude of the current location
        	latitude = myLocation.getLatitude();
        	// Get longitude of the current location
            longitude = myLocation.getLongitude();
            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);
            // Show the current location in Google Map        
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        else {
        	Log.e("MapActivity", "NO LOCATION FOUND");
        }
        
        // Zoom in the Google Map
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        
        map.setOnInfoWindowClickListener(this);
        
        // get database and cursor
        DatabaseAssetHelper dbHelper = new DatabaseAssetHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();  
        
        Cursor cursor = null;
        cursor = db.query("stops", 
        		new String[]{"stop_lat", "stop_lon", "stop_id", "stop_name"}, 
        		null, null, null, null, null);
        
        // add stops to map
        if (cursor.moveToFirst()) {
        	do {
        		latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("stop_lat")));
        		longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("stop_lon")));
        		String stopId = cursor.getString(cursor.getColumnIndex("stop_id"));
        		String stopName = cursor.getString(cursor.getColumnIndex("stop_name"));
        		map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(stopId + " " + stopName));
        	} while (cursor.moveToNext());
        }
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		String title = marker.getTitle();
		Integer stopId = Integer.valueOf(title.split("\\s")[0]);
		
		Intent intent = new Intent(MapActivity.this, DisplayRoutesActivity.class);
		intent.putExtra("stop_id", stopId);
		startActivity(intent);
	}

}
