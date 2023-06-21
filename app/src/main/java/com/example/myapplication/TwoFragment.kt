package com.example.myapplication

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController

lateinit var text_label1: TextView

lateinit var button1: Button

class TwoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_two, container, false)
        button1 = view.findViewById(R.id.button)
        text_label1=view.findViewById(R.id.textView5)


        val sharedPreferences= requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val savedBoolean=sharedPreferences.getBoolean("BOOLEAN_KEY",false)

        if(!savedBoolean){
            text_label1.setText(R.string.turned_off_en)
            button1.setText(R.string.button_return_en)
        }
        else{
            text_label1.setText(R.string.turned_off_pl)
            button1.setText(R.string.button_return_pl)
        }

        button1.setOnClickListener(){
            val intent = Intent(second_activity(),MainActivity::class.java).also {
                startActivity(it)

            }
        }
        return view
    }

}