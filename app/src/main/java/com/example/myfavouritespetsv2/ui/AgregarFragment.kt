package com.example.myfavouritespetsv2.ui

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.myfavouritespetsv2.R
import com.example.myfavouritespetsv2.bd.MyDBOpenHelper
import com.example.myfavouritespetsv2.databinding.FragmentAgregarBinding
import com.example.myfavouritespetsv2.model.MyPet
import com.example.myfavouritespetsv2.utils.Constantes
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AgregarFragment : Fragment() {

    lateinit var binding: FragmentAgregarBinding
    var thumbnail: Uri? = null
    private var myPet = MyPet()
    private var mynewPet = MyPet()
    private lateinit var myDBOpenHelper: MyDBOpenHelper
    private var modificable: Boolean = false

    fun getData(data: MyPet, boolean: Boolean) {
        myPet = data
        modificable = boolean
        if (modificable) {
            myDBOpenHelper = activity?.applicationContext?.let { MyDBOpenHelper(it, null) }!!
            metodoModificar()
        }
        cambiarLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAgregarBinding.inflate(inflater, container, false)
        setSpinners()
        cambiarLayout()
        binding.ivSeleccionarFoto.setOnClickListener(View.OnClickListener { seleccionarImagen() })
        return binding.root
    }


    private fun metodoModificar() {
        with(binding) {
            spAmor.setSelection(myPet.amorosidad - 1)
            var posicionClase = 0
            var posicionPelaje = 0
            when (myPet.clase) {
                "Ave" -> posicionClase = 0
                "Mamífero" -> posicionClase = 1
                "Anfibio" -> posicionClase = 2
                "Réptil" -> posicionClase = 3
            }
            when (myPet.pelaje) {
                "Corto" -> posicionPelaje = 0
                "Largo" -> posicionPelaje = 1
                "Rizado" -> posicionPelaje = 2
                "Lanudo" -> posicionPelaje = 3
            }
            spClase.setSelection(posicionClase)
            spPelaje.setSelection(posicionPelaje)
            //
            edNombre.setText(myPet.nombre)
            edNombreCientifico.setText(myPet.nombreCientifico)
            edEnlace.setText(myPet.enlace)
            activity?.let {
                Glide.with(it.applicationContext).load(myPet.rutaImagen).into(ivSeleccionarFoto)
            }
            btnAgregar.text = Constantes.MODIFICAR
            tvTitulo.text = Constantes.MODIFICAR_MASCOTA

        }
    }

    private fun seleccionarImagen() {
        ImagePicker.with(this)
            .crop() //Crop image(Optional), Check Customization for more option
            .compress(1024) //Final image size will be less than 1 MB(Optional)
            .saveDir(activity?.getExternalFilesDir(null)!!)
            .maxResultSize(
                1080,
                1080
            ) //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === AppCompatActivity.RESULT_OK) {
            if (data != null) {
                thumbnail = data.data
                binding.ivSeleccionarFoto.setImageURI(thumbnail)
            }

        }
    }

    private fun setSpinners() {
        //Funcion para poner datos a los spinners
        val spAmorosidad: Spinner = binding.spAmor

        activity?.let {
            ArrayAdapter.createFromResource(
                it.applicationContext,
                R.array.desplegable_amorosidad,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spAmorosidad.adapter = adapter
            }
        }

        val spClase: Spinner = binding.spClase

        activity?.let {
            ArrayAdapter.createFromResource(
                it.applicationContext,
                R.array.desplegable_clase,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spClase.adapter = adapter
            }
        }

        val spPelaje: Spinner = binding.spPelaje

        activity?.let {
            ArrayAdapter.createFromResource(
                it.applicationContext,
                R.array.desplegable_pelaje,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spPelaje.adapter = adapter
            }
        }
    }


    private fun cambiarLayout() {
        myDBOpenHelper = activity?.applicationContext?.let { MyDBOpenHelper(it, null) }!!
        val db: SQLiteDatabase = myDBOpenHelper.readableDatabase

        binding.btnAgregar.setOnClickListener {
            if (binding.edNombre.text.isEmpty()
                || binding.edNombreCientifico.text.isEmpty()
                || binding.edEnlace.text.isEmpty()
            ) {
                Snackbar.make(
                    it, "Rellena todos los campos",
                    Snackbar.LENGTH_LONG
                ).show()

            } else {
                with(binding) {
                    mynewPet = MyPet()
                    mynewPet.id = UUID.randomUUID().toString()
                    mynewPet.nombre = edNombre.text.toString()
                    mynewPet.nombreCientifico = edNombreCientifico.text.toString()
                    mynewPet.clase = spClase.selectedItem.toString()
                    mynewPet.pelaje = spPelaje.selectedItem.toString()
                    mynewPet.amorosidad = spAmor.selectedItem.toString().toInt()
                    mynewPet.enlace = edEnlace.text.toString()
                    mynewPet.rutaImagen = Constantes.IMAGEN_DEFECTO

                    binding.edNombre.setText("")
                    binding.edNombreCientifico.setText("")
                    binding.edEnlace.setText("")

                    if (modificable) {
                        mynewPet.id = myPet.id
                        mynewPet.rutaImagen = myPet.rutaImagen
                        if (myPet.favorito) {
                            mynewPet.favorito = true
                        }
                    }
                    if (thumbnail != null) {
                        mynewPet.rutaImagen = thumbnail.toString()
                    }
                }

                if (modificable) {
                    myDBOpenHelper.updatePet(myPet.id, mynewPet)
                } else {
                    myDBOpenHelper.addPet(mynewPet)
                }
                modificable = false
                reset()
                val viewPager = activity?.findViewById<ViewPager>(R.id.viewPager)
                val fragment = (viewPager?.adapter as ViewPagerAdapter).getItem(0) as ListaFragment
                fragment.refresh()
                viewPager.setCurrentItem(0, true)
                db.close()
            }
        }
    }

    private fun reset() {
        binding.edNombre.setText("")
        binding.edNombreCientifico.setText("")
        binding.edEnlace.setText("")
        binding.tvTitulo.text = Constantes.ANYADIR_MASCOTA
        binding.btnAgregar.text = Constantes.ANYADIR
        setSpinners()
        modificable = false
        binding.ivSeleccionarFoto.setImageResource(R.drawable.ic_baseline_photo_camera_24)
        myPet = MyPet()
    }

}