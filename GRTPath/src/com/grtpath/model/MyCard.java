package com.grtpath.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;
import com.grtpath.R;

/**
 * This class is currently not used
 * @author Charles
 *
 */
public class MyCard extends Card {
	
	public MyCard(String text){
		super(text);
	}

	@Override
	public boolean convert(View arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_ex, null);

		((TextView) view.findViewById(R.id.title)).setText(title);
		
		return view;
	}


}
