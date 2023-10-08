package id.allana.movieapp_teravintestcase.ui.listmovie

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import id.allana.movieapp_teravintestcase.base.BaseActivity
import id.allana.movieapp_teravintestcase.base.GenericViewModelFactory
import id.allana.movieapp_teravintestcase.base.Resource
import id.allana.movieapp_teravintestcase.data.network.ApiConfig
import id.allana.movieapp_teravintestcase.data.network.datasource.MovieDataSourceImpl
import id.allana.movieapp_teravintestcase.data.network.model.Movie
import id.allana.movieapp_teravintestcase.data.network.service.MovieWorker
import id.allana.movieapp_teravintestcase.databinding.ActivityListMovieBinding
import id.allana.movieapp_teravintestcase.ui.listmovie.adapter.ListMovieAdapter
import java.util.concurrent.TimeUnit

class ListMovieActivity : BaseActivity<ActivityListMovieBinding, ListMovieViewModel>(
    ActivityListMovieBinding::inflate
), ListMovieContract.View {

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    private val adapter: ListMovieAdapter by lazy {
        ListMovieAdapter()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) Toast.makeText(this, "Izin notifikasi diberikan", Toast.LENGTH_SHORT).show()
        else Toast.makeText(this, "Izin notifikasi ditolak", Toast.LENGTH_SHORT).show()
    }

    override fun initView() {
        initMovieWorker()
        requestPermission()
        getData()
        setupRecyclerView()
    }

    override fun initMovieWorker() {
        workManager = WorkManager.getInstance(this)
        setPeriodicTask()
    }

    override fun setPeriodicTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest = PeriodicWorkRequest.Builder(MovieWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(periodicWorkRequest)
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this@ListMovieActivity) { workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    Snackbar.make(findViewById(android.R.id.content), "Data berhasil diperbarui", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    override fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun getData() {
        getViewModel().getDiscoveryMovies()
    }

    override fun initViewModel(): ListMovieViewModel {
        val movieDataSource = MovieDataSourceImpl(ApiConfig.getApiService())
        val repository = ListMovieRepository(movieDataSource)
        ListMovieViewModel(repository)
        return GenericViewModelFactory(ListMovieViewModel(repository)).create(ListMovieViewModel::class.java)
    }


    override fun setupRecyclerView() {
        getViewBinding().rvDiscoveryMovies.apply {
            adapter = this@ListMovieActivity.adapter
            layoutManager = LinearLayoutManager(this@ListMovieActivity)
        }
    }

    override fun observeData() {
        super.observeData()
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
                        setListData(listMovie)
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

    override fun setListData(data: List<Movie>) {
        adapter.setItems(data)
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