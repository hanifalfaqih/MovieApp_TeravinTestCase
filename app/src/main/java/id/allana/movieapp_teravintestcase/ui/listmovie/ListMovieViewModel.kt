package id.allana.movieapp_teravintestcase.ui.listmovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.allana.movieapp_teravintestcase.base.BaseViewModelImpl
import id.allana.movieapp_teravintestcase.base.Resource
import id.allana.movieapp_teravintestcase.data.local.MovieEntity
import id.allana.movieapp_teravintestcase.data.network.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListMovieViewModel(private val repository: ListMovieRepository) : BaseViewModelImpl(),
    ListMovieContract.ViewModel {

    private val listMovieResponseLiveData = MutableLiveData<Resource<List<Movie>>>()
    override fun getDiscoveryMoviesLiveData(): LiveData<Resource<List<Movie>>> =
        listMovieResponseLiveData

    override fun getAllDiscoveryMoviesFromLocalLiveData(): LiveData<List<MovieEntity>> {
        return repository.getAllDiscoveryMoviesFromLocal()
    }

    override fun getDiscoveryMovies() {
        listMovieResponseLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getDiscoveryMovies()
                viewModelScope.launch(Dispatchers.Main) {
                    listMovieResponseLiveData.value = Resource.Success(response.results)
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    listMovieResponseLiveData.value = Resource.Error(e.message.orEmpty())
                }
            }
        }
    }
}