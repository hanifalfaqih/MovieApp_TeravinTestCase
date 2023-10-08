package id.allana.movieapp_teravintestcase.data.network.datasource

import id.allana.movieapp_teravintestcase.data.network.ApiService
import id.allana.movieapp_teravintestcase.data.network.model.ResponseDiscoveryMovies

class MovieDataSourceImpl(private val apiService: ApiService): MovieDataSource {
    override suspend fun getDiscoverMovie(): ResponseDiscoveryMovies {
        return apiService.getDiscoverMovies()
    }
}