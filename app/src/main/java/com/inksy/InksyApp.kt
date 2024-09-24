package com.inksy

import android.app.Application
import com.google.firebase.FirebaseApp

class InksyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }

}