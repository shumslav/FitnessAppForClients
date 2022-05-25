package com.example.fitnessappuser.models

data class AddedExercise(
    var name:String = "",
    val repetitions:String = "",
    val rounds:String = "",
    val liftedWeight:String? = null
)