package com.bungeee.android.swapi.ui.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bungeee.android.swapi.R;
import com.bungeee.android.swapi.data.database.FilmEntry;
import com.bungeee.android.swapi.ui.detail.DetailActivity;
import com.bungeee.android.swapi.utilities.StarWarsFilmsUtils;

import java.util.List;

/**
 * Exposes a list of weather forecasts from a list of {@link FilmEntry} to a {@link RecyclerView}.
 */
class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ForecastAdapterViewHolder> {

    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private final Context mContext;

    private List<FilmEntry> mFilms;

    /**
     * Creates a FilmAdapter.
     * @param context      Used to talk to the UI and app resources
     */
    FilmAdapter(@NonNull Context context) {
        mContext = context;

    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (like ours does) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.film_list_item, viewGroup, false);
        view.setFocusable(true);
        return new ForecastAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param forecastAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        FilmEntry currentWeather = mFilms.get(position);

        /****************
         *  Film Poster *
         ****************/
        String weatherIconId = currentWeather.getEpisode_id();
        String weatherImageResourceId = StarWarsFilmsUtils
                .getLargeArtResourceIdForWeatherCondition(weatherIconId);

		Glide.with(mContext)
				.load(weatherImageResourceId)
				.into(forecastAdapterViewHolder.iconView);

        /**************
         * Film Title *
         **************/
        String title = currentWeather.getTitle();
        forecastAdapterViewHolder.titleView.setText(title);

		/***************
		 *  Episode Num *
		 ***************/
		String episodeNum = currentWeather.getEpisode_id();
		String romanNumeral = StarWarsFilmsUtils.getRomanNumeral(episodeNum);
		forecastAdapterViewHolder.episodeView.setText(romanNumeral);

		/*****************
		 *  Release Date *
		 *****************/
		String releaseDate = currentWeather.getRelease_date();
		forecastAdapterViewHolder.releaseDateView.setText(releaseDate);

		/************************************************
		 * CardView OnClick & SharedElement Transitions *
		 ************************************************/
		forecastAdapterViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent(mContext, DetailActivity.class);
				intent.putExtra(DetailActivity.EPISODE_ID_EXTRA, currentWeather.getEpisode_id());
				intent.putExtra("poster", weatherImageResourceId);
				intent.putExtra("title", currentWeather.getTitle());

				Pair<View, String> p1 = Pair.create((View) forecastAdapterViewHolder.iconView, mContext.getString(R.string.image_transition));
				Pair<View, String> p2 = Pair.create((View) forecastAdapterViewHolder.titleView, mContext.getString(R.string.title_transition));

				ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, p1, p2);
				mContext.startActivity(intent, options.toBundle());
			}
		});
    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our Films
     */
    @Override
    public int getItemCount() {
        if (null == mFilms) return 0;
        return mFilms.size();
    }

    /**
     * Swaps the list used by the FilmAdapter for its film data. This method is called by
     * {@link MainActivity} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newFilms the new list of films to use as FilmAdapter's data source
     */
    void swapFilms(final List<FilmEntry> newFilms) {
        mFilms = newFilms;
        notifyDataSetChanged();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item.
     */
    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder {
        final ImageView iconView;
        final TextView titleView;
		final TextView episodeView;
		final TextView releaseDateView;
        final CardView cardView;


        ForecastAdapterViewHolder(View view) {
            super(view);

            iconView = view.findViewById(R.id.film_icon);
			titleView = view.findViewById(R.id.title);
			episodeView = view.findViewById(R.id.episode_number);
			releaseDateView = view.findViewById(R.id.release_date);
			cardView = view.findViewById(R.id.card_view);

        }

    }
}