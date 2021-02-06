package br.com.thiagofiladelfo.clickbus.data.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.thiagofiladelfo.clickbus.data.model.Movie

/**
 * Interface para acessar os dados de filmes no armazenamento interno do dispositivo
 */
@Dao
interface MovieDAO {

    /**
     * Carrega todos os filmes favoritados
     */
    @Query("SELECT * FROM movie ORDER BY title ASC")
    fun getMovies(): List<Movie>

    /**
     * Insere um filme no banco de dados (SQLite) do dispositivo para recuperar
     * posteriormente como um favorito
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: Movie)

    /**
     * Remove um filme com afinalidade de tirar do favorito
     */
    @Query("DELETE FROM movie WHERE id = :id")
    suspend fun delete(id: Int)

    /**
    * Remove todos os filmes
    */
    @Query("DELETE FROM movie")
    suspend fun deleteAll()
}