package com.bungeee.android.swapi.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bungeee.android.swapi.data.FilmRepository;
import com.bungeee.android.swapi.data.database.FilmEntry;

/**
 * {@link ViewModel} for {@link DetailActivity}
 */
public class DetailActivityViewModel extends ViewModel {

    // Weather forecast the user is looking at
	private final LiveData<FilmEntry> mWeather;
	//private final LiveData<CharacterEntry> mCharacters;
	// Date for the weather forecast
    private final String mEpisode_id;
    private final FilmRepository mRepository;


	public DetailActivityViewModel(FilmRepository repository, String episode_id){
		mRepository = repository;
		mEpisode_id = episode_id;
		mWeather = mRepository.getFilmByEpisodeId(mEpisode_id);
		//mCharacters = mRepository.findCharacterName
	}

    public LiveData<FilmEntry> getWeather() {
        return mWeather;
    }



}
