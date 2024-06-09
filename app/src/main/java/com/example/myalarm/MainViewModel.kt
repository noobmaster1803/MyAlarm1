package com.example.myalarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _alarmSet = MutableLiveData<Boolean>()
    val alarmSet: LiveData<Boolean> get() = _alarmSet

    init {
        _alarmSet.value = false
    }

    fun setAlarm(alarmSet: Boolean) {
        _alarmSet.value = alarmSet
    }
}
