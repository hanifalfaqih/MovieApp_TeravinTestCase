package id.allana.movieapp_teravintestcase.ui.listmovie

import androidx.lifecycle.LiveData
import id.allana.movieapp_teravintestcase.base.BaseContract
import id.allana.movieapp_teravintestcase.base.Resource
import id.allana.movieapp_teravintestcase.data.network.model.Movie
import id.allana.movieapp_teravintestcase.data.network.model.ResponseDiscoveryMovies

interface ListMovieContract {
    interface View : BaseContract.BaseView {
        fun setRepeatingUpdate()
        fun requestPermission()
        fun getData()
        fun setupRecyclerView()
        fun setListData(data: List<Movie>)
    }

    interface ViewModel: BaseContract.BaseViewModel {
        fun getDiscoveryMoviesLiveData(): LiveData<Resource<List<Movie>>>
        fun getDiscoveryMovies()
    }

    interface Repository: BaseContract.BaseRepository {
        suspend fun getDiscoveryMovies(): ResponseDiscoveryMovies
    }
}