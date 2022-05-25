package com.example.fitnessappuser.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessappuser.R
import com.example.fitnessappuser.databinding.ScheduleNoteCardBinding
import com.example.fitnessappuser.ui.schedule.TrainNoteFragment
import com.example.fitnessappuser.viewModels.ScheduleViewModel

@SuppressLint("NotifyDataSetChanged")
class TrainNotesAdapter(
    val viewmodel: ScheduleViewModel,
    lifecycleOwner: LifecycleOwner,
    val fragment: Fragment
) :
    RecyclerView.Adapter<TrainNotesAdapter.TrainNotesHolder>() {

    var pickedDate: String? = null

    init {
        viewmodel.trainNotes.observe(lifecycleOwner) {
            notifyDataSetChanged()
        }
        viewmodel.lastPickedDay.observe(lifecycleOwner) {
            pickedDate = it?.dateString
            notifyDataSetChanged()
        }
    }

    class TrainNotesHolder(val binding: ScheduleNoteCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainNotesHolder {
        return TrainNotesHolder(
            ScheduleNoteCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrainNotesHolder, position: Int) {
        if (pickedDate != null) {
            if (viewmodel.trainNotes.value != null) {
                if (viewmodel.trainNotes.value!!.containsKey(pickedDate)) {
                    val trainNote = viewmodel.trainNotes.value!![pickedDate]!![position]
                    holder.binding.trainNote = trainNote
                    if (trainNote.isCompleted)
                        holder.binding.status.text = "Выполнена"
                    else
                        holder.binding.status.text = "Не выполнена"
                    holder.binding.trainNoteCard.setOnClickListener {
                        fragment.childFragmentManager.beginTransaction()
                            .add(
                                R.id.fragment_container,
                                TrainNoteFragment.newInstance(
                                    trainNote.date,
                                    trainNote.id.toString()
                                )
                            )
                            .addToBackStack("TrainNoteFragment")
                            .commit()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (pickedDate == null)
            return 0
        if (viewmodel.trainNotes.value.isNullOrEmpty())
            return 0
        if (!viewmodel.trainNotes.value!!.containsKey(pickedDate))
            return 0
        else
            return viewmodel.trainNotes.value!![pickedDate]!!.size
    }
}
