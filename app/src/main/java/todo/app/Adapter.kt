package todo.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import todo.app.databinding.TextRowItemBinding

/*
* Adapter.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   June 26, 2024:
*       * Initialised project
*/

// A control class and a wrapper for the first RecyclerView
class Adapter(private val onItemClicked: (ToDoTask) -> Unit) :

    ListAdapter<ToDoTask, ToDoTaskHolder>(ToDoTaskComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoTaskHolder {
        val binding = TextRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoTaskHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoTaskHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current)

        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
    }
}