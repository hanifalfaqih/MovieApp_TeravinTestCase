package id.allana.movieapp_teravintestcase.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_table")
    fun getAllDiscoveryMoviesFromLocal(): LiveData<List<MovieEntity>>

    @Delete
    suspend fun deleteAllMoviesFromLocal(listMovie: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDiscoveryMovies(listMovie: List<MovieEntity>)

}