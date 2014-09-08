package com.grtpath.database.contract;

import android.provider.BaseColumns;

public final class DatabaseContract {

	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	
    public DatabaseContract() {}

    //schema for favourite stops
    public static abstract class FavouriteStopsEntry implements BaseColumns {
        public static final String TABLE_NAME = "favourite_stops";
        public static final String COLUMN_NAME_FAVOURITES_ID = "fav_id";
        public static final String COLUMN_NAME_STOP_NAME = "stop_name";
        public static final String COLUMN_NAME_STOP_ID = "stop_id";
        
        public static final String CREATE_TABLE =  "CREATE TABLE " + FavouriteStopsEntry.TABLE_NAME + " (" +
	    		FavouriteStopsEntry._ID + INTEGER_TYPE + " PRIMARY KEY," +
	    		FavouriteStopsEntry.COLUMN_NAME_FAVOURITES_ID + INTEGER_TYPE + COMMA_SEP +
	    		FavouriteStopsEntry.COLUMN_NAME_STOP_NAME + TEXT_TYPE + COMMA_SEP +
	    		FavouriteStopsEntry.COLUMN_NAME_STOP_ID + INTEGER_TYPE + " )";
        
        //TODO only insert unique stopIds
        public static String insertQuery(String stopName, Integer stopId) {
        	return "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME_FAVOURITES_ID + COMMA_SEP +
        			COLUMN_NAME_STOP_NAME + COMMA_SEP + COLUMN_NAME_STOP_ID + ") " +
        			"VALUES ( 1" + COMMA_SEP + "'" + stopName + "'" + COMMA_SEP + stopId + ");";
        }
    }
	
}
