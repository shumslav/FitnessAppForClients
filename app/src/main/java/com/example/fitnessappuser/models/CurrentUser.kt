package com.example.fitnessappuser.models

import android.content.Context
import android.content.Context.MODE_PRIVATE

class CurrentUser(context: Context) {
    companion object {
        private const val USER = "user"
        private const val LOGIN = "login"
        private const val NAME = "name"
        private const val TRAINER = "trainerLogin"
    }

    private val user = context.getSharedPreferences(USER, MODE_PRIVATE)

    var login: String
        get() {
            return if (user.contains(LOGIN))
                user.getString(LOGIN, "")!!
            else ""
        }
        set(value) {user.edit().putString(LOGIN,value).apply()}

    var name: String
        get() {
            return if (user.contains(TRAINER))
                user.getString(TRAINER, "")!!
            else ""
        }
        set(value) {user.edit().putString(TRAINER,value).apply()}

    var trainerLogin: String
        get() {
            return if (user.contains(NAME))
                user.getString(NAME, "")!!
            else ""
        }
        set(value) {user.edit().putString(NAME,value).apply()}


    fun isEnterProfile():Boolean{
        return user.contains(LOGIN)
    }

    fun logout(){
        user.edit().remove(LOGIN).remove(NAME).remove(TRAINER).apply()
    }
}