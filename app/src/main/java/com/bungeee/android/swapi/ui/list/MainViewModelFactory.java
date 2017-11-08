package com.bungeee.android.swapi.ui.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.bungeee.android.swapi.data.FilmRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link FilmRepository} and an ID for the current {@link com.bungeee.android.swapi.data.database.FilmEntry}
 */


//  The constructor that is automatically called by ViewModelProvider is the
//  default one - it takes no arguments. If you want to create a different constructor
//	for a view model, you'll need to make a view model provider factory. The starting
//	code can be found if you uncomment DetailViewModelFactory:
public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

	private final FilmRepository mRepository;

	public MainViewModelFactory(FilmRepository repository) {
		this.mRepository = repository;
	}

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(mRepository);
    }
}
