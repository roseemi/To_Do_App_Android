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
*   August 3, 2024:
*       * Added CRUD functions for Firestore
*/

class ToDoTaskViewModel : ViewModel() {
    // Alias for the DataManager singleton
    private val dataManager = DataManager.instance()

    private val m_tasks = MutableLiveData<List<ToDoTask>>()
    val tasks: LiveData<List<ToDoTask>> get() = m_tasks

    private val m_task = MutableLiveData<ToDoTask?>()
    val tvShow: LiveData<ToDoTask?> get() = m_task

    fun loadAllToDoTasks() {
        viewModelScope.launch {
            // m_tasks.value = dataManager.tasks.toList()
            m_tasks.value = dataManager.getAllToDoTasks()
        }
    }

    fun loadToDoTaskById(id: String) {
        viewModelScope.launch {
            m_task.value = dataManager.getToDoTaskById(id)
        }
    }
    fun saveToDoTask(toDoTask: ToDoTask) {
        viewModelScope.launch {
            if (toDoTask.id.isEmpty()) {
                dataManager.insert(toDoTask)
            } else {
                dataManager.update(toDoTask)
            }
            loadAllToDoTasks()
        }
    }
    fun deleteToDoTask(toDoTask: ToDoTask) {
        viewModelScope.launch {
            dataManager.delete(toDoTask)
            loadAllToDoTasks()
        }
    }
}