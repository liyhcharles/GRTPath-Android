package com.grtpath.activity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTimeComparator;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.fima.cardsui.views.CardUI;
import com.grtpath.R;
import com.grtpath.database.DatabaseHelper;
import com.grtpath.model.MyPlayCard;

public class DisplayRoutesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_routes);
		setupActionBar();
		
		Intent intent = getIntent();
		String stopId = intent.getStringExtra("stop_id");
		
		try {
			displayRoutes(stopId);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Display routes
	 * @throws ParseException 
	 */
	private void displayRoutes(String stopId) throws ParseException {
		Log.i("DisplayRoutesActivity", "Displaying routes for " + stopId);
		
		// init CardView
		CardUI mCardView = (CardUI) findViewById(R.id.routes_cardview);
		mCardView.setSwipeable(false);
		
		SQLiteDatabase db = DatabaseHelper.getStaticDb();
		
		// get today's date in ######## format
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		String todayDate = dateFormat.format(cal.getTime());
		
		// get today's day name (i.e. Sunday)
		dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
		String dayName = dateFormat.format(cal.getTime());
		
		// find the service_id
		String serviceSQL = "select service_id from calendar " +
		"where '" + todayDate + "' >= start_date " + 
		"and '" + todayDate + "' <= end_date " + 
		"and " + dayName + " = '1'";
		
		Cursor serviceCursor = db.rawQuery(serviceSQL, null);
		
		String serviceName = "";
		
		// expect only one service
		if (serviceCursor.moveToFirst()) {
			if (serviceCursor.getCount() > 1) {
				Log.e("DisplayRoutesActivity", "More than one service found");
			}
			serviceName = serviceCursor.getString(serviceCursor.getColumnIndex("service_id"));
		}
		else {
			Log.e("DisplayRoutesActivity", "No services found");
		}
				
		// sql which displays all available routes today in the next hour
		String sql = "select distinct T2.trip_headsign, T1.arrival_time " + 
		"from trips as T2 " + 
		"JOIN stop_times as T1 " + 
		"ON T1.trip_id = T2.trip_id " +
		"and T1.stop_id  = '" + stopId + "' " +
		//"and time(T1.arrival_time) <= time('now', 'localtime', '+60 minutes') " +
		//"and time(T1.arrival_time) >= time('now', 'localtime', '-60 minutes') " +
		"and T2.service_id = '" + serviceName + "' " +
		"order by T1.arrival_time"; 
		
		Cursor cursor = db.rawQuery(sql, null);
		
		// display all routes for this stop
		if (cursor.moveToFirst()) {
			do {
				String tripName = cursor.getString(cursor.getColumnIndex("trip_headsign"));
				String arrivalTime = cursor.getString(cursor.getColumnIndex("arrival_time"));
				MyPlayCard routeCard;
				dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
				DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
				int comparison = comparator.compare((Date)dateFormat.parse(arrivalTime), new Timestamp(new Date().getTime()));
				if (comparison < 0) {
					routeCard = new MyPlayCard(tripName,
							arrivalTime, "#e00707", "#e00707", false, true);
				}
				else {
					routeCard = new MyPlayCard(tripName,
							arrivalTime, "#33b6ea", "#33b6ea", false, true);
				}				
				mCardView.addCard(routeCard);
			} while (cursor.moveToNext());
		}
		else {
			findViewById(R.id.nostops).setVisibility(1);
		}
				
		// draw cards
		mCardView.refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_routes, menu);
		return true;
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

}
