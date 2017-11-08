package com.bungeee.android.swapi.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

/**
 * Created by Adrian on 02-Nov-17.
 */
/**
 * {@link FilmDatabase} database for the application including a table for {@link FilmEntry}
 * with the DAO {@link StarWarsDao}.
 */

// List of the entry classes and associated TypeConverters
@Database(entities = {FilmEntry.class, CharacterEntry.class}, version = 1)
@TypeConverters({ConverterArrayList.class, ConverterDate.class})
public abstract class FilmDatabase extends RoomDatabase {

	private static final String LOG_TAG = FilmDatabase.class.getSimpleName();
	private static final String DATABASE_NAME = "film";

	// For Singleton instantiation
	private static final Object LOCK = new Object();
	private static FilmDatabase sInstance;

	public static FilmDatabase getInstance(Context context) {
		Log.d(LOG_TAG, "Getting the database");
		if (sInstance == null) {
			synchronized (LOCK) {
				sInstance = Room.databaseBuilder(context.getApplicationContext(),
						FilmDatabase.class, FilmDatabase.DATABASE_NAME).build();
				Log.d(LOG_TAG, "Made new database");
			}
		}
		return sInstance;
	}

	// The associated DAOs for the database
	public abstract StarWarsDao starWarsDao();
}
