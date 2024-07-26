package todo.app

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/*
* ToDoTaskComparator.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   June 26, 2024:
*       * Initialised project
*/

class ToDoTaskComparator: DiffUtil.ItemCallback<ToDoTask>() {
    // Checks if two items represent the same entity by comparing IDs
    override fun areItemsTheSame(oldItem: ToDoTask, newItem: ToDoTask): Boolean {
        return oldItem.task == newItem.task
    }

    // Checks if the contents of the items are the same by comparing the properties
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ToDoTask, newItem: ToDoTask): Boolean {
        return oldItem == newItem
    }
}