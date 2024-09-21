package com.example.studysaver.databases.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.studysaver.databases.entities.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE status = :status ORDER BY deadlineTimestamp ASC")
    fun getTaskByStatus(status: String): LiveData<List<Task>>

    @Query("SELECT COUNT(*) FROM task WHERE status = :status")
    fun getTaskCountByStatus(status: String): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Query("SELECT * FROM task WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: Long): Task?

    @Query(
        "UPDATE task " +
            "SET " +
            "title = :title, " +
            "description = :description, " +
            "deadline = :deadline, " +
            "deadlineTimestamp = :deadlineTimestamp " +
            "WHERE id = :taskId"
    )
    suspend fun updateTaskById(
        taskId: Long,
        title: String,
        description: String,
        deadline: String,
        deadlineTimestamp: Long
    )

    @Query("UPDATE task SET isReadyToDelete = :deleteStatus WHERE status = :taskStatus")
    suspend fun updateTaskReadyToDeleteStatus(deleteStatus: Boolean, taskStatus: String)

    @Query("UPDATE task SET isChecked = :isChecked WHERE status = :taskStatus")
    suspend fun updateTaskCheckedStatus(isChecked: Boolean, taskStatus: String)

    @Query("UPDATE task SET status = 'Done', dateFinished = :dateFinished WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Long, dateFinished: String)

    @Query("UPDATE task SET status = 'Late' WHERE deadlineTimestamp < :currentTime")
    suspend fun updateTaskStatusIfDeadlinePassed(currentTime: Long)

    @Query("DELETE FROM task WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)

    @Query("DELETE FROM task WHERE status = :status")
    suspend fun deleteTaskByStatus(status: String)
}