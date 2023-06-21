package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Dao
import com.example.myapplication.adapters.DaoAdapter
import com.example.myapplication.room.PeopleDatabase
import com.example.myapplication.room.Person
import com.example.myapplication.viewmodels.PeopleViewModel

class activity_2 : AppCompatActivity() {

    private lateinit var viewModel:PeopleViewModel
    private lateinit var daoAdapter: DaoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var listOfPeople: LiveData<List<Person>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(PeopleViewModel::class.java)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        listOfPeople = viewModel.getAllPeople()
        listOfPeople.observe(this, Observer {
            if (it.isNotEmpty()){
                daoAdapter= DaoAdapter(it)
                recyclerView.adapter=daoAdapter
            }
        })

    }
}