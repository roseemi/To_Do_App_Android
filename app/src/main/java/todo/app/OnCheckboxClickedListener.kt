package todo.app

/*
* MainActivity.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
*   August 8, 2024:
*       * Created a listener interface to detect when/which recycler view items are clicked
*/

// Source: https://www.youtube.com/watch?v=7GPUpvcU1FE
interface OnCheckboxClickedListener {
    fun onCheckboxClicked(toDoTask: ToDoTask)
}