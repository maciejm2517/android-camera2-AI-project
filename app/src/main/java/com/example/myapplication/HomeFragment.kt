package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController

lateinit var text_label: TextView
lateinit var edit_text: EditText
lateinit var button: Button

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home, container, false)
        button = view.findViewById(R.id.button1)
        text_label=view.findViewById(R.id.textView6)
        edit_text=view.findViewById(R.id.edit1)
        val sharedPreferences= requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val savedBoolean=sharedPreferences.getBoolean("BOOLEAN_KEY",false)

        if(!savedBoolean){
            text_label.setText(R.string.homefragment_en)
            edit_text.setHint(R.string.turn_off_en)
            button.setText(R.string.button_accept_en)
        }
        else{
            text_label.setText(R.string.homefragment_pl)
            edit_text.setHint(R.string.turn_off_pl)
            button.setText(R.string.button_accept_pl)
        }

        button.setOnClickListener(){
            val text = view.findViewById<EditText>(R.id.edit1)
            if (text.getText().toString() == "tak" || text.getText().toString() == "yes" ) {
                findNavController().navigate(R.id.action_homeFragment_to_twoFragment)
            }
        }
        return view
    }

}