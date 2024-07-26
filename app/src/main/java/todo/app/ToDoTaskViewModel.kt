package todo.app

import androidx.lifecycle.*
import kotlinx.coroutines.launch

/*
* ToDoTaskViewModel.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   June 26, 2024:
*       * Initialised project
*/

class ToDoTaskViewModel : ViewModel() {
    // Alias for the DataManager singleton
    private val dataManager = DataManager.instance()

    private val m_tasks = MutableLiveData<List<ToDoTask>>()
    val tasks: LiveData<List<ToDoTask>> get() = m_tasks

    fun loadAllTasks() {
        viewModelScope.launch {
            m_tasks.value = dataManager.tasks.toList()
        }
    }
}