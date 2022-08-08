package com.jojoe.mynews.di

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jojoe.mynews.api.NewsAPI
import com.jojoe.mynews.db.ArticleDao_Impl
import com.jojoe.mynews.db.ArticleDatabase
import com.jojoe.mynews.repository.NewsRepository
import com.jojoe.mynews.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(val context: Context) {
    private fun provideOkHttpClient() =
        OkHttpClient.Builder().addInterceptor(
            ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .build()
        ).build()

    private fun provideRetrofit() = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(provideOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun provideNewApi() = provideRetrofit().create(NewsAPI::class.java)

    private fun provideDb() = ArticleDatabase(context)

    fun provideRepository() = NewsRepository(provideDb(),provideNewApi())

}