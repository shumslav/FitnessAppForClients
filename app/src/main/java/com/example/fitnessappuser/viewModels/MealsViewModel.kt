package com.example.fitnessappuser.viewModels

import NODE_MEALS
import NODE_USERS
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessappuser.models.CurrentUser
import com.example.fitnessappuser.models.Meal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MealsViewModel(private val myApplication: Application) : AndroidViewModel(myApplication) {

    val meals: MutableLiveData<MutableMap<String,Meal>> = MutableLiveData()
    val user = CurrentUser(myApplication)

    val daysOfWeek =
        listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")


    init {
        getMeals()
    }
    fun getMeals(){
        Firebase.database.reference
            .child(NODE_USERS)
            .child(user.login)
            .child(NODE_MEALS).addValueEventListener(
                object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val currentMeals = mutableMapOf<String,Meal>()
                        daysOfWeek.forEach { day ->
                            if (snapshot.hasChild(day))
                                currentMeals[day] = snapshot.child(day).getValue(Meal::class.java)!!
                            else
                                currentMeals[day] = Meal()
                        }
                        meals.value = currentMeals
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }
            )
    }
}