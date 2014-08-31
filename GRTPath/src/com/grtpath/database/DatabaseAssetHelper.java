package com.grtpath.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * This database will never have a new version, as it is always overwritten
 * Used for stop information, read-only
 * @author Charles
 *
 */
public class DatabaseAssetHelper extends SQLiteAssetHelper {
	
	private static final String DATABASE_NAME = "GRTDB.sqlite";
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseAssetHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
}
