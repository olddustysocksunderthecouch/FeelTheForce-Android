package com.bungeee.android.swapi.data.network;

import android.support.annotation.NonNull;

import com.bungeee.android.swapi.data.database.FilmEntry;

/**
 * Film response from the backend. Contains the all the films.
 */
class ResponseFilm {

    @NonNull
    private final FilmEntry[] mFilmEntries;

    public ResponseFilm(@NonNull final FilmEntry[] filmEntries) {
        mFilmEntries = filmEntries;
    }

    public FilmEntry[] getFilmEntries() {
        return mFilmEntries;
    }
}