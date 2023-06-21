package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.room.Person

class DaoAdapter(private val listOfPeople:List<Person>):RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.person_row,parent,false)
        return MyViewHolder(row)
    }

    override fun getItemCount(): Int {
        return listOfPeople.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.imieTextView.text=listOfPeople[position].imie
        holder.nazwiskoTextView.text=listOfPeople[position].nazwisko
        holder.peselTextView.text=listOfPeople[position].pesel

    }
}
class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
    val imieTextView: TextView = view.findViewById(R.id.textView2)
    val nazwiskoTextView: TextView = view.findViewById(R.id.textView3)
    val peselTextView: TextView = view.findViewById(R.id.textView4)
}