package id.allana.movieapp_teravintestcase.ui.splashscreen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.allana.movieapp_teravintestcase.base.BaseViewModelImpl
import id.allana.movieapp_teravintestcase.data.local.MovieEntity

class SplashScreenViewModel(private val repository: SplashScreenRepository): BaseViewModelImpl(), SplashScreenContract.ViewModel {

    private val isLocalDataAvailable = MutableLiveData<Boolean>()
    private val connectionStatus = MutableLiveData<Boolean>()
    override fun connectionStatusLiveData(): LiveData<Boolean> = connectionStatus
    override fun isLocalDataAvailableLiveData(): LiveData<Boolean> = isLocalDataAvailable
    override fun getDiscoveryMoviesFromLocal(): LiveData<List<MovieEntity>> {
        return repository.getListMovieDiscoveryFromLocal()
    }

    override fun isLocalDataAvailable(data: List<MovieEntity>) {
        isLocalDataAvailable.value = data.isEmpty()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun connectionStatus(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val networkInfo = connectivityManager.activeNetworkInfo

        /**
         * Check for >= API 23
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectionStatus.value = capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
        } else {
            /**
             * Check for > API 23
             */
            connectionStatus.value = networkInfo != null && networkInfo.isConnected
        }
    }
}