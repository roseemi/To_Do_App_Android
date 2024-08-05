package todo.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import todo.app.databinding.ActivityDetailsBinding
import java.util.UUID

/*
* DetailsActivity.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   June 26, 2024:
*       * Initialised project
*   August 5, 2024:
*       * Began adding CRUD capabilities
*/

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: ToDoTaskViewModel by viewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var dataManager: DataManager

    private var toDoTaskId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataManager = DataManager.instance()

        auth = FirebaseAuth.getInstance()

        // Saves from one screen to another
        toDoTaskId = intent.getStringExtra("toDoTaskId")

        if(toDoTaskId != null)
        {
            viewModel.loadToDoTaskById(toDoTaskId!!)
        }
        else
        {
            binding.deleteButton.visibility = View.GONE
        }

        // Observe the LiveData from the ViewModel to update the UI
        viewModel.tvShow.observe(this) { tvShow ->
            tvShow?.let {
                binding.taskNameDetails.setText(it.name)
                binding.taskDescriptionDetails.setText(it.notes)
                binding.taskDeadlineDetails.setText(it.dueDate.toString())
            }
        }

        binding.editButton.setOnClickListener {
            if(auth.currentUser != null) saveToDoTask()
        }

        binding.deleteButton.setOnClickListener {
            if(auth.currentUser != null) deleteToDoTask()
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }
    private fun saveToDoTask()
    {
        val name = binding.taskNameDetails.text.toString()
        val description = binding.taskDescriptionDetails.text.toString()
        val deadline = binding.calendarView.date

//        if(name.isNotEmpty() && description.isNotEmpty())
//        {
//            if(name.isNotEmpty() && description.isNotEmpty()) {
//                val tvShow = ToDoTask(
//                    id = toDoTaskId ?: UUID.randomUUID().toString(),
//                    name = name,
//                    notes = description,
//                    dueDate = deadline,
//                    hasDueDate = deadline.isNotBlank(),
//                    isCompleted = deadline.)
//                viewModel.saveToDoTask(tvShow)
//                Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show()
//                finish()    // Takes us back to the previous activity
//            }
//            else {
//                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
//            }
//        }
//        else
//        {
//            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun deleteToDoTask() {
        toDoTaskId?.let { id ->
            AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this Task?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.tvShow.value?.let {
                        viewModel.deleteToDoTask(it)
                        Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}