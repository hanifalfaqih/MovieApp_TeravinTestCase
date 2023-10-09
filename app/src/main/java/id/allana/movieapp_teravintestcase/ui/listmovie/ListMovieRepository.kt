package id.allana.movieapp_teravintestcase.ui.listmovie

import androidx.lifecycle.LiveData
import id.allana.movieapp_teravintestcase.data.local.MovieEntity
import id.allana.movieapp_teravintestcase.data.local.datasource.LocalMovieDataSource
import id.allana.movieapp_teravintestcase.data.network.datasource.MovieDataSource
import id.allana.movieapp_teravintestcase.data.network.model.ResponseDiscoveryMovies

class ListMovieRepository(
    private val movieDataSource: MovieDataSource,
    private val localMovieDataSource: LocalMovieDataSource): ListMovieContract.Repository {

    override suspend fun getDiscoveryMovies(): ResponseDiscoveryMovies {
        return movieDataSource.getDiscoverMovie()
    }

    override fun getAllDiscoveryMoviesFromLocal(): LiveData<List<MovieEntity>> {
        return localMovieDataSource.getAllDiscoveryMoviesFromLocal()
    }
}