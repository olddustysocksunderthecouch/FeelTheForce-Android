package com.bungeee.android.swapi.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * Created by Adria on 02-Nov-17.
 */
import java.util.Date;
import java.util.List;

/**
 * {@link Dao} which provides an api for all data operations
 */
@Dao
public interface StarWarsDao {
	/**
	 * Selects all {@link FilmEntry} entries after a give date, inclusive. The LiveData will
	 * be kept in sync with the database, so that it will automatically notify observers when the
	 * values in the table change
	 *
	 * @param episode_id A {@link String} from which to select all films
	 * @return {@link LiveData} list of all {@link FilmEntry} objects after date
	 */
	@Query("SELECT * FROM film WHERE episode_id > 0")
	LiveData<List<FilmEntry>> getCurrentFlims();

	/**
	 * Gets the weather for a single day
	 *
	 * @param episode_id The episode you want info for
	 * @return {@link LiveData} with single film
	 */
	@Query("SELECT * FROM film WHERE episode_id = :episode_id")
	LiveData<FilmEntry> getFilmByEpisode(String episode_id);

	/**
	 * Inserts a list of {@link FilmEntry} into the film table. If there is a conflicting id
	 * or episode_id the film entry uses the {@link OnConflictStrategy} of replacing the film
	 * The required uniqueness of these values is defined in the {@link FilmEntry}.
	 *
	 * @param film A list of films  to insert
	 */
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void bulkInsert(FilmEntry... film);

	/**
	 * An attempt to join the tables to return the character name
	 * for the url in FilmEntry
	 * @param search
	 */

//	@Query("SELECT * FROM character WHERE url LIKE :search ")
//	List<CharacterEntry> findCharacterName(String search);
//
//	@Query("SELECT * FROM character "
//			+ "INNER JOIN loan ON loan.book_id = book.id "
//			+ "INNER JOIN user ON user.id = loan.user_id "
//			+ "WHERE user.name LIKE :userName")
//
//	List<CharacterEntry> findCharacterName(String search);

//
//	@Query("SELECT * FROM character "
//			+ "INNER JOIN loan ON loan.book_id = book.id "
//			+ "INNER JOIN user ON user.id = loan.user_id "
//			+ "WHERE user.name LIKE :userName")
//
//	LiveData<CharacterEntry> getCharaterByUrl(String url);

//	@Query("DELETE FROM weather WHERE date < :date")
//	void deleteOldWeather(Date date);




}
