package com.example.fitnessappuser.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.fitnessappuser.R
import com.example.fitnessappuser.models.DeviceId
import com.example.fitnessappuser.ui.login.EnterActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

private val notificationID = "firebase_channel"

class NotificationService():FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val deviceId = DeviceId(applicationContext)
        deviceId.id = token
        Log.i("NEW DEVICE ID", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, EnterActivity::class.java)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent, FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, notificationID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_profile)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "channelName"
        val channel = NotificationChannel(notificationID, channelName, IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }
}