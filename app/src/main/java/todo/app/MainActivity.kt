package todo.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import todo.app.databinding.ActivityMainBinding

/*
* MainActivity.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   June 26, 2024:
*       * Initialised project
*/

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ToDoTaskViewModel by viewModels()

    private lateinit var dataManager: DataManager

    private val adapter = Adapter { toDoTask: ToDoTask ->
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("tvShowId", toDoTask.task)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        dataManager = DataManager.instance()

        binding.firstRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.firstRecyclerView.adapter = adapter

        // Observe the LiveData from the ViewModel to update the UI
        viewModel.tasks.observe(this) { toDoTasks ->
            adapter.submitList(toDoTasks)
        }

        viewModel.loadAllTasks()

        // Temporary button to see details page!
        binding.toDoLabel.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            startActivity(intent)
        }
    }
}