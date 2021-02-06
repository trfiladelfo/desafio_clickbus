package br.com.thiagofiladelfo.clickbus.data.repository.local.dao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.thiagofiladelfo.clickbus.data.repository.local.dao.MovieDAO
import br.com.thiagofiladelfo.clickbus.data.repository.local.dao.entity.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDAO(): MovieDAO

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        /**
         * Recupera a instancia da classe de manipulação dos filmes
         */
        fun getDatabase(context: Context): MovieDatabase =
            (INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie"
                ).build().also {  INSTANCE = it }
            })
    }
}