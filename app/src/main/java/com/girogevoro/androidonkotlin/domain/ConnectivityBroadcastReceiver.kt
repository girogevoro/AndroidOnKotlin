package com.girogevoro.androidonkotlin.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ConnectivityBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            StringBuilder().apply {
                append("Message ")
                append(intent.action)
            }.toString().let { str ->
                context?.let {
                    Toast.makeText(context, str, Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}