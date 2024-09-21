package com.example.studysaver.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.studysaver.R
import com.example.studysaver.adapters.TaskAdapter
import com.example.studysaver.databases.AppDatabase
import com.example.studysaver.databases.entities.Task
import com.example.studysaver.databases.repositories.TaskRepository
import com.example.studysaver.utils.DateTimeUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository: TaskRepository

    private val _tasks = MediatorLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks
    private var currentSource: LiveData<List<Task>>

    val menuId = MutableLiveData<Int>().apply { value = 1 }
    val undoneButtonStyle = MutableLiveData<Pair<Int, Int>>()
    val lateButtonStyle = MutableLiveData<Pair<Int, Int>>()
    val doneButtonStyle = MutableLiveData<Pair<Int, Int>>()
    val showDeleteTask = MutableLiveData<Boolean>()

    val taskTitleDialog = MutableLiveData<String>()
    val taskDescriptionDialog = MutableLiveData<String>()
    val taskDeadlineDialog = MutableLiveData<String>()
    val taskDeadline = MutableLiveData<Long>()

    init {
        val taskDao = AppDatabase.getDatabase(application).getTaskDao()
        taskRepository = TaskRepository(taskDao)
        currentSource = taskRepository.getTaskByStatus("Undone")

        _tasks.addSource(currentSource) {
            _tasks.value = it
        }
    }

    val undoneTaskCount: LiveData<Int> = taskRepository.getTaskCountByStatus("Undone")
    val lateTaskCount: LiveData<Int> = taskRepository.getTaskCountByStatus("Late")
    val doneTaskCount: LiveData<Int> = taskRepository.getTaskCountByStatus("Done")

    fun changeMenuButton(menuId: Int) {
        this.menuId.value = menuId
        updateButtonStyles(menuId)
    }

    private fun updateButtonStyles(menuId: Int) {
        undoneButtonStyle.value = getButtonStyle(menuId, 1)
        lateButtonStyle.value = getButtonStyle(menuId, 2)
        doneButtonStyle.value = getButtonStyle(menuId, 3)
    }

    private fun getButtonStyle(menuId: Int, targetMenuId: Int): Pair<Int, Int> {
        return if (menuId == targetMenuId) {
            Pair(R.drawable.box_hover_blue, R.color.white)
        } else {
            Pair(0, R.color.sky_blue)
        }
    }

    fun getViewType(menuId: Int): Int {
        return when (menuId) {
            1 -> TaskAdapter.VIEW_TYPE_ONE
            2 -> TaskAdapter.VIEW_TYPE_TWO
            3 -> TaskAdapter.VIEW_TYPE_THREE
            else -> TaskAdapter.VIEW_TYPE_ONE
        }
    }

    fun setTaskTitleAndDescription(title: String, description: String) {
        taskTitleDialog.value = title
        taskDescriptionDialog.value = description
    }

    fun setTaskDeadline(year: Int, month: Int, day: Int) {
        val selectedDate = "$year-${month + 1}-$day"
        val deadlineTimestamp = DateTimeUtil.convertToTimestamp(selectedDate)
        taskDeadlineDialog.value = DateTimeUtil.getIndonesianDateFormat(selectedDate)
        taskDeadline.value = deadlineTimestamp
    }

    fun onDeleteTaskClicked() {
        showDeleteTask.value = true
    }

    fun onCloseDeleteTaskClicked() {
        showDeleteTask.value = false
    }

    fun switchTaskSource(menuId: Int) {
        _tasks.removeSource(currentSource)

        when (menuId) {
            1 -> currentSource = taskRepository.getTaskByStatus("Undone")
            2 -> currentSource = taskRepository.getTaskByStatus("Late")
            3 -> currentSource = taskRepository.getTaskByStatus("Done")
        }

        _tasks.addSource(currentSource) {
            _tasks.value = it
        }
    }

    fun insert(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.insert(task)
    }

    fun getTaskById(taskId: Long): LiveData<Task?> {
        val taskLiveData = MutableLiveData<Task?>()
        viewModelScope.launch(Dispatchers.IO) {
            val task = taskRepository.getTaskById(taskId)
            taskLiveData.postValue(task) // Update LiveData
        }
        return taskLiveData
    }

    fun updateTask(
        taskId: Long,
        title: String,
        description: String,
        deadline: String,
        deadlineTimestamp: Long
    ) = viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTaskById(taskId, title, description, deadline, deadlineTimestamp)
        }


    fun updateReadyToDeleteStatus(deleteStatus: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        when (menuId.value) {
            1 -> taskRepository.updateTaskReadyToDeleteStatus(deleteStatus, "Undone")
            2 -> taskRepository.updateTaskReadyToDeleteStatus(deleteStatus, "Late")
            3 -> taskRepository.updateTaskReadyToDeleteStatus(deleteStatus, "Done")
        }
    }

    fun updateCheckedStatus(isChecked: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        when (menuId.value) {
            1 -> taskRepository.updateTaskCheckedStatus(isChecked, "Undone")
            2 -> taskRepository.updateTaskCheckedStatus(isChecked, "Late")
            3 -> taskRepository.updateTaskCheckedStatus(isChecked, "Done")
        }
    }

    fun updateTaskStatus(taskId: Long, dateFinished: String) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.updateTaskStatus(taskId, dateFinished)
    }

    fun checkAndUpdateTaskStatus() = viewModelScope.launch(Dispatchers.IO) {
        val currentTime = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTaskStatusIfDeadlinePassed(currentTime)
        }
    }

    fun deleteTaskById(listTaskId: List<Long>) = viewModelScope.launch(Dispatchers.IO) {
        val taskIdsCopy = listTaskId.toList()
        val iterator = taskIdsCopy.iterator()
        while (iterator.hasNext()) {
            taskRepository.deleteTaskById(iterator.next())
        }
    }

    fun deleteTaskByStatus() = viewModelScope.launch(Dispatchers.IO) {
        when (menuId.value) {
            1 -> taskRepository.deleteTaskByStatus("Undone")
            2 -> taskRepository.deleteTaskByStatus("Late")
            3 -> taskRepository.deleteTaskByStatus("Done")
        }
    }
}