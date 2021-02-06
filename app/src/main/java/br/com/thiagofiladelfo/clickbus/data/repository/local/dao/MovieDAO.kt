package br.com.thiagofiladelfo.clickbus.data.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.thiagofiladelfo.clickbus.data.repository.local.dao.entity.Movie


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

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Int): Movie?

    /**
     * Insere ou atualiza um filme no banco de dados (SQLite) do dispositivo para recuperar
     * posteriormente como um favorito
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) //para inserir ou atualizar quando existir
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