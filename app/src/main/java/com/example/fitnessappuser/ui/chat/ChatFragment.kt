package com.example.fitnessappuser.ui.chat

import NODE_MESSAGES
import NODE_USERS
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessappuser.adapters.MessagesAdapter
import com.example.fitnessappuser.databinding.FragmentChatBinding
import com.example.fitnessappuser.models.Message
import com.example.fitnessappuser.viewModels.ChatViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class ChatFragment : Fragment() {

    lateinit var viewModel: ChatViewModel
    lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        binding = FragmentChatBinding.inflate(inflater, container, false)

        viewModel.currentTrainer.observe(viewLifecycleOwner){
            binding.name.text = it.name
        }

        binding.recyclerMessages.adapter = MessagesAdapter(viewModel,viewLifecycleOwner)
        binding.recyclerMessages.layoutManager = LinearLayoutManager(requireContext())

        binding.send.setOnClickListener {
            viewModel.sendMessage(binding.message.text.toString())
            binding.message.text.clear()
        }

        return binding.root
    }


}