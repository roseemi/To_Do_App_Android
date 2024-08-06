package todo.app

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import todo.app.databinding.TextRowItemBinding
import java.util.Date

/*
* ToDoTaskHolder.kt
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
*/

class ToDoTaskViewHolder(private val binding: TextRowItemBinding):
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var dataManager: DataManager

    fun bind(toDoTask: ToDoTask) {
        binding.taskName.text = toDoTask.name
        binding.taskDescription.text = toDoTask.notes
        if(toDoTask.dueDate != 0L) binding.taskTime.text = Utilities.formatDate(toDoTask.dueDate)

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
            // TODO: update checkbox in db
        }

        // Open the details page when the edit button is clicked
        binding.editButton.setOnClickListener {
            val intent = Intent(itemView.context, DetailsActivity::class.java).apply {
                putExtra("toDoTaskId", toDoTask.id)
            }
            itemView.context.startActivity(intent)
        }
    }
}