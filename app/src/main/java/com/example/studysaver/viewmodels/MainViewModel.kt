package com.example.studysaver.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studysaver.ui.fragments.MainHomeFragment

class MainViewModel : ViewModel() {
    val fragment = MutableLiveData<Fragment>().apply { value = MainHomeFragment() }

    fun changeFragment(fragment: Fragment) {
        this.fragment.value = fragment
    }
}