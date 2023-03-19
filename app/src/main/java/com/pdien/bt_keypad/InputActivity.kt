package com.pdien.bt_keypad

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.io.IOException
import java.io.InputStream

class InputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        val btDevice: BluetoothDevice? = intent.extras?.getParcelable("btdevice")
        //private val btSocket: BluetoothSocket =
    }

    private fun write() {

    }
}