package id.allana.movieapp_teravintestcase.data.network

import id.allana.movieapp_teravintestcase.data.network.model.ResponseDiscoveryMovies
import retrofit2.http.GET

interface ApiService {

    @GET("discover/movie")
    fun getDiscoverMovies(): ResponseDiscoveryMovies

}