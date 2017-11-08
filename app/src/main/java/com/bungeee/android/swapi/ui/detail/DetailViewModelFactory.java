package com.bungeee.android.swapi.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.bungeee.android.swapi.data.FilmRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link FilmRepository} and an ID for the current {@link com.bungeee.android.swapi.data.database.FilmEntry}
 */


//  The constructor that is automatically called by ViewModelProvider is the
//  default one - it takes no arguments. If you want to create a different constructor
//	for a view model, you'll need to make a view model provider factory.
public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

	private final FilmRepository mRepository;
    private final String mEpisode_id;

	public DetailViewModelFactory(FilmRepository repository, String episode_id) {
		this.mRepository = repository;
		this.mEpisode_id = episode_id;
	}

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailActivityViewModel(mRepository, mEpisode_id);
    }
}
