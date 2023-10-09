package id.allana.movieapp_teravintestcase.ui.listmovie

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.allana.movieapp_teravintestcase.base.BaseActivity
import id.allana.movieapp_teravintestcase.base.GenericViewModelFactory
import id.allana.movieapp_teravintestcase.base.Resource
import id.allana.movieapp_teravintestcase.data.local.MovieDatabase
import id.allana.movieapp_teravintestcase.data.local.MovieEntity
import id.allana.movieapp_teravintestcase.data.local.datasource.LocalMovieDataSourceImpl
import id.allana.movieapp_teravintestcase.data.network.ApiConfig
import id.allana.movieapp_teravintestcase.data.network.datasource.MovieDataSourceImpl
import id.allana.movieapp_teravintestcase.data.network.model.Movie
import id.allana.movieapp_teravintestcase.data.network.service.MovieUpdateDataTask
import id.allana.movieapp_teravintestcase.databinding.ActivityListMovieBinding
import id.allana.movieapp_teravintestcase.ui.listmovie.adapter.ListMovieLocalAdapter
import id.allana.movieapp_teravintestcase.ui.listmovie.adapter.ListMovieNetworkAdapter
import id.allana.movieapp_teravintestcase.util.checkConnection
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class ListMovieActivity : BaseActivity<ActivityListMovieBinding, ListMovieViewModel>(
    ActivityListMovieBinding::inflate
), ListMovieContract.View {

    private lateinit var movieUpdateDataTask: MovieUpdateDataTask


    private val adapter: ListMovieNetworkAdapter by lazy {
        ListMovieNetworkAdapter()
    }

    private val adapterLocal: ListMovieLocalAdapter by lazy {
        ListMovieLocalAdapter()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) Log.d(ListMovieActivity::class.simpleName, "Izin notifikasi diberikan")
        else Log.d(ListMovieActivity::class.simpleName, "Izin notifikasi ditolak")
    }

    override fun initView() {
        movieUpdateDataTask = MovieUpdateDataTask()
        if (checkConnection(this)) {
            setRepeatingUpdate()
            getDataFromNetwork()
            setupRecyclerViewNetwork()
        } else {
            getDataFromLocal()
            setupRecyclerViewLocal()
        }

        requestPermission()

    }

    override fun setRepeatingUpdate() {
        lifecycleScope.launch {
            movieUpdateDataTask.setRepeatingUpdateData(this@ListMovieActivity)
        }
    }

    override fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun getDataFromNetwork() {
        getViewModel().getDiscoveryMovies()
    }

    override fun getDataFromLocal() {
        getViewModel().getAllDiscoveryMoviesFromLocalLiveData()
    }

    override fun initViewModel(): ListMovieViewModel {
        val movieDao = MovieDatabase.getDatabase(this).movieDao()
        val movieDataSource = MovieDataSourceImpl(ApiConfig.getApiService())
        val localMovieDataSource = LocalMovieDataSourceImpl(movieDao)
        val repository = ListMovieRepository(movieDataSource, localMovieDataSource)
        return GenericViewModelFactory(ListMovieViewModel(repository)).create(ListMovieViewModel::class.java)
    }


    override fun setupRecyclerViewNetwork() {
        getViewBinding().rvDiscoveryMovies.apply {
            adapter = this@ListMovieActivity.adapter
            layoutManager = LinearLayoutManager(this@ListMovieActivity)
        }
    }

    override fun setupRecyclerViewLocal() {
        getViewBinding().rvDiscoveryMovies.apply {
            adapter = this@ListMovieActivity.adapterLocal
            layoutManager = LinearLayoutManager(this@ListMovieActivity)
        }
    }

    override fun observeData() {
        getViewModel().getAllDiscoveryMoviesFromLocalLiveData().observe(this) {
            setListDataFromLocal(it)
        }

        getViewModel().getDiscoveryMoviesLiveData().observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                    showContent(false)
                    showError(false)
                }

                is Resource.Success -> {
                    showLoading(false)
                    showContent(true)
                    showError(false)
                    it.data?.let { listMovie ->
                        setListDataFromNetwork(listMovie)
                    }
                }

                is Resource.Error -> {
                    showLoading(false)
                    showContent(false)
                    showError(true, it.message)
                }
            }
        }
    }

    override fun setListDataFromNetwork(data: List<Movie>) {
        adapter.setItems(data)
    }

    override fun setListDataFromLocal(data: List<MovieEntity>) {
        adapterLocal.setItemsFromLocal(data)
    }

    override fun showContent(isVisible: Boolean) {
        getViewBinding().rvDiscoveryMovies.isVisible = isVisible
    }

    override fun showLoading(isLoading: Boolean) {
        getViewBinding().pbLoadingMovie.isVisible = isLoading
    }

    override fun showError(isError: Boolean, messageError: String?) {
        if (isError) Toast.makeText(this, messageError, Toast.LENGTH_SHORT).show()
    }
}