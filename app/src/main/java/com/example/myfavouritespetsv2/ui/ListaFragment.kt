package com.example.myfavouritespetsv2.ui

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.myfavouritespetsv2.R
import com.example.myfavouritespetsv2.bd.MyDBOpenHelper
import com.example.myfavouritespetsv2.databinding.FragmentListaBinding
import com.example.myfavouritespetsv2.model.MyPet

class ListaFragment(
    private val viewPager: ViewPager,
    private val agregarFragment: AgregarFragment
) : Fragment() {

    private var datos = (mutableListOf<MyPet>())
    lateinit var binding: FragmentListaBinding
    private lateinit var adapter: PetAdapter
    private lateinit var myDBOpenHelper: MyDBOpenHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaBinding.inflate(inflater, container, false)
        setListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        adapter = PetAdapter() { myPet ->
            viewPager.setCurrentItem(1, true)
            agregarFragment.getData(myPet, true)
        }
        datos = (mutableListOf<MyPet>())
        conexionBBDD()
        setAdapter(datos)
    }

    fun refresh() {
        datos = (mutableListOf<MyPet>())
        conexionBBDD()
        setAdapter(datos)
    }

    fun setListener() {
        setSpinners()
        filtrar()
    }

    private fun conexionBBDD() {
        //Funcion para abrir el archivo de texto y recoger los datos de normales y favoritos
        myDBOpenHelper = activity?.applicationContext?.let { MyDBOpenHelper(it, null) }!!
        val db: SQLiteDatabase = myDBOpenHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "" +
                    "SELECT * FROM ${MyDBOpenHelper.TABLA_PETS};", null
        )
        if (cursor.moveToFirst()) {

            do {
                val myPet = MyPet()
                myPet.id = cursor.getString(0)
                myPet.nombre = cursor.getString(1)
                myPet.nombreCientifico = cursor.getString(2)
                myPet.pelaje = cursor.getString(3)
                myPet.clase = cursor.getString(4)
                myPet.amorosidad = cursor.getInt(5)
                myPet.rutaImagen = cursor.getString(6)
                myPet.favorito = cursor.getInt(7) > 0
                myPet.enlace = cursor.getString(8)
                datos.add(myPet)
            } while (cursor.moveToNext())


        } else {
            // Mensaje de que no hay nada
        }
        db.close()
    }

    private fun setAdapter(datos: MutableList<MyPet>) {
        binding.rvPet.setHasFixedSize(true)
        binding.rvPet.layoutManager = LinearLayoutManager(activity?.applicationContext)
        activity?.let { adapter.PetAdapter(datos, it.supportFragmentManager) }
        adapter.setViewPager(viewPager)
        binding.rvPet.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    private fun setSpinners() { //Funcion para poner datos a los spinners
        val spOrdenar: Spinner = binding.spOrdenar

        activity?.applicationContext?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.desplegable_ordenar,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spOrdenar.adapter = adapter
            }
        }
    }

    private fun filtrar() { //Funcion para los filtros
        binding.spOrdenar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> aplicarFiltro()
                    1 -> filtroAmorosidad()
                    2 -> filtroFavoritas()
                    3 -> filtroAlfabeticamente()
                }
            }
        }
    }

    private fun filtroAlfabeticamente() { //Filtro para ordenar Alfabeticamente de la A-Z
        datos.sortWith(Comparator { o1: MyPet, o2: MyPet ->
            o1.nombre.lowercase().compareTo(o2.nombre.lowercase())
        })
        aplicarFiltro()
    }

    private fun filtroFavoritas() { //Filtro para mostrar solo la lista de Favs
        val listaFavs: MutableList<MyPet> =
            datos.filter { myPet -> myPet.favorito } as MutableList<MyPet>
        activity?.let { adapter.PetAdapter(listaFavs, it.supportFragmentManager) }
        binding.rvPet.adapter = adapter
    }

    private fun filtroAmorosidad() { //Filtro para ordenar por Amorosidad
        datos.sortWith(Comparator { o1: MyPet, o2: MyPet ->
            o2.amorosidad.compareTo(o1.amorosidad)
        })
        aplicarFiltro()
    }

    private fun aplicarFiltro() {
        activity?.let { adapter.PetAdapter(datos, it.supportFragmentManager) }
        binding.rvPet.adapter = adapter
    }
}