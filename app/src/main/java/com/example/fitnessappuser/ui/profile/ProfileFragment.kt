package com.example.fitnessappuser.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnessappuser.R
import com.example.fitnessappuser.databinding.FragmentProfileBinding
import com.example.fitnessappuser.databinding.FragmentRegistrationBinding
import com.example.fitnessappuser.models.CurrentUser
import com.example.fitnessappuser.ui.login.EnterActivity
import com.google.firebase.messaging.FirebaseMessaging


class ProfileFragment : Fragment() {

    lateinit var user: CurrentUser
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        user = CurrentUser(requireContext())

        binding.fragment = this

        return binding.root
    }

    fun logout() {
        user.logout()
        FirebaseMessaging.getInstance().unsubscribeFromTopic(user.login)
        requireActivity().startActivity(Intent(requireContext(), EnterActivity::class.java))
        requireActivity().finish()
    }

    fun groupMuscles() {
        childFragmentManager.beginTransaction()
            .add(R.id.container_fragment_profile, GroupMusclesFragment())
            .addToBackStack("group_muscles")
            .commit()
    }

    fun exercises() {
        childFragmentManager.beginTransaction()
            .add(R.id.container_fragment_profile, ExercisesFragment())
            .addToBackStack("exercises")
            .commit()
    }
}