package com.girogevoro.androidonkotlin.view.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactsViewModel(
    val contactsLiveData: MutableLiveData<List<String>> = MutableLiveData()
) : ViewModel() {

    fun setListContacts(list: List<String>) {
        contactsLiveData.postValue(list)
    }

}