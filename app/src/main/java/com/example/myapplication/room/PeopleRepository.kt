package com.example.myapplication.room

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PeopleRepository (application: Application) {
    private var peopleDao:PeopleDao

    init {
        val database:PeopleDatabase?=PeopleDatabase.getInstance(application.applicationContext)
        peopleDao=database!!.peopleDao()
    }
    fun insertPerson(person: Person)= CoroutineScope(Dispatchers.IO).launch {
        peopleDao.insert(person)
    }
    fun updatePerson(person: Person)= CoroutineScope(Dispatchers.IO).launch {
        peopleDao.update(person)
    }
    fun deletePerson(person: Person)= CoroutineScope(Dispatchers.IO).launch {
        peopleDao.delete(person)
    }
    fun getAllPeopleAsync(): Deferred<LiveData<List<Person>>> =
        CoroutineScope(Dispatchers.IO).async{
            peopleDao.getAllPeople()
    }
    fun deleteAllRows() =
        CoroutineScope(Dispatchers.IO).launch {
            peopleDao.deleteAllRows()
        }
}
