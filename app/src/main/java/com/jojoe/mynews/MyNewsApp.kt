package com.jojoe.mynews

import android.app.Application
import android.content.Context
import com.jojoe.mynews.di.AppContainer

class MyNewsApp:Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(applicationContext)
    }
}