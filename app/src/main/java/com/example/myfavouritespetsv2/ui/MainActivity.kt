package com.example.myfavouritespetsv2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myfavouritespetsv2.R
import com.example.myfavouritespetsv2.databinding.ActivityMainBinding
import com.example.myfavouritespetsv2.ui.ListaFragment.Companion.newInstance

class MainActivity : AppCompatActivity(), NavigationListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Se carga la toolbar.
        setSupportActionBar(binding.toolbar)
        // Se crea el adapter.
        val adapter = ViewPagerAdapter(supportFragmentManager)

        // Recogemos los datos aqui el fragmento al que quereremos ir y el idPet
        // Despues empezamos en el fragmento de agregar.


        // Se añaden los fragments y los títulos de pestañas.
        adapter.addFragment(ListaFragment(), "Listado")
        adapter.addFragment(AgregarFragment(), "Agregar")

        // Se asocia el adapter.
        binding.viewPager.adapter = adapter
        // Se cargan las tabs.
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    override fun navigateToFragment(data: Any) {
        val fragment = AgregarFragment.newInstance(data as String)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.viewPager, fragment)
            .addToBackStack(null)
            .commit()
    }
}