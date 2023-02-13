package com.example.myfavouritespetsv2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myfavouritespetsv2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Se carga la toolbar.
        setSupportActionBar(binding.toolbar)
        // Se crea el adapter.
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val agregarFragment = AgregarFragment()
        // Se añaden los fragments y los títulos de pestañas.
        adapter.addFragment(ListaFragment(binding.viewPager, agregarFragment), "Listado")
        adapter.addFragment(agregarFragment, "Agregar")

        // Se asocia el adapter.
        binding.viewPager.adapter = adapter
        // Se cargan las tabs.
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

}