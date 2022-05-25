package com.example.fitnessappuser.models

import android.content.Context
import android.content.SharedPreferences

class DeviceId(context: Context) {
    private companion object{
        const val DEVICE = "Device"
        const val ID = "Id"
    }

    private val deviceId = context.getSharedPreferences(DEVICE,Context.MODE_PRIVATE)

    var id:String
    get() {
        return if (deviceId.contains(ID))
            deviceId.getString(ID,"")!!
        else
            ""
    }
    set(value){
        deviceId.edit().putString(ID,value).apply()
    }
}