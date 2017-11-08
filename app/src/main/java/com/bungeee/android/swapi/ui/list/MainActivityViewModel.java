package com.bungeee.android.swapi.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bungeee.android.swapi.data.FilmRepository;
import com.bungeee.android.swapi.data.database.FilmEntry;
import com.bungeee.android.swapi.ui.detail.DetailActivity;

import java.util.List;

/**
 * {@link ViewModel} for {@link DetailActivity}
 */
public class MainActivityViewModel extends ViewModel {

    // Weather forecast the user is looking at
	private final LiveData<List<FilmEntry>> mForecast;
	// Date for the weather forecast
    private final FilmRepository mRepository;

	public MainActivityViewModel(FilmRepository repository){
		mRepository = repository;
		mForecast = mRepository.getCurrentFilmsEntries();
	}

    public LiveData<List<FilmEntry>> getFilms() {
        return mForecast;
    }

}
