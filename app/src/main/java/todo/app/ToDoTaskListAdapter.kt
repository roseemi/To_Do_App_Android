package todo.app

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import todo.app.databinding.TextRowItemBinding

/*
* ToDoTaskListAdapter.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   July 26, 2024:
*       * Initialised project
*/

// A control class and a wrapper for the first RecyclerView
class ToDoTaskListAdapter(private val onItemClicked: (ToDoTask) -> Unit):
    ListAdapter<ToDoTask, ToDoTaskViewHolder>(ToDoTaskComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoTaskViewHolder {
        val binding = TextRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoTaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoTaskViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current)

        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
    }
}