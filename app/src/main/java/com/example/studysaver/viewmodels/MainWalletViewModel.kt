package com.example.studysaver.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studysaver.R

class MainWalletViewModel : ViewModel() {
    val menuId = MutableLiveData<Int>().apply { value = 1 }
    val allTransactionButtonStyle = MutableLiveData<Pair<Int, Int>>()
    val incomeButtonStyle = MutableLiveData<Pair<Int, Int>>()
    val expenseButtonStyle = MutableLiveData<Pair<Int, Int>>()

    fun changeMenuButton(menuId: Int) {
        this.menuId.value = menuId
        updateButtonStyles(menuId)
    }

    private fun updateButtonStyles(menuId: Int) {
        allTransactionButtonStyle.value = getButtonStyle(menuId, 1)
        incomeButtonStyle.value = getButtonStyle(menuId, 2)
        expenseButtonStyle.value = getButtonStyle(menuId, 3)
    }

    private fun getButtonStyle(menuId: Int, targetMenuId: Int): Pair<Int, Int> {
        return if (menuId == targetMenuId) {
            Pair(R.drawable.box_hover_blue, R.color.white)
        } else {
            Pair(0, R.color.sky_blue)
        }
    }
}