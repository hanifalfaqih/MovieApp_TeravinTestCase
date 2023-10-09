package id.allana.movieapp_teravintestcase.data.local.datasource

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import id.allana.movieapp_teravintestcase.data.local.MovieEntity
import id.allana.movieapp_teravintestcase.data.network.model.Movie

interface LocalMovieDataSource {

    fun getAllDiscoveryMoviesFromLocal(): LiveData<List<MovieEntity>>
    suspend fun deleteAllDiscoveryMoviesFromLocal(listMovie: List<MovieEntity>)
    suspend fun insertAllDiscoveryMovies(listMovie: List<MovieEntity>)
}