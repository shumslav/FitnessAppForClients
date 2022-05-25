package com.example.fitnessappuser.viewModels

import NODE_EXERCISES
import NODE_GROUP_MUSCLES
import NODE_USERS
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessappuser.models.CurrentUser
import com.example.fitnessappuser.models.Exercise
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ExercisesViewModel(private val myApplication: Application): AndroidViewModel(myApplication) {

    val groupsMuscle: MutableLiveData<MutableList<String>> = MutableLiveData()
    val groupExercises: MutableLiveData<MutableMap<String,MutableList<Exercise>>> = MutableLiveData()
    val selectedGroup: MutableLiveData<String> = MutableLiveData()
    val selectedExercises:MutableLiveData<MutableList<Exercise>> = MutableLiveData()
    val user = CurrentUser(myApplication)

    init {
        getGroupsMuscleAndExercises()
    }

    fun getGroupsMuscleAndExercises() {
        Firebase.database.reference
            .child(NODE_USERS)
            .child(user.login)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val groups = mutableListOf<String>()
                        val exercisesMap = mutableMapOf<String, MutableList<Exercise>>()
                        snapshot
                            .child(NODE_GROUP_MUSCLES)
                            .children.forEach {
                                val group = it.getValue(String::class.java)
                                if (!group.isNullOrEmpty()) {
                                    groups.add(group)
                                }
                            }
                        groups.forEach { group ->
                            val exercises = mutableListOf<Exercise>()
                            snapshot.child(NODE_EXERCISES).child(group).children.forEach { it1 ->
                                val exercise = it1.getValue(Exercise::class.java)
                                if (exercise != null)
                                    exercises.add(exercise)
                            }
                            exercisesMap[group] = exercises
                        }
                        if (groupsMuscle.value != groups)
                            groupsMuscle.value = groups
                        groupExercises.value = exercisesMap
                        Log.i("GroupsHere",groupsMuscle.value.toString())
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
            )
    }
}