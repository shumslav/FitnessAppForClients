package com.example.fitnessappuser.notifications

import com.example.fitnessappuser.notifications.NotificationDate

data class PushNotification(
    val data: NotificationDate,
    val to: String
)