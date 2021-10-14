package com.stevehechio.apps.networkconectivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.stevehechio.apps.nectworkconnectivity.NetworkStatus
import com.stevehechio.apps.nectworkconnectivity.NetworkStatusHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetworkStatusHelper(this).observe(this,{
            Log.e("network status", "network status:    $it")
            findViewById<TextView>(R.id.tv).text =
                if (it == NetworkStatus.Available) "Network connectivity available" else "No Internet"
        })

    }
}