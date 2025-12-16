package com.example.a24hberlin

import android.app.Application
import com.example.a24hberlin.data.repository.NetworkMonitor

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkMonitor.initialize(applicationContext)
    }
}