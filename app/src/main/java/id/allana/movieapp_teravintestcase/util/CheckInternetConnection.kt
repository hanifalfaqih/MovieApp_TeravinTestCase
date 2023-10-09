package id.allana.movieapp_teravintestcase.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
fun checkConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    val networkInfo = connectivityManager.activeNetworkInfo

    /**
     * Check for >= API 23
     */
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
    } else {
        /**
         * Check for <= API 22
         */
        networkInfo != null && networkInfo.isConnected
    }
}