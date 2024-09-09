package com.example.studysaver.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainTaskViewModel : ViewModel() {
    val menuId = MutableLiveData<Int>()

    private val undoneButtonColor = MutableLiveData<Int>()
    private val undoneTextColor = MutableLiveData<Int>()
    val undoneButtonData = MediatorLiveData<Pair<Int, Int>>().apply {
        addSource(undoneButtonColor) { background ->
            val textColor = undoneTextColor.value ?: return@addSource
            value = background to textColor
        }
        addSource(undoneTextColor) { textColor ->
            val background = undoneButtonColor.value ?: return@addSource
            value = background to textColor
        }
    }

    private val lateButtonColor = MutableLiveData<Int>()
    private val lateTextColor = MutableLiveData<Int>()
    val lateButtonData = MediatorLiveData<Pair<Int, Int>>().apply {
        addSource(lateButtonColor) { background ->
            val textColor = lateTextColor.value ?: return@addSource
            value = background to textColor
        }
        addSource(lateTextColor) { textColor ->
            val background = lateButtonColor.value ?: return@addSource
            value = background to textColor
        }
    }

    private val doneButtonColor = MutableLiveData<Int>()
    private val doneTextColor = MutableLiveData<Int>()
    val doneButtonData = MediatorLiveData<Pair<Int, Int>>().apply {
        addSource(doneButtonColor) { background ->
            val textColor = doneTextColor.value ?: return@addSource
            value = background to textColor
        }
        addSource(doneTextColor) { textColor ->
            val background = doneButtonColor.value ?: return@addSource
            value = background to textColor
        }
    }

    init {
        menuId.value = 1
    }

    fun changeMenuButton(
        menuId: Int,
        undoneButtonColor: Int,
        undoneTextColor: Int,
        lateButtonColor: Int,
        lateTextColor: Int,
        doneButtonColor: Int,
        doneTextColor: Int,
    ) {
        this.menuId.value = menuId
        this.undoneButtonColor.value = undoneButtonColor
        this.undoneTextColor.value = undoneTextColor
        this.lateButtonColor.value = lateButtonColor
        this.lateTextColor.value = lateTextColor
        this.doneButtonColor.value = doneButtonColor
        this.doneTextColor.value = doneTextColor
    }
}