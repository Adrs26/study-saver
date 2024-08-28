package com.example.studysaver.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(private val defaultFragment: Fragment) : ViewModel() {
    private val _fragment = MutableLiveData<Fragment>().apply {
        value = defaultFragment
    }
    val fragment: LiveData<Fragment>
        get() = _fragment

    fun changeFragment(fragment: Fragment) {
        _fragment.value = fragment
    }
}