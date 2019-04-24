package com.example.myapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo



class ApiUtils {
     fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }


//
//     fun getCacheEnabledRetrofit(context: Context): Retrofit? {
//        val cacheSize = (5 * 1024 * 1024).toLong()
//        val myCache = Cache(context.cacheDir, cacheSize)
//        val okHttpClient = OkHttpClient.Builder()
//            .cache(myCache)
//            .addInterceptor { chain ->
//                var request = chain.request()
//                request = if (hasNetwork(context)!!)
//                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
//                else
//                    request.newBuilder().header(
//                        "Cache-Control",
//                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
//                    ).build()
//                chain.proceed(request)
//            }
//            .build()
//        val retrofit = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
//            .baseUrl(Api.URL)
//            .build()
//
//                return retrofit
    }

