package com.girogevoro.androidonkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.girogevoro.androidonkotlin.databinding.ActivityMainBinding

private val TAG = "mytag"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val data = DataView("world")
        setContentView(binding.root)

        binding.button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d(TAG, "click")
                binding.textView1.text = "${data.name}  ${data.type}"
            }
        })

        Log.d(TAG, Repository.text)

        val value = object : Base(3, 5){}
        val copy = value.copy()
        Log.d(TAG,copy.toString())


        for(i in 1..8){
            Log.d(TAG, "$i")
        }

        for(i in 5 until 8 step  3){
            Log.d(TAG,"-------------------")
        }

        for(i in 3 downTo 1){
            Log.d(TAG, "+")
        }

        //Log.d(TAG, )
    }
}

 data class DataView(val name: String, val type: String = "Hello")

object Repository {
    val text = "new"
}

open class Base(var a: Int = 1, var b: Int = 2) {
    fun result(): Int {
        return a + b
    }

    fun copy(): Base {
        return Base(a, b)
    }

    override fun toString(): String {
        return "Base(a=$a, b=$b)"
    }

}