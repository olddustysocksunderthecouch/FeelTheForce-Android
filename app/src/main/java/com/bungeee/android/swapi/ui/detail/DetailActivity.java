package com.bungeee.android.swapi.ui.detail;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.AssetFileDescriptor;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bungeee.android.swapi.R;
import com.bungeee.android.swapi.data.database.FilmEntry;

//import com.bungeee.android.swapi.databinding;

import com.bungeee.android.swapi.ui.starwarstextcrawl.StarWarsTextView;
import com.bungeee.android.swapi.utilities.InjectorUtils;

import java.io.IOException;


/**
 * Displays single film's data
 */
public class DetailActivity extends AppCompatActivity {

	public static final String EPISODE_ID_EXTRA = "EPISODE_ID_EXTRA";
	private MediaPlayer mediaPlayer;

	private DetailActivityViewModel mViewModel;
	private TextView introTextView;
	private StarWarsTextView openingTextView;
	private TextView releasedateTextView;
	private TextView directorTextView;
	private TextView producerTextView;
	private TextView charactersTextView;

	private boolean audioPrepared;

	private boolean startRequested;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		DataBindingUtil.setContentView(this, R.layout.activity_detail);

		//Postpone the enter transition until image is loaded in this activity (Shared Element Transition)
		postponeEnterTransition();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("STAR WARS DETAILS");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView filmIcon = findViewById(R.id.film_icon);
		TextView title = findViewById(R.id.title);

		releasedateTextView = findViewById(R.id.release_date);
		directorTextView = findViewById(R.id.director_name);
		producerTextView = findViewById(R.id.producer_name);
		charactersTextView = findViewById(R.id.character_names);

		//Get intent data
		Bundle bundle = getIntent().getExtras();
		String episodeId = getIntent().getStringExtra(EPISODE_ID_EXTRA);
		//Loading image using Glide
		Glide.with(this)
				.load(bundle.getString("poster"))
				.listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
						return false;
					}

					@Override
					public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
						//Image successfully loaded into image view
						scheduleStartPostponedTransition(filmIcon);
						return false;
					}
				})
				.into(filmIcon);

		title.setText(bundle.getString("title"));

        // Get the ViewModel from the factory
        DetailViewModelFactory factory = InjectorUtils.provideDetailViewModelFactory(this.getApplicationContext(), episodeId);
        mViewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);

        // Observers changes in the FilmEntry with the id mId
        mViewModel.getWeather().observe(this, filmEntryObserver -> {
            // If the film's details change, update the UI
            if (filmEntryObserver != null) bindWeatherToUI(filmEntryObserver);
        });

		introTextView = (TextView) findViewById(R.id.intro_text_view);
		openingTextView = (StarWarsTextView) findViewById(R.id.opening_text_view);
		Button startButton = findViewById(R.id.button);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (audioPrepared) {
					startOpeningScene();
				} else {
					startRequested = true;
				}
		}
		});
		initMediaPlayer();
    }

	private void initMediaPlayer() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				audioPrepared = true;
				if (startRequested) {
					startOpeningScene();
					startRequested = false;
				}
			}
		});

		try {
			AssetFileDescriptor afd = getAssets().openFd("opening_crawl_1977.ogg");
			mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
		} catch (IOException e) {
			Log.w("Error Music", "Error loading music:" + e.toString());
		}
		mediaPlayer.prepareAsync();
	}

	@Override
	protected void onPause() {
		mediaPlayer.release();
		super.onPause();
	}
	private void startOpeningScene() {
		try {
			mediaPlayer.seekTo(0);
			mediaPlayer.start();
		} catch (IllegalStateException ie) {
			Log.w("Error Music", "could not start playing audio:" + ie.toString());
		}
		startAnimation();
	}

	private void startAnimation() {
		startIntroTextAnimation();
		openingTextView.startAnimation().addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationCancel(Animator animation) {
				//btnStart.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				//btnStart.setVisibility(View.VISIBLE);
			}
		});
	}

	private void startIntroTextAnimation() {
		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Animator introAnimator = AnimatorInflater.loadAnimator(this, R.animator.intro);
			introAnimator.setTarget(introTextView);
			introAnimator.start();
		} else {
			Keyframe kf0 = Keyframe.ofFloat(0f, 0);
			Keyframe kf1 = Keyframe.ofFloat(.2f, 1);
			Keyframe kf2 = Keyframe.ofFloat(.9f, 1);
			Keyframe kf3 = Keyframe.ofFloat(1f, 0);
			PropertyValuesHolder pvh = PropertyValuesHolder.ofKeyframe("alpha", kf0, kf1, kf2, kf3);
			ObjectAnimator alphaAnimator = ObjectAnimator.ofPropertyValuesHolder(introTextView, pvh);
			alphaAnimator.setInterpolator(new DecelerateInterpolator());
			alphaAnimator.setDuration(6000).setStartDelay(1000);
			alphaAnimator.start();
		}
	}


	private void scheduleStartPostponedTransition(final ImageView sharedElement) {
		sharedElement.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
				startPostponedEnterTransition();
				return true;
			}
		});
	}

    private void bindWeatherToUI(FilmEntry filmEntry) {
		/****************
		 * Release Date *
		 ****************/
		releasedateTextView.setText(filmEntry.getRelease_date());

		/****************
		 *   Director   *
		 ****************/
		directorTextView.setText(filmEntry.getDirector());

		/****************
		 *   Producer   *
		 ****************/
		producerTextView.setText(filmEntry.getProducer());

		/***********************
		 * Character Name urls *
		 ***********************/

		StringBuilder sb = new StringBuilder();
		for (String s : filmEntry.getCharacters())
		{
			sb.append(s);
			sb.append("\t");
		}

		charactersTextView.setText(sb.toString());

		/**********************
		 * Opening Crawl Text *
		 **********************/
		openingTextView.setText(filmEntry.getOpening_crawl());


    }
}