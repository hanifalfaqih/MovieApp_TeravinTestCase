package id.allana.movieapp_teravintestcase.ui.listmovie

import id.allana.movieapp_teravintestcase.data.network.datasource.MovieDataSource
import id.allana.movieapp_teravintestcase.data.network.model.ResponseDiscoveryMovies

class ListMovieRepository(private val movieDataSource: MovieDataSource): ListMovieContract.Repository {

    override suspend fun getDiscoveryMovies(): ResponseDiscoveryMovies {
        return movieDataSource.getDiscoverMovie()
    }

}