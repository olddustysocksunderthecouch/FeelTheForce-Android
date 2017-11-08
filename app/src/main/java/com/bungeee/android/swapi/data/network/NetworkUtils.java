package com.bungeee.android.swapi.data.network;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the SWAPI servers.
 */
final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String SWAPI_URL = "https://swapi.co/api/films";


    /**
     * Retrieves the proper URL to query for Star Wars Data.
     *
     * @return URL to query SWAPI service
     */
    static URL getUrl() {
        return buildUrlWithLocationQuery();
    }

    /**
     * Builds the URL used to talk to the SWAPI server.
     *
     * @return The URL to use to query the SWAPI server.
     */

    private static URL buildUrlWithLocationQuery() {
        Uri weatherQueryUri = Uri.parse(SWAPI_URL).buildUpon()
//                .appendQueryParameter(QUERY_PARAM, locationQuery)
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.v(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    static URL getCharacterUrl(){
		Uri weatherQueryUri = Uri.parse("https://swapi.co/api/people/").buildUpon()
				.build();

		try {
			URL characterQueryUrl = new URL(weatherQueryUri.toString());
			Log.v(TAG, "URL: " + characterQueryUrl);
			return characterQueryUrl;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}



    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}