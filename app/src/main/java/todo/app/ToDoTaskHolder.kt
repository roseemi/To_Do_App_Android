package todo.app

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import todo.app.databinding.TextRowItemBinding

/*
* ToDoTaskHolder.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   June 26, 2024:
*       * Initialised project
*/

class ToDoTaskHolder(private val binding: TextRowItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(toDoTask: ToDoTask) {
        binding.taskName.text = toDoTask.task
        binding.taskTime.text = toDoTask.deadline
        binding.taskDescription.text = toDoTask.description

        // Show the description if it was hidden, or hide it if it was visible
        binding.taskNotes.setOnClickListener {
            if(binding.taskDescription.visibility == View.VISIBLE) {
                binding.taskDescription.visibility = View.GONE
                binding.editButton.visibility = View.GONE
            }
            else {
                binding.taskDescription.visibility = View.VISIBLE
                binding.editButton.visibility = View.VISIBLE
            }
        }

        binding.checkBox.setOnClickListener{
            if(binding.checkBox.isChecked) {
                binding.taskCard.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#EEEEEE")))
            }
            else {
                binding.taskCard.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#F8FFE5")))
            }
        }
    }
}