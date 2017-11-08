package com.bungeee.android.swapi.data.network;

import android.support.annotation.Nullable;
import android.util.Log;

import com.bungeee.android.swapi.data.database.FilmEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Parser for Star Wars Films JSON data.
 */
final class JsonParserFilm {

    private static final String OWM_LIST = "results";

    private static final String SWO_TITLE = "title";
    private static final String SWO_EPISODEID = "episode_id";
	private static final String SWO_OPENINGCRAWL = "opening_crawl";
    private static final String SWO_DIRECTOR = "director";
    private static final String SWO_PRODUCER = "producer";
	private static final String SWO_RELEASEDATE = "release_date";
	private static final String SWO_CHARACTERS = "characters";

    private static final String OWM_MESSAGE_CODE = "cod";

    private static boolean hasHttpError(JSONObject forecastJson) throws JSONException {
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    return false;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Location invalid
                default:
                    // Server probably down
                    return true;
            }
        }
        return false;
    }

    private static FilmEntry[] fromJsonList(final JSONObject allFilmsJson) throws JSONException {
        JSONArray jsonFilmsArray = allFilmsJson.getJSONArray(OWM_LIST);

        FilmEntry[] filmEntries = new FilmEntry[jsonFilmsArray.length()];

        //long normalizedUtcStartDay = StarWarsDateUtils.getNormalizedUtcMsForToday();

        for (int i = 0; i < jsonFilmsArray.length(); i++) {
            // Get the JSON object representing the day
            JSONObject dayForecast = jsonFilmsArray.getJSONObject(i);

            // In preparation for date ordering
            //long dateTimeMillis = normalizedUtcStartDay + StarWarsDateUtils.DAY_IN_MILLIS * i;
            FilmEntry weather = fromJsonSingleEntry(dayForecast);

            filmEntries[i] = weather;
        }
        return filmEntries;
    }

    private static FilmEntry fromJsonSingleEntry(final JSONObject singleFilm
                                         ) throws JSONException {

		String title = singleFilm.getString(SWO_TITLE);
		Log.d("JsonParser Title: ", title);
		String episode_id = singleFilm.getString(SWO_EPISODEID);
		String opening_crawl = singleFilm.getString(SWO_OPENINGCRAWL);
		String director = singleFilm.getString(SWO_DIRECTOR);
		String producer = singleFilm.getString(SWO_PRODUCER);
		String releasedate = singleFilm.getString(SWO_RELEASEDATE);

		JSONArray characters = singleFilm.getJSONArray(SWO_CHARACTERS);

		ArrayList<String> characterDataList = new ArrayList<String>();

		if (characters != null) {
			for (int i=0;i<characters.length();i++){
				characterDataList.add(characters.getString(i));
			}
		}

		// Create the films entry object
        return new FilmEntry(title, episode_id, opening_crawl, director, producer, releasedate, characterDataList);
    }

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the films from the Star Wars Series.
     *
     * @param filmsJsonStr JSON response from server
     * @return Array of Strings describing film data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    @Nullable
	ResponseFilm parse(final String filmsJsonStr) throws JSONException {
        JSONObject filmsJson = new JSONObject(filmsJsonStr);

        // Is there an error?
        if (hasHttpError(filmsJson)) {
            return null;
        }

        FilmEntry[] filmEntries = fromJsonList(filmsJson);

        return new ResponseFilm(filmEntries);
    }
}