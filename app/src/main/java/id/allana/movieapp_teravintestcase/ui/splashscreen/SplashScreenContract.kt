package id.allana.movieapp_teravintestcase.ui.splashscreen

import android.content.Context
import androidx.lifecycle.LiveData
import id.allana.movieapp_teravintestcase.base.BaseContract
import id.allana.movieapp_teravintestcase.data.local.MovieEntity
import id.allana.movieapp_teravintestcase.data.network.model.Movie

class SplashScreenContract {

    interface View : BaseContract.BaseView {
        fun navigateToListMovie()
        fun checkConnectionStatus()
        fun showAlertDialog()
    }

    interface ViewModel: BaseContract.BaseViewModel {
        fun connectionStatus(context: Context)
        fun connectionStatusLiveData(): LiveData<Boolean>
        fun isLocalDataAvailable(data: List<MovieEntity>)
        fun isLocalDataAvailableLiveData(): LiveData<Boolean>
        fun getDiscoveryMoviesFromLocal(): LiveData<List<MovieEntity>>
    }

    interface Repository: BaseContract.BaseRepository {
        fun getListMovieDiscoveryFromLocal(): LiveData<List<MovieEntity>>
    }
}