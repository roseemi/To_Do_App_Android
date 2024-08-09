package todo.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import todo.app.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.abs

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

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener, OnCheckboxClickedListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ToDoTaskViewModel by viewModels()

    private lateinit var gestureDetector: GestureDetector
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100

    private lateinit var auth: FirebaseAuth
    private lateinit var dataManager: DataManager

    private val adapter = ToDoTaskListAdapter(this) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gestureDetector = GestureDetector(this)


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

    // Recognise touch events
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gestureDetector.onTouchEvent(event)) {
            true
        }
        else {
            super.onTouchEvent(event)
        }
    }

    override fun onDown(e: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent) {
        return
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        return
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        try {
            val diffY = e2.y - (e1?.y ?: 0f)
            val diffX = e2.x - (e1?.x ?: 0f)
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX > 0) {
                        Toast.makeText(this, "Left to Right swipe gesture", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "Right to Left swipe gesture", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        catch (exception: Exception) {
            exception.printStackTrace()
        }
        return true
    }
}