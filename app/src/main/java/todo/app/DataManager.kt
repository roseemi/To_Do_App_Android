package todo.app

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/*
* DataManager.kt
* Author: Emily Rose
* ID: 200553504
* Description: This is a to-do application using Firebase as the database. Users can input their
* tasks and track what needs to be done. Users can click on tasks to see/edit details of said task.
*
* Version history:
*   July 26, 2024:
*       * Initialised project
*   August 3, 2024:
*       * Added CRUD functions for Firestore
*/

class DataManager private constructor() {

    private val db: FirebaseFirestore = Firebase.firestore

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

    suspend fun insert(toDoTask: ToDoTask) {
        try {
            db.collection("ToDoTasks").document(toDoTask.id).set(toDoTask).await()
        }
        catch (e: Exception) {
            Log.e(TAG, "Error inserting ToDoTask: ${e.message}", e)
        }
    }
    suspend fun update(toDoTask: ToDoTask) {
        try {
            db.collection("ToDoTasks").document(toDoTask.id).set(toDoTask).await()
        }
        catch (e: Exception) {
            Log.e(TAG, "Error updating ToDoTask: ${e.message}", e)
        }
    }
    suspend fun delete(toDoTask: ToDoTask) {
        try {
            db.collection("ToDoTasks").document(toDoTask.id).delete().await()
        }
        catch (e: Exception) {
            Log.e(TAG, "Error deleting ToDoTask: ${e.message}", e)
        }
    }
    suspend fun getAllToDoTasks(): List<ToDoTask> {
        return try {
            val result = db.collection("ToDoTasks").get().await()
            result?.toObjects(ToDoTask::class.java) ?: emptyList()
        }
        catch (e: Exception) {
            Log.e(TAG, "Error getting all ToDoTasks: ${e.message}", e)
            emptyList()
        }
    }
    suspend fun getToDoTaskById(id: String): ToDoTask? {
        return try {
            val result = db.collection("ToDoTasks").document(id).get().await()
            result?.toObject(ToDoTask::class.java)
        }
        catch (e: Exception) {
            Log.e(TAG, "Error getting ToDoTask by ID: ${e.message}", e)
            null
        }
    }
}