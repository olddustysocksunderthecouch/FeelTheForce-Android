package com.bungeee.android.swapi.data;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.bungeee.android.swapi.AppExecutors;
import com.bungeee.android.swapi.data.database.CharacterEntry;
import com.bungeee.android.swapi.data.database.FilmEntry;
import com.bungeee.android.swapi.data.database.StarWarsDao;
import com.bungeee.android.swapi.data.network.StarWarsNetworkDataSource;
import com.bungeee.android.swapi.utilities.StarWarsDateUtils;

import java.util.Date;
import java.util.List;

/**
 * Handles data operations in SWAPI. Acts as a mediator between {@link StarWarsNetworkDataSource}
 * and {@link StarWarsDao}
 */
public class FilmRepository {
    private static final String LOG_TAG = FilmRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static FilmRepository sInstance;
    private final StarWarsDao mStarWarsDao;
    private final StarWarsNetworkDataSource mStarWarsNetworkDataSource;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;

    private FilmRepository(StarWarsDao starWarsDao,
						   StarWarsNetworkDataSource starWarsNetworkDataSource,
						   AppExecutors executors) {
        mStarWarsDao = starWarsDao;
        mStarWarsNetworkDataSource = starWarsNetworkDataSource;
        mExecutors = executors;


		// As long as the repository exists, observe the network LiveData.
		// If that LiveData changes, update the database.
		LiveData<FilmEntry[]> networkData = mStarWarsNetworkDataSource.getCurrentFilmEntries();
		networkData.observeForever(newFilmFromNetwork -> {
			mExecutors.diskIO().execute(() -> {
				// Deletes old historical data
				//deleteOldData();
				Log.d(LOG_TAG, "Old weather deleted");

				// Insert our new weather data into Sunshine's database
				mStarWarsDao.bulkInsert(newFilmFromNetwork);
				Log.d(LOG_TAG, "New values inserted");
			});
		});
    }

    public synchronized static FilmRepository getInstance(
			StarWarsDao starWarsDao, StarWarsNetworkDataSource starWarsNetworkDataSource,
			AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new FilmRepository(starWarsDao, starWarsNetworkDataSource,
                        executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    private synchronized void initializeData() {
		// Only perform initialization once per app lifetime. If initialization has already been
		// performed, we have nothing to do in this method.
		if (mInitialized) return;
		mInitialized = true;
		// This method call triggers Sunshine to create its task to synchronize weather data
		// periodically.
		mStarWarsNetworkDataSource.scheduleRecurringFetchWeatherSync();
		mExecutors.diskIO().execute(() -> {
			//Checks if it has 14 days of weather forecast
			startFetchWeatherService();
//			if (isFetchNeeded()){
//				startFetchWeatherService();
//			}
		});

    }


	/**
	 * Database related operations
	 **/

	public LiveData<FilmEntry> getFilmByEpisodeId(String episode_id) {
		initializeData();
		return mStarWarsDao.getFilmByEpisode(episode_id);
	}
	/**
	 * Database related operations
	 **/

	public  LiveData<List<FilmEntry>> getCurrentFilmsEntries() {
		initializeData();
		//Date in preparation for ordering by date
		//Date today = StarWarsDateUtils.getNormalizedUtcDateForToday();
		return mStarWarsDao.getCurrentFlims();
	}

//	/**
//	 * Part of the attempt to return character names based on the url in FilmEntries
//	 */
//	public  LiveData<CharacterEntry> getCharaterByUrl(String url) {
//		initializeData();
//		return mStarWarsDao.getCharaterByUrl(url);
//	}

    /**
     * Network related operation
     */

    private void startFetchWeatherService() {
		mStarWarsNetworkDataSource.startFetchWeatherService();
    }

}