package com.example.fitnessappuser.viewModels

import NODE_MESSAGES
import NODE_TRAINERS
import NODE_USERS
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessappuser.models.CurrentUser
import com.example.fitnessappuser.models.Message
import com.example.fitnessappuser.models.Trainer
import com.example.fitnessappuser.notifications.NotificationDate
import com.example.fitnessappuser.notifications.PushNotification
import com.example.fitnessappuser.notifications.RetrofitInstance
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

private const val TAG = "ChatViewModel"

class ChatViewModel(private val myApplication: Application) : AndroidViewModel(myApplication) {
    val messages: MutableLiveData<MutableList<Message>> = MutableLiveData()
    val currentTrainer: MutableLiveData<Trainer> = MutableLiveData()
    val currentUser = CurrentUser(myApplication)
    private val refMessages = Firebase.database.reference.child(NODE_USERS).child(currentUser.login)
        .child(NODE_MESSAGES)

    init {
        refMessages.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = mutableListOf<Message>()
                    snapshot.children.forEach {
                        result.add(it.getValue(Message::class.java)!!)
                    }
                    messages.value = result
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )

        Firebase.database.reference.child(NODE_TRAINERS).child(currentUser.trainerLogin)
            .addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val trainer = snapshot.getValue(Trainer::class.java)
                        currentTrainer.value = trainer!!
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
            )
    }

    fun sendMessage(msg:String) {
        if (msg.isEmpty())
            return
        val calendar = Calendar.getInstance()
        val time = getTime(calendar)
        val date = getDate(calendar)
        val message =
            Message(msg, currentUser.login, time, date)

        Firebase.database.reference.child(NODE_USERS).child(currentUser.login)
            .child(NODE_MESSAGES).push().setValue(message)

        sendNotificationNewMessage(
            PushNotification(
                NotificationDate(
                    "Новое сообщение от клиента",
                    "Клиент ${currentUser.name} отправил вам новое новое сообщение"
                ),
                "/topics/${currentUser.trainerLogin}"
            )
        )
    }

    private fun getDate(calendar: Calendar): String {
        val day = if ((calendar.get(Calendar.DAY_OF_MONTH)).toString().length == 1)
            "0${calendar.get(Calendar.DAY_OF_MONTH)}"
        else
            "${calendar.get(Calendar.DAY_OF_MONTH)}"

        val month = if ((calendar.get(Calendar.MONTH)+1).toString().length == 1)
            "0${calendar.get(Calendar.MONTH)+1}"
        else
            "${calendar.get(Calendar.MONTH)+1}"

        val year = if (calendar.get(Calendar.YEAR).toString().length == 1)
            "0${calendar.get(Calendar.YEAR)}"
        else
            "${calendar.get(Calendar.YEAR)}"
        return "${day}:${month}:${year}"
    }

    fun getTime(calendar: Calendar): String {
        val hours = if (calendar.get(Calendar.HOUR_OF_DAY).toString().length == 1)
            "0${calendar.get(Calendar.HOUR_OF_DAY)}"
        else
            "${calendar.get(Calendar.HOUR_OF_DAY)}"

        val minutes = if (calendar.get(Calendar.MINUTE).toString().length == 1)
            "0${calendar.get(Calendar.MINUTE)}"
        else
            "${calendar.get(Calendar.MINUTE)}"

        return "${hours}:${minutes}"
    }

    fun sendNotificationNewMessage(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val response = RetrofitInstance.api.postNotification(notification)
        }
        catch (e: Exception){
            Log.e(TAG,e.toString())
        }
    }
}