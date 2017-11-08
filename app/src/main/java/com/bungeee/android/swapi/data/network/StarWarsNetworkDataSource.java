package com.bungeee.android.swapi.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bungeee.android.swapi.AppExecutors;
import com.bungeee.android.swapi.data.database.CharacterEntry;
import com.bungeee.android.swapi.data.database.FilmEntry;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Provides an API for doing all operations with the server data
 */
public class StarWarsNetworkDataSource {
    // The number of days we want our API to return, set to 14 days or two weeks
    public static final int NUM_DAYS = 5;

    private static final String LOG_TAG = StarWarsNetworkDataSource.class.getSimpleName();

    // Interval at which to sync with the weather. Use TimeUnit for convenience, rather than
    // writing out a bunch of multiplication ourselves and risk making a silly mistake.
    private static final int SYNC_INTERVAL_HOURS = 24;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String SWAPIAPP_SYNC_TAG = "swapi-sync";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static StarWarsNetworkDataSource sInstance;
    private final Context mContext;

    // LiveData storing the latest downloaded weather forecasts
    private final MutableLiveData<FilmEntry[]> mDownloadedFilmEntries;
	private final MutableLiveData<CharacterEntry[]> mDownloadedCharacters;

    private final AppExecutors mExecutors;

    private StarWarsNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedFilmEntries = new MutableLiveData<FilmEntry[]>();
		mDownloadedCharacters = new MutableLiveData<CharacterEntry[]>();
    }

    /**
     * Get the singleton for this class
     */
    public static StarWarsNetworkDataSource getInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new StarWarsNetworkDataSource(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    public LiveData<FilmEntry[]> getCurrentFilmEntries() {
        return mDownloadedFilmEntries;
    }

    /**
     * Starts an intent service to fetch the weather.
     */
    public void startFetchWeatherService() {
        Intent intentToFetch = new Intent(mContext, StarWarsSyncIntentService.class);
        mContext.startService(intentToFetch);
        Log.d(LOG_TAG, "Service created");
    }

    /**
     * Schedules a repeating job service which fetches the weather.
     */
    public void scheduleRecurringFetchWeatherSync() {
        Driver driver = new GooglePlayDriver(mContext);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // Create the Job to periodically sync SWAPI APP
        Job syncSunshineJob = dispatcher.newJobBuilder()
                /* The Service that will be used to sync SWAPI APP's data */
                .setService(SunshineFirebaseJobService.class)
                /* Set the UNIQUE tag used to identify this Job */
                .setTag(SWAPIAPP_SYNC_TAG)
                /*
                 * Network constraints on which this Job should run. We choose to run on any
                 * network, but you can also choose to run only on un-metered networks or when the
                 * device is charging. It might be a good idea to include a preference for this,
                 * as some users may not want to download any data on their mobile plan.
                 */
                .setConstraints(Constraint.ON_ANY_NETWORK)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                /*
                 * We want SWAPI APP's data to stay up to date, so we tell this Job to recur.
                 */
                .setRecurring(true)
                /*
                 * We want the weather data to be synced every 24 hours. The first argument for
                 * Trigger's static executionWindow method is the start of the time frame when the
                 * sync should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();

        // Schedule the Job with the dispatcher
        dispatcher.schedule(syncSunshineJob);
        Log.d(LOG_TAG, "Job scheduled");
    }

    /**
     * Gets the newest weather
     */
    void fetchFilmsCharacters() {
        Log.d(LOG_TAG, "Fetch data started");
        mExecutors.networkIO().execute(() -> {

        	//RETRIEVE FILMS
            try {
                URL weatherRequestUrl = NetworkUtils.getUrl();

                // Use the URL to retrieve the JSON
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

                // Parse the JSON into a list of films
                ResponseFilm response = new JsonParserFilm().parse(jsonWeatherResponse);
                Log.d(LOG_TAG, "JSON Parsing finished");

                // As long as there are films, update the LiveData storing the most recent
                // films. This will trigger observers of that LiveData, such as the
                // FilmRepository.
                if (response != null && response.getFilmEntries().length != 0) {
                    Log.d(LOG_TAG, "JSON not null and has " + response.getFilmEntries().length
                            + " values");
                    Log.d(LOG_TAG, "First value is" +
                            response.getFilmEntries()[0].getTitle()+
                            response.getFilmEntries()[0].getDirector());

                    mDownloadedFilmEntries.postValue(response.getFilmEntries());
                }
            } catch (Exception e) {
                // Server probably invalid
                e.printStackTrace();
            }
			//RETRIEVE CHARACTERS
			try {
				URL characterRequestUrl = NetworkUtils.getCharacterUrl();

				// Use the URL to retrieve the JSON
				String jsonCharacterResponse = NetworkUtils.getResponseFromHttpUrl(characterRequestUrl);

				// Parse the JSON into a list of characters
				ResponseCharacter response = new JsonParserCharacter().parse(jsonCharacterResponse);
				Log.d(LOG_TAG, "JSON Parsing finished");

				// As long as there are characters, update the LiveData storing the most recent
				// characters. This will trigger observers of that LiveData, such as the
				// FilmRepository.
				if (response != null && response.getmCharacterEntries().length != 0) {
					Log.d(LOG_TAG, "JSON not null and has " + response.getmCharacterEntries().length
							+ " values");
					Log.d(LOG_TAG, "First value is" +
							response.getmCharacterEntries()[0].getName()+
							response.getmCharacterEntries()[0].getUrl());

					mDownloadedCharacters.postValue(response.getmCharacterEntries());
				}
			} catch (Exception e) {
				// Server probably invalid
				e.printStackTrace();
			}
        });

    }

}