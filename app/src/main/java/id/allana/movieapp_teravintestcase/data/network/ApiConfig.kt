package id.allana.movieapp_teravintestcase.data.network

import id.allana.movieapp_teravintestcase.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object {
        fun getApiService(): ApiService {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor { interceptor ->
                    val requestAuthApiKey = interceptor.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${BuildConfig.AUTH_KEY}")
                        .build()
                    interceptor.proceed(requestAuthApiKey)
                }
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

}