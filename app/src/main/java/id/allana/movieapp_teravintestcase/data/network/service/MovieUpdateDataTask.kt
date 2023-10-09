package id.allana.movieapp_teravintestcase.data.network.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import id.allana.movieapp_teravintestcase.R
import id.allana.movieapp_teravintestcase.data.local.MovieDatabase
import id.allana.movieapp_teravintestcase.data.local.MovieEntity
import id.allana.movieapp_teravintestcase.data.local.datasource.LocalMovieDataSourceImpl
import id.allana.movieapp_teravintestcase.data.network.ApiConfig
import id.allana.movieapp_teravintestcase.data.network.datasource.MovieDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieUpdateDataTask : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (intent.action == ACTION_NOTIFY_AFTER_FINISH) {
            showNotification(context, "Data Diperbarui", "Data pada penyimpanan lokal berhasil diperbarui")
        } else if (intent.action == ACTION_NOTIFY_BEFORE_START) {
            showNotificationTemporary(context, "Data Baru Tersedia", "Data terbaru sudah tersedia")
        }
    }

    suspend fun setRepeatingUpdateData(context: Context) {
        val localMovieDataSource = LocalMovieDataSourceImpl(MovieDatabase.getDatabase(context).movieDao())
        val movieDataSource = MovieDataSourceImpl(ApiConfig.getApiService())

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val movieResponse = withContext(Dispatchers.IO) {
           movieDataSource.getDiscoverMovie()
        }

        val listMovieResponse = movieResponse.results
        val listMovieEntity = listMovieResponse.map {
            MovieEntity(it.id, it.overview, it.title, it.posterPath, it.releaseDate)
        }
        /**
         * Delete old data first
         */
        val oldListMovieData = localMovieDataSource.getAllDiscoveryMoviesFromLocal()
        oldListMovieData.value?.let { localMovieDataSource.deleteAllDiscoveryMoviesFromLocal(it) }

        /**
         * Replace with new data
         */
        localMovieDataSource.insertAllDiscoveryMovies(listMovieEntity).also {
            Log.d(MovieUpdateDataTask::class.java.simpleName, localMovieDataSource.getAllDiscoveryMoviesFromLocal().toString())
        }

        val intentBeforeStartTask = Intent(context, MovieUpdateDataTask::class.java)
            .setAction(ACTION_NOTIFY_BEFORE_START)
        val beforeStartTaskPendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_NOTIFY,
            intentBeforeStartTask,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime(),
            59000L,
            beforeStartTaskPendingIntent
        )

        val intentAfterFinishTask = Intent(context, MovieUpdateDataTask::class.java).setAction(
            ACTION_NOTIFY_AFTER_FINISH)
        val afterFinishTaskPendingIntent =
            PendingIntent.getBroadcast(context, REQUEST_REPEATING, intentAfterFinishTask, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime(),
            60000L,
            afterFinishTaskPendingIntent
        )
    }

    private fun showNotification(context: Context, title: String, description: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    private fun showNotificationTemporary(context: Context, title: String, description: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setTimeoutAfter(5000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        private const val REQUEST_REPEATING = 101
        private const val REQUEST_NOTIFY = 102

        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "100"
        private const val CHANNEL_NAME = "MOVIE"

        private const val ACTION_NOTIFY_BEFORE_START = "action_notify_before_start"
        private const val ACTION_NOTIFY_AFTER_FINISH = "action_notify_after_finish"
    }
}