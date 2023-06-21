package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.myapplication.room.PeopleRepository
import com.example.myapplication.room.Person
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking

class PeopleViewModel (application: Application):
    AndroidViewModel(application) {

        private var peopleRepository:PeopleRepository= PeopleRepository(application)
        private var allPeople:Deferred<LiveData<List<Person>>> =
            peopleRepository.getAllPeopleAsync()

    fun insertPerson(person:Person){
        peopleRepository.insertPerson(person)
    }
    fun updatePerson(person:Person){
        peopleRepository.updatePerson(person)
    }
    fun deletePerson(person:Person){
        peopleRepository.deletePerson(person)
    }
    fun getAllPeople() : LiveData<List<Person>> = runBlocking {
        allPeople.await()
    }
    fun deleteAllRows(){
        peopleRepository.deleteAllRows()
    }
}