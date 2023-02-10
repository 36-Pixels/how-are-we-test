package com.example.testapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.model.InputData
import com.example.testapp.storage.SharedPrefs

class ActivityMainViewModel(application: Application) :
    AndroidViewModel(application) {

    private val storage = SharedPrefs.getInstance(application)

    private val _values: MutableLiveData<List<InputData>> = MutableLiveData(listOf())
    val values: LiveData<List<InputData>> = _values

    init {
        _values.value = storage.getValues()
    }

    fun persistData () {
        storage.saveValues(_values.value!!)
    }

    fun onAddValue(number: Int) {
        val oldList = _values.value!!.toMutableList()
        oldList.add(InputData(number))
        val sorted = oldList.sortedByDescending {
                it.ts
            }

        _values.value = sorted.toList()

        //println("onAddValue: $number, values = ${_values.value}")
    }
}