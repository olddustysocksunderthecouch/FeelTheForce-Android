package com.bungeee.android.swapi.utilities;

import android.content.Context;

import com.bungeee.android.swapi.AppExecutors;
import com.bungeee.android.swapi.data.FilmRepository;
import com.bungeee.android.swapi.data.database.FilmDatabase;
import com.bungeee.android.swapi.data.network.StarWarsNetworkDataSource;
import com.bungeee.android.swapi.ui.detail.DetailViewModelFactory;
import com.bungeee.android.swapi.ui.list.MainViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for SWAPI APP
 */
public class InjectorUtils {

    public static FilmRepository provideRepository(Context context) {
        FilmDatabase database = FilmDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        StarWarsNetworkDataSource networkDataSource =
                StarWarsNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return FilmRepository.getInstance(database.starWarsDao(), networkDataSource, executors);
    }

    public static StarWarsNetworkDataSource provideNetworkDataSource(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return StarWarsNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, String episode_id) {
        FilmRepository repository = provideRepository(context.getApplicationContext());
        return new DetailViewModelFactory(repository, episode_id);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        FilmRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

}