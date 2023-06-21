package com.example.myapplication.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people_table")
data class Person (
            var imie:String,
            var nazwisko: String,
            var pesel:String) {
    @PrimaryKey(autoGenerate = true)
    var user_id:Int=0
}