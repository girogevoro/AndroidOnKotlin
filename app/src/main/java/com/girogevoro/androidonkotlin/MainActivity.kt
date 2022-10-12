package com.girogevoro.androidonkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.girogevoro.androidonkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.button.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                Log.d("mytag","click")
            }
        })


    }
}