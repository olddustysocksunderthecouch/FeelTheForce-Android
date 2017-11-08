package com.bungeee.android.swapi.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.bungeee.android.swapi.utilities.InjectorUtils;

/**
 * An {@link IntentService} subclass for immediately scheduling a sync with the server off of the
 * main thread. This is necessary because {@link com.firebase.jobdispatcher.FirebaseJobDispatcher}
 * will not trigger a job immediately. This should only be called when the application is on the
 * screen.
 */
public class StarWarsSyncIntentService extends IntentService {
    private static final String LOG_TAG = StarWarsSyncIntentService.class.getSimpleName();

    public StarWarsSyncIntentService() {
        super("StarWarsSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "Intent service started");
        StarWarsNetworkDataSource networkDataSource = InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchFilmsCharacters();
    }
}