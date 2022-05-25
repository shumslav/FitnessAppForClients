package com.example.fitnessappuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessappuser.databinding.TrainNoteExerciseCardBinding
import com.example.fitnessappuser.models.TrainNote

class TrainNoteExercisesAdapter(val trainNote:MutableLiveData<TrainNote>) :
    RecyclerView.Adapter<TrainNoteExercisesAdapter.TrainNoteExercisesHolder>() {

    class TrainNoteExercisesHolder(val binding: TrainNoteExerciseCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainNoteExercisesHolder {
        return TrainNoteExercisesHolder(
            TrainNoteExerciseCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrainNoteExercisesHolder, position: Int) {
        if (trainNote.value!=null){
            val currentTrainNote = trainNote.value!!
            val exercises = currentTrainNote.exercises
            val cont = "${position+1}."
            val exercise = exercises[position]
            holder.binding.count.text = cont
            holder.binding.name.text = exercise.name
            val data = if (exercise.liftedWeight==null)
                "${exercise.repetitions} | ${exercise.rounds}"
            else
                "${exercise.repetitions} | ${exercise.rounds} | ${exercise.liftedWeight}"
            holder.binding.data.text = data
        }
    }

    override fun getItemCount(): Int {
        return trainNote.value?.exercises?.size ?: 0
    }
}