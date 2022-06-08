package com.example.fitnessappuser.models

import android.net.ParseException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class TrainNote(
    var id:Int = 0,
    var date:String = "",
    var startTime:String = "",
    var finishTime:String = "",
    var bodyPart:String = "",
    var exercises:MutableList<AddedExercise> = mutableListOf(),
    var notes:String = "",
    var isCompleted:Boolean = false,
    var review:String = ""
){
    val duration:String
        get() {
            val df: DateFormat = SimpleDateFormat("hh:mm", Locale.US)
            return try {
                val date1: Date = df.parse(startTime)!!
                val date2: Date = df.parse(finishTime)!!
                val diff: Long = (date2.time - date1.time) / 1000
                if (diff < 0)
                    "${1440 + (diff / 60)} мин"
                else
                    "${diff / 60} мин"
            } catch (e: ParseException){
                "0 мин"
            }
        }
    val dateWithDot: String
        get() {
            return date.replace(":", ".")
        }

    val allTime: String
        get() {
            return "${startTime}-${finishTime}"
        }
}