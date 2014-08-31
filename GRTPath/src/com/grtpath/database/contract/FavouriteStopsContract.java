package com.grtpath.database.contract;

import android.provider.BaseColumns;

public final class FavouriteStopsContract {

    public FavouriteStopsContract() {}

    //schema for favourite stops
    public static abstract class FavouriteStopsEntry implements BaseColumns {
        public static final String TABLE_NAME = "favourite_stops";
        public static final String COLUMN_NAME_FAVOURITES_ID = "id";
        public static final String COLUMN_NAME_STOP_NAME = "stop_name";
        public static final String COLUMN_NAME_STOP_ID = "stop_id";
    }
	
}
