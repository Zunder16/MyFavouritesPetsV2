package com.example.myfavouritespetsv2.model

import java.io.Serializable

class MyPet : Serializable {
    var id: String = ""
    var nombre: String = ""
    var nombreCientifico: String = ""
    var pelaje: String = ""
    var clase: String = ""
    var amorosidad: Int = 0
    var rutaImagen: String = ""
    var favorito: Boolean = false
    var enlace: String = ""
}