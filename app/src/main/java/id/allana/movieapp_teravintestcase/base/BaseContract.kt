package id.allana.movieapp_teravintestcase.base

interface BaseContract {

    interface BaseView {
        fun observeData()
        fun showContent(isVisible: Boolean)
        fun showLoading(isLoading: Boolean)
        fun showError(isError: Boolean, messageError: String? = null)
    }

    interface BaseViewModel

    interface BaseRepository

}