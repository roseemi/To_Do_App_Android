package todo.app

import android.graphics.Color
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
*   August 7, 2024:
*       * Added date control and a "has due date" switch
*       * Added dialogue for saving and canceling changes
*/

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: ToDoTaskViewModel by viewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var dataManager: DataManager

    // Management vars for task state
    private var toDoTaskId: String? = null
    private var selectedDate: Long? = null
    private var defaultToDoTask: ToDoTask = ToDoTask()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataManager = DataManager.instance()

        auth = FirebaseAuth.getInstance()

        val minDate = binding.calendarView.minDate
        val maxDate = binding.calendarView.maxDate

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
                binding.hasDueDateSwitch.isChecked = toDoTask.hasDueDate
                if(it.dueDate != null) binding.taskDeadlineDetails.setText(Utilities.formatDate(it.dueDate))
                if(it.dueDate != null) binding.calendarView.setDate(it.dueDate)
                if(toDoTask.completed) {
                    binding.detailsConstraintLayout.setBackgroundColor(Color.parseColor("#407DDE92"))
                    binding.checkBox.isChecked = true
                }
                updateCalendar(minDate, maxDate, toDoTask)
                selectedDate = it.dueDate
                defaultToDoTask = toDoTask.copy()
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
            binding.taskDeadlineDetails.setText(Utilities.formatDate(selectedDate!!))
        }

        // Change the background colour of the item based on the item's completion statue
        binding.checkBox.setOnClickListener{
            if(binding.checkBox.isChecked) {
                binding.detailsConstraintLayout.setBackgroundColor(Color.parseColor("#407DDE92"))
            }
            else {
                binding.detailsConstraintLayout.setBackgroundColor(Color.parseColor("#F8FFE5"))
            }
        }

        // Clear the display date if no due date on the switch is selected
        binding.hasDueDateSwitch.setOnClickListener {
            if(!binding.hasDueDateSwitch.isChecked) binding.taskDeadlineDetails.text = null
        }

        binding.saveButton.setOnClickListener {
            saveToDoTask()
        }

        binding.deleteButton.setOnClickListener {
            deleteToDoTask()
        }

        binding.cancelButton.setOnClickListener {
            cancelChanges()
        }
    }

    // Disable or enable the calendar based on the status of the switch
    private fun updateCalendar(minDate : Long, maxDate : Long, toDoTask : ToDoTask) {
        if (!toDoTask.hasDueDate) {
            // Only valid date will be today
            binding.calendarView.minDate = System.currentTimeMillis()
            binding.calendarView.maxDate = System.currentTimeMillis()
        }
        else {
            binding.calendarView.minDate = minDate
            binding.calendarView.maxDate = maxDate
        }
    }

    // Save task in the database
    private fun saveToDoTask()
    {
        toDoTaskId?.let {
            AlertDialog.Builder(this)
                .setTitle("Save Task")
                .setMessage("Are you sure you want to save this task?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.toDoTask.value?.let {
                        val name = binding.taskNameDetails.text.toString()
                        val description = binding.taskDescriptionDetails.text.toString()

                        if(name.isNotEmpty() && description.isNotEmpty())
                        {
                            if(name.isNotEmpty() && description.isNotEmpty()) {

                                val updatedToDoTask = createUpdatedToDoTask()
                                viewModel.saveToDoTask(updatedToDoTask)

                                Toast.makeText(
                                    this,
                                    "Task Saved",
                                    Toast.LENGTH_SHORT).show()

                                finish()
                            }
                            else {
                                Toast.makeText(
                                    this,
                                    "Please fill all fields",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                        else
                        {
                            Toast.makeText(
                                this,
                                "Please fill all fields",
                                Toast.LENGTH_SHORT).show()
                        }
                        finish()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    // Delete task in the database
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

    // Return to the previous activity
    private fun cancelChanges() {
        val ttoDoTask = createUpdatedToDoTask()

        if(defaultToDoTask != ttoDoTask) {
            AlertDialog.Builder(this)
                .setTitle("Cancel changes")
                .setMessage("Are you sure you want to discard your changes?")
                .setPositiveButton("Yes") { _, _ ->
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
        else {
            finish()
        }
    }

    // Bind the new properties to the task
    private fun createUpdatedToDoTask(): ToDoTask {
        val name = binding.taskNameDetails.text.toString()
        val description = binding.taskDescriptionDetails.text.toString()
        val hasDueDate = binding.hasDueDateSwitch.isChecked
        val isCompleted = binding.checkBox.isChecked

        return ToDoTask(
            id = toDoTaskId ?: UUID.randomUUID().toString(),
            name = name,
            notes = description,
            dueDate = if (hasDueDate) selectedDate else null,
            hasDueDate = hasDueDate,
            completed = isCompleted
        )
    }
}