package com.example.myfavouritespetsv2.ui


import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.myfavouritespetsv2.R
import com.example.myfavouritespetsv2.bd.MyDBOpenHelper
import com.example.myfavouritespetsv2.databinding.ItemPetBinding
import com.example.myfavouritespetsv2.model.MyPet
import com.example.myfavouritespetsv2.utils.Constantes
import java.io.File


class PetAdapter() : RecyclerView.Adapter<PetAdapter.ViewHolder>() {
    var petList: MutableList<MyPet> = ArrayList()
    private lateinit var myDBOpenHelper: MyDBOpenHelper
    private lateinit var viewPager: ViewPager

    fun setViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
    }


    @SuppressLint("NotConstructor")
    fun PetAdapter(petList: MutableList<MyPet>, fragmentManager: FragmentManager) {
        this.petList = petList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemPetBinding.inflate(layoutInflater, parent, false).root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemMyPet = petList[position]

        val context = holder.itemView.context
        if (context is FragmentActivity) {
            val fragmentManager = context.supportFragmentManager
            holder.bind(itemMyPet, petList, fragmentManager)
        }


    }

    override fun getItemCount(): Int {
        return petList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemPetBinding.bind(view)

        fun bind(itemMyPet: MyPet, petList: MutableList<MyPet>, fragmentManager: FragmentManager) {

            with(binding) {
                tvNombre.text = itemMyPet.nombre
                tvNombreCientifico.text = itemMyPet.nombreCientifico
                tvClase.text = itemMyPet.clase
                tvPelaje.text = itemMyPet.pelaje
                tvAmor.text = itemMyPet.amorosidad.toString()

                Glide.with(itemView).load(itemMyPet.rutaImagen).into(ivImagen)

                if (itemMyPet.favorito) {
                    Glide.with(itemView).load(R.drawable.fav).into(ivFavorito)
                }

                tvEnlace.setOnClickListener(View.OnClickListener {
                    val intent = Intent(ACTION_VIEW)
                    intent.data = Uri.parse(itemMyPet.enlace)
                    itemView.context.startActivity(intent)
                })

                ivPapelera.setOnClickListener(View.OnClickListener {
                    dialogo(
                        petList,
                        position
                    )
                })

                ivFavorito.setOnClickListener(View.OnClickListener {
                    modificarFavorito(petList, itemMyPet, ivFavorito)
                })

                ivEditar.setOnClickListener(View.OnClickListener {
                    val listener = itemView.context as NavigationListener
                    listener.navigateToFragment(itemMyPet.id)
                })
            }
        }

        private fun modificarFavorito(
            petList: MutableList<MyPet>,
            itemMyPet: MyPet,
            ivFavorito: ImageView
        ) {
            val cDir = itemView.context.applicationContext.getExternalFilesDir(null)
            val file = File(cDir!!.path + "/" + Constantes.NOMBRE_ARCHIVO)
            if (file.exists()) {
                file.delete()
                file.createNewFile()
            }

            itemMyPet.favorito = !itemMyPet.favorito

            if (itemMyPet.favorito) {
                Glide.with(itemView).load(R.drawable.fav).into(ivFavorito)
            } else {
                Glide.with(itemView).load(R.drawable.favorito).into(ivFavorito)
            }

            //
            petList.forEachIndexed { index, myPet ->
                if (position == index)
                    file.appendText(itemMyPet.toString() + System.getProperty("line.separator"))
                else
                    file.appendText(myPet.toString() + System.getProperty("line.separator"))
            }
            //
        }

        private fun dialogo(petList: MutableList<MyPet>, position: Int) {
            val builder = AlertDialog.Builder(itemView.context)
            // Se crea el AlertDialog.
            builder.apply {
                // Se asigna un título.
                setTitle("Borrar pet!!")
                // Se asgina el cuerpo del mensaje.
                setMessage("¿Desea borrar la mascota?")
                // Se define el comportamiento de los botones.
                setPositiveButton(android.R.string.ok) { _, _ ->
                    val myDBOpenHelper: MyDBOpenHelper =
                        itemView.context.applicationContext?.let { MyDBOpenHelper(it, null) }!!
                    myDBOpenHelper.delPet(petList[position].id)
                    val intent = Intent(itemView.context, MainActivity::class.java)
                    itemView.context.startActivity(intent)
                }
                setNegativeButton(android.R.string.no) { _, _ ->
                    Toast.makeText(
                        itemView.context,
                        android.R.string.no,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // Se muestra el AlertDialog.
            builder.show()
        }

    }
}