package com.example.fitnessappuser.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessappuser.databinding.MealCardBinding
import com.example.fitnessappuser.models.SomeMeal
import com.example.fitnessappuser.viewModels.MealsViewModel

@SuppressLint("NotifyDataSetChanged")
class MealsAdapter(val viewModel: MealsViewModel, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<MealsAdapter.MealsHolder>() {

    var sortedMeals = mutableListOf<SomeMeal>()

    init{
        viewModel.selectedMeals.observe(lifecycleOwner){
            Log.i("UNSORTED", it.toString())
            sortedMeals = viewModel.sortMeals(it)
            Log.i("SORTED", sortedMeals.toString())
            notifyDataSetChanged()
        }
    }

    class MealsHolder(val binding: MealCardBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsHolder {
        return MealsHolder(
            MealCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MealsHolder, position: Int) {
        holder.binding.meal = sortedMeals[position]
        val numberMeal = "Прием пищи ${position+1}"
        holder.binding.numberMeal.text = numberMeal
    }

    override fun getItemCount(): Int {
        return sortedMeals.size
    }
}