package id.allana.movieapp_teravintestcase.ui.splashscreen

import androidx.lifecycle.LiveData
import id.allana.movieapp_teravintestcase.data.local.MovieEntity
import id.allana.movieapp_teravintestcase.data.local.datasource.LocalMovieDataSource

class SplashScreenRepository(private val localMovieDataSource: LocalMovieDataSource): SplashScreenContract.Repository {
    override fun getListMovieDiscoveryFromLocal(): LiveData<List<MovieEntity>> {
        return localMovieDataSource.getAllDiscoveryMoviesFromLocal()
    }
}