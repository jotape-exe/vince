package com.company.ourfinances.model.remote.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.company.ourfinances.R


class ForegroundApiService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    enum class Actions{
        START, STOP
    }

    private fun start(){
        val notification = NotificationCompat.Builder(this, "running_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Notificação Background")
            .setContentText("Conteúdo da notificação")
            .build()

        startForeground(1, notification)
    }


}