package com.example.encryptedimagesharingapp

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {

    private val _dataList = MutableLiveData<ArrayList<Uri>>()
    val dataList = _dataList

    fun addFileToList(item: Uri) {
        val value = _dataList.value
        value?.add(item)
        _dataList.value = value
    }

    fun removeFileToList(item: Uri) {
        val value = _dataList.value
        value?.remove(item)
        _dataList.value = value
    }


}