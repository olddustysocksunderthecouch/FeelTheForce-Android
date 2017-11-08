package com.bungeee.android.swapi.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.bungeee.android.swapi.R;
import com.bungeee.android.swapi.utilities.InjectorUtils;

/**
 * Displays a list of all the Star Wars Films
 */
public class MainActivity extends AppCompatActivity {

    private FilmAdapter mFilmAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;

    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if(getSupportActionBar() != null){
			getSupportActionBar().setTitle("STAR WARS FILMS");
		}

        mRecyclerView = findViewById(R.id.film_recycler_view);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        // Get the ViewModel from the factory
        MainViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        // Observers changes in the FilmEntry with the id mId
        mViewModel.getFilms().observe(this, filmEntries -> {
            mFilmAdapter.swapFilms(filmEntries);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.smoothScrollToPosition(mPosition);

            // Show the weather list or the loading screen based on whether the Films data exists
            // and is loaded
            if (filmEntries != null && filmEntries.size() != 0) showWeatherDataView();
            else showLoading();
        });

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mFilmAdapter = new FilmAdapter(this);
        mRecyclerView.setAdapter(mFilmAdapter);
        showLoading();
    }


    /**
     * This method will make the View for the film data visible and hide the error message and
     * loading indicator.
     */
    private void showWeatherDataView() {
        // First, hide the loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        // Finally, make sure the weather data is visible
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the film View and error
     * message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showLoading() {
        // Then, hide the weather data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Finally, show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}
