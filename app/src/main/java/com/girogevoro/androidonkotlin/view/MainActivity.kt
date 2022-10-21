package com.girogevoro.androidonkotlin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.girogevoro.androidonkotlin.R
import com.girogevoro.androidonkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()
    }
}

