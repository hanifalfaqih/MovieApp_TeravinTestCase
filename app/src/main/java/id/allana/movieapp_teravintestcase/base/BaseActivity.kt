package id.allana.movieapp_teravintestcase.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B: ViewBinding, VM: ViewModel>(
    val bindingFactory: (LayoutInflater) -> B
): AppCompatActivity(), BaseContract.BaseView {

    private lateinit var binding: B
    private lateinit var viewModel: VM

    fun getViewBinding(): B = binding
    fun getViewModel(): VM = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        viewModel = initViewModel()
        initView()
        observeData()
    }

    abstract fun initView()
    abstract fun initViewModel(): VM

    override fun observeData() {}

    override fun showContent(isVisible: Boolean) {}

    override fun showError(isError: Boolean, messageError: String?) {}
}