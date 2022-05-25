package com.example.fitnessappuser.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessappuser.databinding.GroupMusclesCardBinding
import com.example.fitnessappuser.viewModels.GroupMusclesViewModel

@SuppressLint("NotifyDataSetChanged")
class GroupMusclesAdapter(val viewModel:GroupMusclesViewModel, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<GroupMusclesAdapter.GroupMusclesHolder>() {

    init{
        viewModel.groupMuscles.observe(lifecycleOwner){
            notifyDataSetChanged()
        }
    }

    class GroupMusclesHolder(val binding: GroupMusclesCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMusclesHolder {
        return GroupMusclesHolder(
            GroupMusclesCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GroupMusclesHolder, position: Int) {
        if (!viewModel.groupMuscles.value.isNullOrEmpty()){
            holder.binding.name = viewModel.groupMuscles.value!![position]
            holder.binding.count = "${position+1}."
        }
    }

    override fun getItemCount(): Int {
        return viewModel.groupMuscles.value?.size ?: 0
    }

}