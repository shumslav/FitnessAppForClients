package com.example.fitnessappuser.adapters

import NODE_EXERCISES
import NODE_GROUP_MUSCLES
import NODE_USERS
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessappuser.databinding.GroupMusclesCardBinding
import com.example.fitnessappuser.models.Exercise
import com.example.fitnessappuser.viewModels.ExercisesViewModel
import com.example.fitnessappuser.viewModels.GroupMusclesViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@SuppressLint("NotifyDataSetChanged")
class ExercisesAdapter(val viewModel: ExercisesViewModel, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<ExercisesAdapter.ExercisesHolder>() {

    init{
        viewModel.selectedExercises.observe(lifecycleOwner){
            notifyDataSetChanged()
        }
    }

    class ExercisesHolder(val binding: GroupMusclesCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesHolder {
        return ExercisesHolder(
            GroupMusclesCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ExercisesHolder, position: Int) {
        if (!viewModel.selectedExercises.value.isNullOrEmpty()){
            holder.binding.name = viewModel.selectedExercises.value!![position].name
            holder.binding.count = "${position+1}."
        }
    }

    override fun getItemCount(): Int {
        return viewModel.selectedExercises.value?.size ?: 0
    }

    fun deleteExercise(Exercise: Exercise) {
        Firebase.database.reference
            .child(NODE_USERS)
            .child(viewModel.user.login)
            .child(NODE_EXERCISES)
            .child(Exercise.bodyPart)
            .child(Exercise.name)
            .removeValue()
        viewModel.getGroupsMuscleAndExercises()
    }

}