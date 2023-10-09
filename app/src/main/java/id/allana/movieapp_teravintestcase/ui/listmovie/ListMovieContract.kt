package id.allana.movieapp_teravintestcase.ui.listmovie

import androidx.lifecycle.LiveData
import id.allana.movieapp_teravintestcase.base.BaseContract
import id.allana.movieapp_teravintestcase.base.Resource
import id.allana.movieapp_teravintestcase.data.local.MovieEntity
import id.allana.movieapp_teravintestcase.data.network.model.Movie
import id.allana.movieapp_teravintestcase.data.network.model.ResponseDiscoveryMovies

interface ListMovieContract {
    interface View : BaseContract.BaseView {
        fun setRepeatingUpdate()
        fun requestPermission()
        fun setupRecyclerViewNetwork()
        fun setupRecyclerViewLocal()
        fun getDataFromNetwork()
        fun getDataFromLocal()
        fun setListDataFromNetwork(data: List<Movie>)
        fun setListDataFromLocal(data: List<MovieEntity>)
    }

    interface ViewModel: BaseContract.BaseViewModel {
        fun getDiscoveryMoviesLiveData(): LiveData<Resource<List<Movie>>>
        fun getDiscoveryMovies()
        fun getAllDiscoveryMoviesFromLocalLiveData(): LiveData<List<MovieEntity>>
    }

    interface Repository: BaseContract.BaseRepository {
        suspend fun getDiscoveryMovies(): ResponseDiscoveryMovies
        fun getAllDiscoveryMoviesFromLocal(): LiveData<List<MovieEntity>>
    }
}