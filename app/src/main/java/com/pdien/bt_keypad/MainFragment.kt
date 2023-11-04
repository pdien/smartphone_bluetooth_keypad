package com.pdien.bt_keypad

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.pdien.bt_keypad.databinding.FragmentMainBinding
class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btmenu.setOnClickListener{ openBluetoothMenu() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun openBluetoothMenu() {
        val intent = Intent(activity, Bluetooth::class.java)
        startActivity(intent)
    }
}