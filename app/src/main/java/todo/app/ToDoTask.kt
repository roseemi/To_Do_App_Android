package todo.app

/*
* ToDoTask.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   June 26, 2024:
*       * Initialised project
*/

class ToDoTask {
    val task: String
    val deadline: String
    val description: String

    constructor(task: String, deadline: String, description: String) {
        this.task = task
        this.deadline = deadline
        this.description = description
    }
}