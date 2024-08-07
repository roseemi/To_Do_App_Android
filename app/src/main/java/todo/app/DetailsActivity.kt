package todo.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import todo.app.databinding.ActivityDetailsBinding
import java.util.Calendar
import java.util.UUID

/*
* DetailsActivity.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   July 26, 2024:
*       * Initialised project
*   August 5, 2024:
*       * Began adding CRUD capabilities
*   August 6, 2024:
*       * Fixed date selection capabilities
*/

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: ToDoTaskViewModel by viewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var dataManager: DataManager

    private var toDoTaskId: String? = null
    private var selectedDate: Long = 0L

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
        viewModel.toDoTask.observe(this) { toDoTask ->
            toDoTask?.let {
                binding.taskNameDetails.setText(it.name)
                binding.taskDescriptionDetails.setText(it.notes)
                if(it.dueDate != 0L) binding.taskDeadlineDetails.setText(Utilities.formatDate(it.dueDate))
                if(it.dueDate != 0L) binding.calendarView.setDate(it.dueDate)
            }
        }

        // Update the selected date every time a new date is clicked on the calendar
        binding.calendarView.setOnDateChangeListener{ _, year, month, dayOfMonth ->
            // Change the view date to be equal to what was clicked
            binding.calendarView.date = Calendar.getInstance()
                .apply { set(year, month, dayOfMonth) }
                .timeInMillis
            // Save that date as a variable
            selectedDate =  binding.calendarView.date
            binding.taskDeadlineDetails.setText(Utilities.formatDate(selectedDate))
        }

        binding.saveButton.setOnClickListener {
            saveToDoTask()
        }

        binding.deleteButton.setOnClickListener {
            deleteToDoTask()
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun saveToDoTask()
    {
        val name = binding.taskNameDetails.text.toString()
        val description = binding.taskDescriptionDetails.text.toString()
        val dueDate = binding.taskDeadlineDetails.text.toString()
        val completed = binding.checkBox.isActivated

        if(name.isNotEmpty() && description.isNotEmpty())
        {
            if(name.isNotEmpty() && description.isNotEmpty()) {
                val toDoTask = ToDoTask(
                    id = toDoTaskId ?: UUID.randomUUID().toString(),
                    name = name,
                    notes = description,
                    dueDate = if(dueDate.isEmpty()) 0L else selectedDate,
                    hasDueDate = dueDate.isNotEmpty(),
                    isCompleted = completed
                )
                viewModel.saveToDoTask(toDoTask)
                Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show()
                finish()    // Takes us back to the previous activity
            }
            else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
            }
        }
        else
        {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteToDoTask() {
        toDoTaskId?.let { id ->
            AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.toDoTask.value?.let {
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