package com.example.fitnessappuser.ui.login

import NODE_CLIENTS
import NODE_EXERCISES
import NODE_GROUP_MUSCLES
import NODE_NAME
import NODE_PASSWORD
import NODE_TRAINERS
import NODE_USERS
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnessappuser.R
import com.example.fitnessappuser.databinding.FragmentRegistrationBinding
import com.example.fitnessappuser.databinding.FragmentScheduleBinding
import com.example.fitnessappuser.models.Client
import com.example.fitnessappuser.models.User
import com.example.fitnessappuser.notifications.NotificationDate
import com.example.fitnessappuser.notifications.PushNotification
import com.example.fitnessappuser.notifications.RetrofitInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import makeToast
import standartExercises
import standartGroupMuscles
import java.lang.Exception
import kotlin.math.log

private const val TAG = "RegistrationFragment"

class RegistrationFragment : Fragment() {

    lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        binding.fragment = this

        return binding.root
    }

    private fun checkEmptyFields(): Boolean {
        return (binding.login.text.toString() != ""
                && binding.password.text.toString() != ""
                && binding.name.text.toString() != ""
                && binding.loginTrainer.text.toString() != "")
    }

    fun registration() {
        if (checkEmptyFields()) {
            val login = binding.login.text.toString()
            val password = binding.password.text.toString()
            val name = binding.name.text.toString()
            val loginTrainer = binding.loginTrainer.text.toString()
            Firebase.database.reference.child(NODE_USERS).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.hasChild(login)) {
                            Firebase.database.reference.child(NODE_TRAINERS)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (!snapshot.hasChild(login)) {
                                                if (snapshot.hasChild(loginTrainer)) {
                                                    Firebase.database.reference.child(NODE_USERS).child(login)
                                                        .setValue(User(login, password, name, loginTrainer))
                                                    setStandartGroupAndExercises(login)
                                                    setTrainer(loginTrainer, login, name)
                                                    makeToast(requireContext(), "Пользователь успешно зарегистрирован")
                                                    sendNotificationNewClient(
                                                        PushNotification(
                                                            NotificationDate(
                                                                "Новый клиент",
                                                                "У вас появился новый клиент $name"
                                                            ),
                                                            "/topics/$loginTrainer"
                                                        )
                                                    )
                                                    this@RegistrationFragment.requireActivity().supportFragmentManager.popBackStack() }
                                                else makeToast(requireContext(), "Тренер не найден")
                                            } else makeToast(requireContext(), "Такой пользователь уже есть") }
                                        override fun onCancelled(error: DatabaseError) {} })
                        } else makeToast(requireContext(), "Такой пользователь уже есть")
                    } override fun onCancelled(error: DatabaseError) {} })
        } else { makeToast(requireContext(), "Заполните все поля") }
    }

    private fun setStandartGroupAndExercises(login: String) {
        standartGroupMuscles.forEach {
            Firebase.database.reference
                .child(NODE_USERS)
                .child(login)
                .child(NODE_GROUP_MUSCLES)
                .child(it).setValue(it)
        }
        standartExercises.forEach {
            Firebase.database.reference
                .child(NODE_USERS)
                .child(login)
                .child(NODE_EXERCISES)
                .child(it.bodyPart)
                .child(it.name)
                .setValue(it)
        }
    }

    private fun setTrainer(loginTrainer: String, login: String, name: String) {
        Firebase.database.reference
            .child(NODE_TRAINERS)
            .child(loginTrainer)
            .child(NODE_CLIENTS)
            .child(login).setValue(Client(login, name))
    }

    fun sendNotificationNewClient(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val response = RetrofitInstance.api.postNotification(notification)
        }
        catch (e: Exception){
            Log.e(TAG,e.toString())
        }
    }
}