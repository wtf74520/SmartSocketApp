package com.smartplugin.android

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.smartplugin.android.databinding.ActivityMainBinding
import com.smartplugin.android.ui.place.MqttFragment
import com.smartplugin.android.ui.place.ProfileFragment

class MainActivity : AppCompatActivity() {
     lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener  { item ->
            when(item.itemId) {
                R.id.menu_home -> {
                    // Respond to navigation item 1 click
                    val homeFragment = MqttFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                        .commit()
                    true
                }
                R.id.menu_profile -> {
                    // Respond to navigation item 2 click
                    val homeFragment = ProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId =  R.id.menu_home
    }
}