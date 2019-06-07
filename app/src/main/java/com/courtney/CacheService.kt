package com.courtney

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class CacheService : IntentService("CacheService"), AnkoLogger {

    override fun onHandleIntent(intent: Intent?) {
        info { "onHandleIntent" }
        Thread.sleep(5000)
    }

    /*override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        info { "onStartCommand" }
        return START_STICKY
    }*/

    override fun onCreate() {
        super.onCreate()
        info { "onCreate" }
    }

    override fun onDestroy() {
        super.onDestroy()
        info { "onDestroy" }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}