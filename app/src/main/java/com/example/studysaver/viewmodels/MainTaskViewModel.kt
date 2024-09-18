package com.example.studysaver.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.studysaver.R
import com.example.studysaver.adapters.TaskAdapter
import com.example.studysaver.utils.ConvertDate

class MainTaskViewModel(application: Application) : AndroidViewModel(application) {
    val menuId = MutableLiveData<Int>().apply { value = 1 }
    val undoneButtonStyle = MutableLiveData<Pair<Int, Int>>()
    val lateButtonStyle = MutableLiveData<Pair<Int, Int>>()
    val doneButtonStyle = MutableLiveData<Pair<Int, Int>>()
    val showDeleteTask = MutableLiveData<Boolean>()

    val taskTitle = MutableLiveData<String>()
    val taskDescription = MutableLiveData<String>()
    val taskDeadline = MutableLiveData<String>()

    private val undoneTasks = MutableLiveData(mutableListOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1))
    private val lateTasks = MutableLiveData(mutableListOf(1, 1))
    private val doneTasks = MutableLiveData(mutableListOf(1, 1, 1))

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

    fun getTaskList(menuId: Int): List<Int> {
        return when (menuId) {
            1 -> undoneTasks.value ?: emptyList()
            2 -> lateTasks.value ?: emptyList()
            3 -> doneTasks.value ?: emptyList()
            else -> emptyList()
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

    fun changeTaskList(status: Int) {
        when (menuId.value) {
            1 -> undoneTasks.value?.fill(status)
            2 -> lateTasks.value?.fill(status)
            3 -> doneTasks.value?.fill(status)
            else -> {}
        }
    }

    fun setTaskTitleAndDescription(title: String, description: String) {
        taskTitle.value = title
        taskDescription.value = description
    }

    fun setTaskDeadline(year: Int, month: Int, day: Int) {
        val context = getApplication<Application>()
        val selectedDate = context.getString(R.string.selected_date, year, month + 1, day)
        val indonesianDateFormat = context.getString(
            R.string.indonesian_date_format,
            ConvertDate.getDayFromDate(selectedDate),
            ConvertDate.convertDateToIndonesianFormat(selectedDate)
        )
        taskDeadline.value = indonesianDateFormat
    }

    fun onDeleteTaskClicked() {
        showDeleteTask.value = true
    }

    fun onCloseDeleteTaskClicked() {
        showDeleteTask.value = false
    }
}