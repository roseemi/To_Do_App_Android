package todo.app

/*
* ToDoTask.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   July 26, 2024:
*       * Initialised project
*   August 3, 2024:
*       * Refactored the ToDoTask data class to include different values
*   August 6, 2024:
*       * Added a no argument constructor for Firestore
*/

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class ToDoTask(
    @DocumentId val id: String = "",
    val name : String,
    val notes : String,
    val dueDate : Long?,
    val isCompleted : Boolean,
    val hasDueDate : Boolean
)
{
    constructor() : this("", "", "",0L, false, false)
}