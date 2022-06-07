package com.example.fitnessappuser.models

data class Meal(
    var weekDay:String = "",
    var protein:String = "",
    var fats:String = "",
    var carbohydrates:String = "",
    var calories:String = "",
    var notes:String = "",
    var meals:MutableList<SomeMeal> = mutableListOf()
)