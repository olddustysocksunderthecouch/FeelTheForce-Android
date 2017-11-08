package com.bungeee.android.swapi.utilities;

import android.util.Log;

import com.bungeee.android.swapi.R;

public final class StarWarsFilmsUtils {

    private static final String LOG_TAG = StarWarsFilmsUtils.class.getSimpleName();

    /**
     * Helper method to provide a url (as a string) to retrieve an image for
     * a particular episode
     * @param episode_id
     * @return String url for the corresponding film poster.
     */
    public static String getLargeArtResourceIdForWeatherCondition(String episode_id) {
        if (episode_id.equals("1")) {
            return "https://i.ytimg.com/vi/CVRa0xqxWDQ/maxresdefault.jpg";
        } else if (episode_id.equals("2")) {
            return "http://s0.limitedrun.com/images/1170962/v600_Episode2_FRONT.jpg";
        } else if (episode_id.equals("3")) {
            return "http://i0.kym-cdn.com/entries/icons/original/000/022/048/Revenge_of_the_Sith.jpg";
        } else if (episode_id.equals("4")) {
            return "http://www.wcmlibrary.org/wp-content/uploads/2015/10/Episode_IV-1024.jpg";
        } else if (episode_id.equals("5")) {
            return "https://cdn-2.cinemaparadiso.co.uk/1304280120318_l.jpg";
        } else if (episode_id.equals("6")) {
            return "https://upload.wikimedia.org/wikipedia/en/b/b2/ReturnOfTheJediPoster1983.jpg";
        } else if (episode_id.equals("7")) {
            return "https://ae01.alicdn.com/kf/HTB1ppHPHVXXXXcXXXXXq6xXFXXXB/Free-shipping-Star-Wars-Movie-Poster-HD-HOME-WALL-Decor-Custom-ART-PRINT-Silk-Wallpaper-unframed.jpg_640x640.jpg";
        }

        Log.e(LOG_TAG, "Unknown Episode: " + episode_id);
        return "https://ae01.alicdn.com/kf/HTB1ppHPHVXXXXcXXXXXq6xXFXXXB/Free-shipping-Star-Wars-Movie-Poster-HD-HOME-WALL-Decor-Custom-ART-PRINT-Silk-Wallpaper-unframed.jpg_640x640.jpg";
    }

	public static String getRomanNumeral(String episode_id) {
		if (episode_id.equals("1")) {
			return "I";
		} else if (episode_id.equals("2")) {
			return "II";
		} else if (episode_id.equals("3")) {
			return "III";
		} else if (episode_id.equals("4")) {
			return "IV";
		} else if (episode_id.equals("5")) {
			return "V";
		} else if (episode_id.equals("6")) {
			return "VI";
		} else if (episode_id.equals("7")) {
			return "VII";
		}

		Log.e(LOG_TAG, "Unknown Episode: " + episode_id);
		return "Error";
	}
}