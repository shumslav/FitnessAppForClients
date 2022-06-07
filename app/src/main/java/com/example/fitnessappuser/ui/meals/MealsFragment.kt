package com.example.fitnessappuser.ui.meals

import NODE_MEALS
import NODE_USERS
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessappuser.R
import com.example.fitnessappuser.adapters.MealsAdapter
import com.example.fitnessappuser.databinding.FragmentMealsBinding
import com.example.fitnessappuser.models.Meal
import com.example.fitnessappuser.viewModels.ExercisesViewModel
import com.example.fitnessappuser.viewModels.MealsViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import makeToast

class MealsFragment : Fragment() {

    lateinit var binding: FragmentMealsBinding
    lateinit var viewModel: MealsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MealsViewModel::class.java]

        binding.dayOfWeek.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            viewModel.daysOfWeek
        )

        binding.meals.adapter = MealsAdapter(viewModel, viewLifecycleOwner)
        binding.meals.layoutManager = LinearLayoutManager(requireContext())

        viewModel.meals.observe(viewLifecycleOwner) {
            setMeal(viewModel.meals.value!![binding.dayOfWeek.selectedItem as String]!!)
        }

        binding.dayOfWeek.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val day = viewModel.daysOfWeek[p2]
                if (viewModel.meals.value != null)
                    setMeal(viewModel.meals.value!![day]!!)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        return binding.root
    }

    fun setMeal(meal: Meal) {
        binding.calories.text = meal.calories
        binding.protein.text = meal.protein
        binding.fats.text = meal.fats
        binding.carbohydrates.text = meal.carbohydrates
        viewModel.selectedMeals.value = meal.meals
        if (meal.notes == ""){
            binding.notesForm.visibility = View.GONE
        }
        else{
            binding.notes.text = meal.notes
            binding.notesForm.visibility = View.VISIBLE
        }

    }
}