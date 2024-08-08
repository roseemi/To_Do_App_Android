package todo.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import todo.app.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/*
* MainActivity.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   July 26, 2024:
*       * Initialised project
*   August 3, 2024:
*       * Refactored to match the updated ToDoTask data class
*   August 8, 2024:
*       * Created a listener interface to detect when/which recycler view items are clicked
*/

class MainActivity : AppCompatActivity(), OnCheckboxClickedListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ToDoTaskViewModel by viewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var dataManager: DataManager

    private val adapter = ToDoTaskListAdapter(this) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialise Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialise Firestore and DataManager
        FirebaseFirestore.setLoggingEnabled(true)

        dataManager = DataManager.instance()

        binding.firstRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.firstRecyclerView.adapter = adapter

        // Observe the LiveData from the ViewModel to update the UI
        viewModel.tasks.observe(this) { toDoTasks ->
            adapter.submitList(toDoTasks)
        }

        viewModel.loadAllToDoTasks()

        // Let the user add a new entry
        binding.addButton.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAllToDoTasks()
    }

    // Update the change in completion status whenever the checkmark is pressed
    override fun onCheckboxClicked(toDoTask: ToDoTask) {
        val updatedToDoTask = toDoTask.copy(completed = !toDoTask.completed)
        Log.i("updatedtask", updatedToDoTask.completed.toString())
        viewModel.saveToDoTask(updatedToDoTask)
    }
}