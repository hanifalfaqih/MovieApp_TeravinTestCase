package id.allana.movieapp_teravintestcase.data.local.datasource

import androidx.lifecycle.LiveData
import id.allana.movieapp_teravintestcase.data.local.MovieDao
import id.allana.movieapp_teravintestcase.data.local.MovieEntity

class LocalMovieDataSourceImpl(private val movieDao: MovieDao): LocalMovieDataSource {
    override fun getAllDiscoveryMoviesFromLocal(): LiveData<List<MovieEntity>> {
        return movieDao.getAllDiscoveryMoviesFromLocal()
    }

    override suspend fun deleteAllDiscoveryMoviesFromLocal(listMovie: List<MovieEntity>) {
        return movieDao.deleteAllMoviesFromLocal(listMovie)
    }

    override suspend fun insertAllDiscoveryMovies(listMovie: List<MovieEntity>) {
        return movieDao.insertAllDiscoveryMovies(listMovie)
    }
}