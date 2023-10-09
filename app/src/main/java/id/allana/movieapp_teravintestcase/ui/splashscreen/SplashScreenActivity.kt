package id.allana.movieapp_teravintestcase.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.allana.movieapp_teravintestcase.base.BaseActivity
import id.allana.movieapp_teravintestcase.base.GenericViewModelFactory
import id.allana.movieapp_teravintestcase.data.local.MovieDatabase
import id.allana.movieapp_teravintestcase.data.local.datasource.LocalMovieDataSourceImpl
import id.allana.movieapp_teravintestcase.databinding.ActivitySplashScreenBinding
import id.allana.movieapp_teravintestcase.ui.listmovie.ListMovieActivity

@SuppressLint("CustomSplashScreen")
@RequiresApi(Build.VERSION_CODES.M)
class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding, SplashScreenViewModel>(
    ActivitySplashScreenBinding::inflate
), SplashScreenContract.View {

    override fun initView() {
        checkConnectionStatus()
    }

    override fun initViewModel(): SplashScreenViewModel {
        val movieDao = MovieDatabase.getDatabase(this).movieDao()
        val localMovieDataSource = LocalMovieDataSourceImpl(movieDao)
        val repository = SplashScreenRepository(localMovieDataSource)
        return GenericViewModelFactory(SplashScreenViewModel(repository)).create(SplashScreenViewModel::class.java)
    }

    override fun navigateToListMovie() {
        val intentToListMovie = Intent(this, ListMovieActivity::class.java)
        intentToListMovie.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentToListMovie)
    }
    override fun checkConnectionStatus() {
        getViewModel().connectionStatus(this)
    }

    override fun observeData() {
        getViewModel().getDiscoveryMoviesFromLocal().observe(this) { data ->
            getViewModel().isLocalDataAvailable(data)
        }

        getViewModel().isLocalDataAvailableLiveData().observe(this) { isEmpty ->
            getViewModel().connectionStatusLiveData().observe(this) { isConnected ->
                Log.d(SplashScreenActivity::class.simpleName, "isEmpty $isEmpty and isConnected $isConnected")
                /**
                 * To check if empty data is true and connection is false
                 */
                if (isEmpty && !isConnected) {
                    showAlertDialog()
                } else {
                    navigateToListMovie()
                }
            }
        }
    }

    override fun showAlertDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Tidak Terhubung Internet")
            .setMessage("Pastikan Anda terhubung ke internet karena cache data Anda belum tersedia. Silakan hubungkan perangkat Anda dengan internet dan coba lagi.")
            .setPositiveButton("Coba lagi") { _, _ ->
                initView()
            }
            .setNegativeButton("Keluar") { _, _ ->
                finish()
            }.show()
    }
}