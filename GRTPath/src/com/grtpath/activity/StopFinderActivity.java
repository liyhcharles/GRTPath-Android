package com.grtpath.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SearchView;

import com.fima.cardsui.views.CardUI;
import com.grtpath.R;
import com.grtpath.database.DatabaseAssetHelper;
import com.grtpath.model.MyPlayCard;

public class StopFinderActivity extends Activity {
	
	private static String searchResult = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stop_finder);
		setupActionBar();
		handleIntent(getIntent());
	}
	
	/**
	 * Displays stops from search result
	 */
	private void displayStops() {
		Log.i("StopFinderActivity", "Displaying stops");

		// init CardView
		CardUI mCardView = (CardUI) findViewById(R.id.stops_cardview);
		mCardView.setSwipeable(false);
		
		mCardView.clearCards();
		
		// get database and cursor
		DatabaseAssetHelper dbHelper = new DatabaseAssetHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
				
		Cursor cursor = null;
		
		if (isInteger(searchResult)) {
			cursor = db.query("stops", 
					new String[]{"stop_id", "stop_name"}, 
					"stop_id=\"" + searchResult + "\"", 
					null, null, null, null);
		}
		else {
			cursor = db.query("stops", 
					new String[]{"stop_id", "stop_name"}, 
					"stop_name like \"%" + searchResult + "%\"", 
					null, null, null, null);
		}
		
		// switch card color
		String color = "#33b6ea";
				
		if (cursor.moveToFirst()) {
			// list all available stops
			do {
				String stopName = cursor.getString(cursor.getColumnIndex("stop_name"));
				final String stopId = cursor.getString(cursor.getColumnIndex("stop_id"));
				MyPlayCard stopCard = new MyPlayCard(stopId + " " + stopName,
						stopName, color, color, false, true);
				// on click, display routes for that stop
				stopCard.setOnClickListener(new OnClickListener() {
					 @Override
		             public void onClick(View v) {
						 Intent intent = new Intent(StopFinderActivity.this, DisplayRoutesActivity.class);
						 intent.putExtra("stop_id", stopId);
						 startActivity(intent);
					 }
				});
				mCardView.addCard(stopCard);
				color = switchColor(color);
			} while (cursor.moveToNext());
		}
				
		// draw cards
		mCardView.refresh();
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	/**
	 * Switches colors for cards
	 * @param color
	 * @return
	 */
	private String switchColor(String color) {
		if (color.equals("#33b6ea")) {
			color = "#008000";
		}
		else if (color.equals("#008000")){
			color = "#e00707";
		}
		else {
			color = "#33b6ea";
		}
		return color;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stop_finder, menu);
		
		// Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		
		return true;
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    handleIntent(intent);
	}

	/**
	 * Intent handler, used for search handling
	 * @param intent
	 */
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			Log.i("StopFinderActivity", "Handling search action");
			searchResult = intent.getStringExtra(SearchManager.QUERY).replaceAll("\\s+$", "");
			displayStops();
	    }
	}

}
