package com.example.fitnessappuser.ui.schedule

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessappuser.R
import com.example.fitnessappuser.databinding.FragmentScheduleBinding
import com.example.fitnessappuser.adapters.DaysAdapter
import com.example.fitnessappuser.adapters.TrainNotesAdapter
import com.example.fitnessappuser.viewModels.ScheduleViewModel
import java.util.*


class ScheduleFragment : Fragment() {

    lateinit var viewModel: ScheduleViewModel
    lateinit var startDateListener:OnDateSetListener
    lateinit var endDateListener:OnDateSetListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ScheduleViewModel::class.java]

        startDateListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                viewModel.calendarStart.set(Calendar.YEAR, year)
                viewModel.calendarStart.set(Calendar.MONTH, monthOfYear)
                viewModel.calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel.setDates()
                binding.startDate.text = viewModel.startDate
            }

        endDateListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                viewModel.calendarEnd.set(Calendar.YEAR, year)
                viewModel.calendarEnd.set(Calendar.MONTH, monthOfYear)
                viewModel.calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel.setDates()
                binding.endDate.text = viewModel.endDate
            }

        binding.viewmodel = viewModel
        binding.recyclerDays.adapter = DaysAdapter(viewModel, this)
        binding.recyclerDays.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerNotes.adapter = TrainNotesAdapter(viewModel,viewLifecycleOwner, this)
        binding.recyclerNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.fragment = this

        return binding.root
    }

    fun setDateStart() {
        DatePickerDialog(
            requireContext(), startDateListener,
            viewModel.calendarStart.get(Calendar.YEAR),
            viewModel.calendarStart.get(Calendar.MONTH),
            viewModel.calendarStart.get(Calendar.DAY_OF_MONTH)
        )
            .show()
    }

    fun setDateEnd() {
        DatePickerDialog(
            requireContext(), endDateListener,
            viewModel.calendarEnd.get(Calendar.YEAR),
            viewModel.calendarEnd.get(Calendar.MONTH),
            viewModel.calendarEnd.get(Calendar.DAY_OF_MONTH)
        )
            .show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTrainNotes()
    }

}