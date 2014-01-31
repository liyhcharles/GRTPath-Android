package com.grtpath.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DatabaseHelper {
	
	private static final String DB_PATH = Environment.getDataDirectory() + "/data/com.grtpath/databases/";
    private static final String DB_NAME = "GRTDB.sqlite";
    //private static final int DB_VERSION = 1;

    public static void createDatabaseIfNotExists(Context context) throws IOException {
        boolean createDb = false;

        File dbDir = new File(DB_PATH);
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbDir.exists()) {
        	Log.i("DatabaseHelper", "Database directory does not exist, creating it and database.");
            dbDir.mkdir();
            createDb = true;
        }
        else if (!dbFile.exists()) {
        	Log.i("DatabaseHelper", "Database does not exist, creating it.");
            createDb = true;
        }
        else {
        	Log.i("DatabaseHelper", "Database already exists, checking for updates.");
            // Check that we have the latest version of the db
            boolean doUpgrade = false;

            // INSERT UPGRADE LOGIC

            // upgrade the DB
            if (doUpgrade) {
                dbFile.delete();
                createDb = true;
            }
        }

        if (createDb) {
        	Log.i("DatabaseHelper", "Creating database...");
        	
            // Open your local db as the input stream
            InputStream myInput = context.getAssets().open(DB_NAME);

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(dbFile);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
            
            Log.i("DatabaseHelper", "Database was created.");
        }
    }

    public static SQLiteDatabase getStaticDb() {
        return SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    }
}
