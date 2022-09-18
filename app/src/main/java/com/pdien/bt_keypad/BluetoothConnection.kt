package com.pdien.bt_keypad

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat.getSystemService

@SuppressLint("MissingPermissions")
class BluetoothConnection {
    private lateinit var btStateRequestLauncher: ActivityResultLauncher<Intent>
    lateinit var btSocket: BluetoothSocket
    private val deviceList = ArrayList<BtItem>()
    private lateinit var customAdapter: CustomAdapter

//    private val bluetoothAdapter: BluetoothAdapter by lazy {
//        val bluetoothManager = getSystemService(BluetoothManager::class.java)
//        bluetoothManager.adapter
//    }

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Toast.makeText(applicationContext, "Bluetooth Scanning Started", Toast.LENGTH_SHORT).show()
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Toast.makeText(this@MainActivity, "Bluetooth Scanning Finished", Toast.LENGTH_SHORT).show()
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null) {
                        if (device.name != null) {
                            deviceList.add(BtItem(name = device.name, mac = device.address))
                            customAdapter.notifyItemInserted(deviceList.size + 1)
                        }
                    }
                }
            }
        }
    }
}