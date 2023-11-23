package com.example.newsarticlesdemo.app

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp


private const val TAG: String = "NewsApplication"

class NewsApplication : Application() {

    companion object {
        private var appContext: Context? = null

        fun getAppContext(): Context? {
            return appContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        FirebaseApp.initializeApp(this)
    }


}