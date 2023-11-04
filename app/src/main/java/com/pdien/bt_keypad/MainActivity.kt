package com.pdien.bt_keypad

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.pdien.bt_keypad.databinding.ActivityMainBinding
import java.util.*

open class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfig = AppBarConfiguration(setOf(R.id.fragmentMain, R.id.fragmentInput, R.id.fragmentSettings))
        setupActionBarWithNavController(navController, appBarConfig)
//
        binding.bottomNav.setupWithNavController(navController)
    }

//    private fun onButtonClick() {
//        val intent = Intent(this, Bluetooth::class.java)
//        startActivity(intent)
//    }

}
