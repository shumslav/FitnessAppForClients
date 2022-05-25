package com.example.fitnessappuser.ui.schedule

import NODE_MESSAGES
import NODE_TRAINERS
import NODE_TRAIN_NOTES
import NODE_USERS
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessappuser.R
import com.example.fitnessappuser.adapters.TrainNoteExercisesAdapter
import com.example.fitnessappuser.databinding.FragmentTrainNoteBinding
import com.example.fitnessappuser.models.CurrentUser
import com.example.fitnessappuser.models.TrainNote
import com.example.fitnessappuser.models.Trainer
import com.example.fitnessappuser.notifications.NotificationDate
import com.example.fitnessappuser.notifications.PushNotification
import com.example.fitnessappuser.notifications.RetrofitInstance
import com.example.fitnessappuser.viewModels.ScheduleViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


private const val DATE = "Date"
private const val Id = "Id"
private const val TAG = "TrainNoteFragment"

class TrainNoteFragment : Fragment() {
    private var date: String? = null
    private var id: String? = null
    lateinit var binding: FragmentTrainNoteBinding
    lateinit var user: CurrentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = it.getString(DATE)
            id = it.getString(Id)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrainNoteBinding.inflate(inflater, container, false)
        user = CurrentUser(requireContext())
        val viewModel = ViewModelProvider(requireActivity())[ScheduleViewModel::class.java]
        if (date == null || id == null)
            requireActivity().onBackPressed()
        else {
            val trainNote: MutableLiveData<TrainNote> = MutableLiveData()
            binding.exercisesRecycler.adapter = TrainNoteExercisesAdapter(trainNote)
            binding.exercisesRecycler.layoutManager = LinearLayoutManager(requireContext())
            Firebase.database.reference.child(NODE_USERS).child(user.login)
                .child(NODE_TRAIN_NOTES)
                .child(date!!)
                .child(id!!).addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val currentTrainNote = snapshot.getValue(TrainNote::class.java)
                            if (currentTrainNote != null)
                                trainNote.value = currentTrainNote
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
            trainNote.observe(viewLifecycleOwner) {
                binding.trainNote = it
                if (it.notes != "") {
                    binding.notes.text = it.notes
                    binding.notesForm.visibility = View.VISIBLE
                } else {
                    binding.notesForm.visibility = View.GONE
                }
                if (it.isCompleted)
                    binding.acceptForm.visibility = View.GONE
                else
                    binding.acceptForm.visibility = View.VISIBLE
                binding.exercisesRecycler.adapter?.notifyDataSetChanged()
            }
            binding.accept.setOnClickListener {
                if (trainNote.value != null) {
                    val currentTrainNote = trainNote.value!!
                    currentTrainNote.isCompleted = true
                    Firebase.database.reference.child(NODE_USERS).child(user.login)
                        .child(NODE_TRAIN_NOTES)
                        .child(date!!)
                        .child(id!!).setValue(currentTrainNote).addOnCompleteListener {
                            sendNotificationAcceptNote(
                                PushNotification(
                                    NotificationDate(
                                        "Клиент выполнил тренировку",
                                        "${user.name} выполнил тренировку\n${
                                            date!!.replace(
                                                ":",
                                                "."
                                            )
                                        }\n${trainNote.value!!.allTime}"
                                    ),
                                    "/topics/${user.trainerLogin}"
                                )
                            )
                        }
                    viewModel.getTrainNotes()
                    requireActivity().onBackPressed()
                }
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(date: String, id: String) =
            TrainNoteFragment().apply {
                arguments = Bundle().apply {
                    putString(DATE, date)
                    putString(Id, id)
                }
            }
    }

    fun sendNotificationAcceptNote(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
}