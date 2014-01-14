package com.grtpath.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.fima.cardsui.views.CardUI;
import com.grtpath.R;
import com.grtpath.model.MyPlayCard;
import com.grtpath.database.DatabaseHelper;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createCards();		
		try {
			DatabaseHelper.createDatabaseIfNotExists(this);
		} catch (IOException e) {
			Log.e("MainActivity", "Database IO Exception");
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the card UI
	 */
	private void createCards() {
		Log.i("MainActivity", "Creating cards for MainActivity...");

		// init CardView
		CardUI mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(false);

		MyPlayCard favourites = new MyPlayCard("Favourites",
				"Favourited bus stops \n", "#33b6ea",
				"#33b6ea", false, true);
		
		mCardView.addCard(favourites);
		
		MyPlayCard stops = new MyPlayCard(
				"Search for Stops",
				"Look for a stop by name, number or location",
				"#e00707", "#e00707", false, true);
		
		stops.setOnClickListener(new OnClickListener() {
			 @Override
             public void onClick(View v) {
				 Intent intent = new Intent(MainActivity.this, StopFinderActivity.class);
				 startActivity(intent);
			 }
		});
		
		mCardView.addCard(stops);

		mCardView
				.addCard(new MyPlayCard(
						"Get Directions",
						"View a map \n",
						"#f2a400", "#9d36d0", false, true));

		// draw cards
		mCardView.refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
