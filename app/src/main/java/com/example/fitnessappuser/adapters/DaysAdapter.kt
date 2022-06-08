package com.example.fitnessappuser.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessappuser.databinding.ScheduleDayCardBinding
import com.example.fitnessappuser.viewModels.ScheduleViewModel
import com.example.fitnessappuser.models.CalendarDay
import com.google.android.material.card.MaterialCardView

@SuppressLint("NotifyDataSetChanged")
class DaysAdapter(val viewmodel: ScheduleViewModel, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<DaysAdapter.DayCardHolder>() {

    private var lastPosition: MaterialCardView? = null
    private var lastCalendarDay: CalendarDay? = null
    var id = -1

    init {
        viewmodel.datesData.observe(
            lifecycleOwner
        ) { t ->
            Log.i("MyError", "UpdateDates")
            lastPosition = null
            lastCalendarDay = null
            this@DaysAdapter.notifyDataSetChanged()
        }
        viewmodel.trainNotes.observe(
            lifecycleOwner
        ) {
            this@DaysAdapter.notifyDataSetChanged()
        }
    }

    class DayCardHolder(val binding: ScheduleDayCardBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayCardHolder {
        return DayCardHolder(
            ScheduleDayCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: DayCardHolder, position: Int) {
        if (!viewmodel.datesData.value.isNullOrEmpty()) {
            val calendarDay = viewmodel.datesData.value!![position]
            holder.binding.calendarDay = calendarDay
            holder.binding.countCompletedTrains.text = ""
            holder.binding.countUncompletedTrains.text = ""
            holder.binding.completedCard.visibility = View.GONE
            holder.binding.uncompletedCard.visibility = View.GONE
            holder.binding.cardDay.setOnClickListener {
                viewmodel.lastPickedDay.value = calendarDay
                if (id != -1){
                    notifyItemChanged(id)
                }
                id = holder.adapterPosition
                notifyItemChanged(holder.adapterPosition)

            }
            if (calendarDay.dateString == viewmodel.lastPickedDay.value?.dateString) {
                holder.binding.cardDay.setCardBackgroundColor(Color.rgb(82, 103, 209))
                holder.binding.month.setTextColor(Color.WHITE)
                holder.binding.day.setTextColor(Color.WHITE)
                id = holder.adapterPosition
            }
            else {
                holder.binding.cardDay.setCardBackgroundColor(Color.WHITE)
                holder.binding.month.setTextColor(Color.rgb(82, 103, 209))
                holder.binding.day.setTextColor(Color.rgb(82, 103, 209))
            }
            if (!viewmodel.trainNotes.value.isNullOrEmpty()){
                if (viewmodel.trainNotes.value!!.containsKey(calendarDay.dateString)){
                    val trainNotesInDay = viewmodel.trainNotes.value!![calendarDay.dateString]!!
                    var countCompleted = 0
                    var countUncompleted  = 0
                    trainNotesInDay.forEach {
                        if (it.isCompleted)
                            countCompleted+=1
                        else
                            countUncompleted+=1
                    }
                    if (countCompleted>0) {
                        holder.binding.countCompletedTrains.text = countCompleted.toString()
                        holder.binding.completedCard.visibility = View.VISIBLE
                    }
                    else {
                        holder.binding.countCompletedTrains.text = ""
                        holder.binding.completedCard.visibility = View.GONE
                    }
                    if (countUncompleted>0) {
                        holder.binding.countUncompletedTrains.text = countUncompleted.toString()
                        holder.binding.uncompletedCard.visibility = View.VISIBLE
                    }
                    else {
                        holder.binding.countUncompletedTrains.text = ""
                        holder.binding.uncompletedCard.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return viewmodel.datesData.value?.size ?: 0
    }
}