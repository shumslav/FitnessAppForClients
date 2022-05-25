package com.example.fitnessappuser.models

import com.example.fitnessappuser.enums.Calculation

data class Exercise(
    var bodyPart:String = "",
    var name:String = "",
    var calculation: Calculation = Calculation.REPETITIONS,
    var isHaveWeight: Boolean = true
)