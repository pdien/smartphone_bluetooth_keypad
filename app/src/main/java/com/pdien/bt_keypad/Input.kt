package com.pdien.bt_keypad

import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Input : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btDevice: BluetoothDevice? = intent.extras?.getParcelable("btdevice")
        //private val btSocket: BluetoothSocket =
    }

    private fun write() {

    }
}