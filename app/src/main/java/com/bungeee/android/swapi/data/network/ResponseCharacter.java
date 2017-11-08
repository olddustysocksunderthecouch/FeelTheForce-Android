package com.bungeee.android.swapi.data.network;

import android.support.annotation.NonNull;

import com.bungeee.android.swapi.data.database.CharacterEntry;
import com.bungeee.android.swapi.data.database.FilmEntry;

/**
 * Character response from the backend. Contains the characters.
 */
class ResponseCharacter {

    @NonNull
    private final CharacterEntry[] mCharacterEntries;

    public ResponseCharacter(@NonNull final CharacterEntry[] characterEntries) {
		mCharacterEntries = characterEntries;
    }

    public CharacterEntry[] getmCharacterEntries() {
        return mCharacterEntries;
    }
}