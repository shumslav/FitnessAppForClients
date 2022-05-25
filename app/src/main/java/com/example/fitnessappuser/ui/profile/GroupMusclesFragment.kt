package com.example.fitnessappuser.ui.profile

import NODE_GROUP_MUSCLES
import NODE_USERS
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessappuser.R
import com.example.fitnessappuser.adapters.GroupMusclesAdapter
import com.example.fitnessappuser.databinding.FragmentGroupMusclesBinding
import com.example.fitnessappuser.databinding.FragmentScheduleBinding
import com.example.fitnessappuser.models.CurrentUser
import com.example.fitnessappuser.viewModels.GroupMusclesViewModel
import com.example.fitnessappuser.viewModels.ScheduleViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import makeToast

class GroupMusclesFragment : Fragment() {

    lateinit var viewModel:GroupMusclesViewModel
    lateinit var user: CurrentUser
    lateinit var binding: FragmentGroupMusclesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupMusclesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[GroupMusclesViewModel::class.java]
        user = CurrentUser(requireContext())
        val adapter = GroupMusclesAdapter(viewModel,this)

        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.fragment = this

        return binding.root
    }
}