package com.bungeee.android.swapi.data.network;

import android.support.annotation.Nullable;
import android.util.Log;

import com.bungeee.android.swapi.data.database.CharacterEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Parser for OpenWeatherMap JSON data.
 */
final class JsonParserCharacter {
    private static final String SWO_RESULTS = "results";
    private static final String SWO_NAME = "name";
    private static final String SWO_URL = "url";


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

    private static CharacterEntry[] fromJsonList(final JSONObject allCharactersJson) throws JSONException {
        JSONArray jsonCharactersArray = allCharactersJson.getJSONArray(SWO_RESULTS);

        CharacterEntry[] characterEntries = new CharacterEntry[jsonCharactersArray.length()];

        for (int i = 0; i < jsonCharactersArray.length(); i++) {
            // Get the JSON object representing a character
            JSONObject individualCharacter = jsonCharactersArray.getJSONObject(i);

            // Create the weather entry object
            CharacterEntry weather = fromJsonSingleEntry(individualCharacter);

			characterEntries[i] = weather;
        }
        return characterEntries;
    }

    private static CharacterEntry fromJsonSingleEntry(final JSONObject individualFilm
                                         ) throws JSONException {
        // We ignore all the datetime values embedded in the JSON and assume that
		String name = individualFilm.getString(SWO_NAME);
		Log.d("Charater Name: ", name);
		String url = individualFilm.getString(SWO_URL);

        return new CharacterEntry(name, url);

    }

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     *
     * @param filmsJsonStr JSON response from server
     * @return Array of Strings describing film data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    @Nullable
	ResponseCharacter parse(final String filmsJsonStr) throws JSONException {
        JSONObject forecastJson = new JSONObject(filmsJsonStr);

        // Is there an error?
        if (hasHttpError(forecastJson)) {
            return null;
        }

        CharacterEntry[] characterEntries = fromJsonList(forecastJson);

        return new ResponseCharacter(characterEntries);
    }
}