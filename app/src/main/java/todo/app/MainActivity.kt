package todo.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
*   August 9, 2024:
*       * Added swipe detection on recyclerview items
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

        // Setup movement and movement detection on recyclerview items
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Perform an action when an item is swiped
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val toDoTask = adapter.getItemAtPosition(position)

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("Delete Task")
                            .setMessage("Are you sure you want to delete this task?")
                            .setPositiveButton("Yes") { _, _ ->
                                viewModel.deleteToDoTask(toDoTask)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Task Deleted",
                                    Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("No", null)
                            .show()
                    }
                    ItemTouchHelper.RIGHT -> {
                        val intent = Intent(
                            this@MainActivity,
                            DetailsActivity::class.java)
                            .apply {
                                putExtra("toDoTaskId", toDoTask.id)
                            }
                        startActivity(intent)
                    }
                }
                // Reload the item in the adapter
                adapter.notifyItemChanged(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.firstRecyclerView)

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

    // Runs when the app is reloaded from an inactive state
    override fun onResume() {
        super.onResume()
        viewModel.loadAllToDoTasks()
    }

    // Update the change in completion status whenever the checkmark is pressed
    override fun onCheckboxClicked(toDoTask: ToDoTask) {
        val updatedToDoTask = toDoTask.copy(completed = !toDoTask.completed)
        viewModel.saveToDoTask(updatedToDoTask)
    }
}