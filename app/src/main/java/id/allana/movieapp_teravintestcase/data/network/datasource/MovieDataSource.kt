package id.allana.movieapp_teravintestcase.data.network.datasource

import id.allana.movieapp_teravintestcase.data.network.model.ResponseDiscoveryMovies

interface MovieDataSource {

    suspend fun getDiscoverMovie(): ResponseDiscoveryMovies

}