package com.grtpath.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.fima.cardsui.views.CardUI;
import com.grtpath.R;
import com.grtpath.database.DatabaseAssetHelper;
import com.grtpath.database.DatabaseHelper;
import com.grtpath.database.contract.DatabaseContract.FavouriteStopsEntry;
import com.grtpath.model.MyPlayCard;

public class FavouritesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourites);
		setupActionBar();
		displayFavourites();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.favourites, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void displayFavourites() {
		Log.i(FavouritesActivity.class.getName(), "Displaying favourites");

		List<Integer> favList = listFavouriteStopIds();
		if (favList.isEmpty()) {
			return;
		}
		
		//get GRT DB
		DatabaseAssetHelper dbHelper = new DatabaseAssetHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		StringBuilder stopIds = new StringBuilder();
		for (int i : favList) {
			stopIds.append(i);
			if (i != favList.get(favList.size() - 1)) {
				stopIds.append(",");
			}
		}
		
		Cursor cursor = db.query("stops", 
				new String[]{"stop_id", "stop_name"}, 
				"stop_id in (" + stopIds.toString() + ")",
				null, null, null, null);
		displayStops(cursor);
		db.close();
	}
	
	public void displayStops(Cursor cursor) {
		// init CardView
		CardUI mCardView = (CardUI) findViewById(R.id.favourites_cardview);
		mCardView.setSwipeable(false);
		mCardView.clearCards();
		// switch card color
		String color = "#33b6ea";
		if (cursor.moveToFirst()) {
			// list all available stops
			do {
				String stopName = cursor.getString(cursor.getColumnIndex("stop_name"));
				final Integer stopId = cursor.getInt(cursor.getColumnIndex("stop_id"));
				MyPlayCard favCard = new MyPlayCard(stopId + " " + stopName,
						stopName, color, color, false, true);
				// on click, display routes for that stop
				favCard.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(FavouritesActivity.this, DisplayRoutesActivity.class);
						intent.putExtra("stop_id", stopId);
						startActivity(intent);
					}
				});
				mCardView.addCard(favCard);
			} while (cursor.moveToNext());
		}
		// draw cards
		mCardView.refresh();
	}
	
	/**
	 * Return a list of favourite stop ids
	 * @return
	 */
	public List<Integer> listFavouriteStopIds() {
		List<Integer> favList = new ArrayList<Integer>();
		//get readable database
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String selectAllQuery = "SELECT * FROM " + FavouriteStopsEntry.TABLE_NAME;
		Cursor c = db.rawQuery(selectAllQuery, null);
		 // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	            int index = c.getColumnIndex(FavouriteStopsEntry.COLUMN_NAME_STOP_ID);
	            favList.add(c.getInt(index));
	        } while (c.moveToNext());
	    }
	    db.close();
	    dbHelper.close();
		return favList;
	}
}
