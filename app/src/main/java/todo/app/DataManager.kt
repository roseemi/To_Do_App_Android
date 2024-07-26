package todo.app

/*
* DataManager.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   June 26, 2024:
*       * Initialised project
*/

class DataManager private constructor() {
    companion object {

        private const val TAG = "DataManager"

        @Volatile
        private var m_instance: DataManager? = null

        fun instance(): DataManager {
            if (m_instance == null) {
                synchronized(this) {
                    if (m_instance == null) {
                        m_instance = DataManager()
                    }
                }
            }
            return m_instance!!
        }
    }
    val tasks = arrayOf(
        ToDoTask("Laundry", "July 16, 2024","Clean bedsheets and clothing."),
        ToDoTask("Dishes", "", ""),
        ToDoTask("Homework", "July 17, 2024","Finish assignment 3!!")
    )
}