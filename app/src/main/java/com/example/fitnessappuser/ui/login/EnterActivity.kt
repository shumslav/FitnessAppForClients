package com.example.fitnessappuser.ui.login

import NODE_USERS
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.fitnessappuser.R
import com.example.fitnessappuser.databinding.ActivityEnterBinding
import com.example.fitnessappuser.models.CurrentUser
import com.example.fitnessappuser.models.User
import com.example.fitnessappuser.ui.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import makeToast

class EnterActivity : AppCompatActivity() {

    lateinit var binding: ActivityEnterBinding
    lateinit var user: CurrentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = CurrentUser(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter)
        binding.fragment = this

        if (user.isEnterProfile()) {
            Firebase.database.reference.child(NODE_USERS).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(user.login)) {
                            unsubscribeAndSubscribeFromTopics(snapshot,user.login)
                            startActivity(Intent(this@EnterActivity, MainActivity::class.java))
                        } else {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(user.login)
                            user.logout()
                            binding.enter.setOnClickListener { enter() }
                            binding.registration.setOnClickListener { registration() }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(user.login)
                        user.logout()
                        binding.enter.setOnClickListener { enter() }
                        binding.registration.setOnClickListener { registration() }
                    }
                }
            )
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(user.login)
            user.logout()
            binding.enter.setOnClickListener { enter() }
            binding.registration.setOnClickListener { registration() }
        }
    }

    fun enter() {
        if (checkEmptyFields()) {
            val loginText = binding.login.text.toString()
            val passwordText = binding.password.text.toString()
            Firebase.database.reference.child(NODE_USERS).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(loginText)) {
                            val snapshotUser =
                                snapshot.child(loginText).getValue(User::class.java)!!
                            if (passwordText == snapshotUser.password) {
                                user.login = loginText
                                user.name = snapshotUser.name
                                user.trainerLogin = snapshotUser.loginTrainer
                                unsubscribeAndSubscribeFromTopics(snapshot,loginText)
                                startActivity(Intent(this@EnterActivity, MainActivity::class.java))
                            } else makeToast(this@EnterActivity, "Неправильный пароль")
                        } else makeToast(this@EnterActivity, "Пользователь не найден")
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        } else makeToast(this, "Заполните все поля")
    }

    private fun checkEmptyFields(): Boolean {
        return (binding.login.text.toString() != "" && binding.password.text.toString() != "")
    }

    fun registration() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container_fragment, RegistrationFragment()).addToBackStack("registration")
            .commit()
    }

    private fun unsubscribeAndSubscribeFromTopics(snapshot: DataSnapshot, login: String) {
        snapshot.children.forEach {
            val name = it.key
            if (name != null && name != login) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/$name")
            }
        }
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$login")
    }
}