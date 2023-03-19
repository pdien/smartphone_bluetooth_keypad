package com.pdien.bt_keypad

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pdien.bt_keypad.CustomAdapter.OnItemClickListener
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

const val BLUETOOTH_PERMISSION_REQUEST_CODE = 9999
val myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")

@SuppressLint("MissingPermission")
open class MainActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var btStateRequestLauncher: ActivityResultLauncher<Intent>
    private lateinit var btSocket: BluetoothSocket
    private val deviceList = ArrayList<BtItem>()
    private lateinit var customAdapter: CustomAdapter

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothManager.adapter
    }

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Toast.makeText(this@MainActivity, "Bluetooth Scanning Started", Toast.LENGTH_SHORT).show()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Bluetooth Keypad"

        btStateRequestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_CANCELED) {
                promptEnableBluetooth(btStateRequestLauncher)
            }
        }
        initializeBluetoothOrRequestPermission()

        val floatingActionButton: FloatingActionButton = findViewById(R.id.searchFab)
        floatingActionButton.setOnClickListener{ startBluetoothScan() }

        val recyclerView: RecyclerView = findViewById(R.id.recView)
        customAdapter = CustomAdapter(deviceList, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter
        getPairedDevices()

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {R.layout.activity_input}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothAdapter.cancelDiscovery()
        unregisterReceiver(receiver)
    }

    override fun onResume() {
        super.onResume()
        if (!bluetoothAdapter.isEnabled) {
            promptEnableBluetooth(btStateRequestLauncher)
        }

        if (this::btSocket.isInitialized) {
           if (btSocket.isConnected) {
                btSocket.close()
                Toast.makeText(this, "Bluetooth Socket Closed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(position: Int) {
        val device = deviceList[position]
        connectDevice(device.mac)
    }

    private fun promptEnableBluetooth(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        resultLauncher.launch(intent)
    }

    private fun getPairedDevices() {
        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
        pairedDevices.forEach { device ->
            deviceList.add(BtItem(name = device.name, mac = device.address))
        }
        customAdapter.notifyItemInserted(deviceList.size + 1)
    }

    private fun startBluetoothScan() {
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
        deviceList.clear()
        customAdapter.notifyDataSetChanged()

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(receiver, filter)?.setPackage("com.pdien.bt_keypad")
        bluetoothAdapter.startDiscovery()
    }

    private fun connectDevice(mac: String) {
        // unregisterReceiver(receiver)
        val mDevice = bluetoothAdapter.getRemoteDevice(mac)
        bluetoothAdapter.cancelDiscovery()
        try {
            btSocket = mDevice.createRfcommSocketToServiceRecord(myUUID)
            btSocket.connect()
            Toast.makeText(this@MainActivity, "Connection Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra("btdevice", mDevice)
            startActivity(intent)
        } catch (e2: IOException) {
            try {
                btSocket.close()
            } catch (e2: IOException) {
                Toast.makeText(this@MainActivity, "Unable to connect to device", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this@MainActivity, "Socket creation failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeBluetoothOrRequestPermission() {
        val requiredPermissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            listOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
        }

        val missingPermissions = requiredPermissions.filter { permission ->
            checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isEmpty()) {
            if (!bluetoothAdapter.isEnabled) {
                promptEnableBluetooth(btStateRequestLauncher)
            }
        } else {
            requestPermissions(missingPermissions.toTypedArray(), BLUETOOTH_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            BLUETOOTH_PERMISSION_REQUEST_CODE -> {
                if (grantResults.none { it != PackageManager.PERMISSION_GRANTED }) {
                    if (!bluetoothAdapter.isEnabled) {
                        promptEnableBluetooth(btStateRequestLauncher)
                    }
                } else {
                    Toast.makeText(this, "Permissions are needed for this app to function", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
