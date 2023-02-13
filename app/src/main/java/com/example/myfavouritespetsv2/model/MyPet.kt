package com.example.myfavouritespetsv2.model

import java.io.Serializable

class MyPet : Serializable {

    constructor()
    var id: String = ""
    var nombre: String = ""
    var nombreCientifico: String = ""
    var pelaje: String = ""
    var clase: String = ""
    var amorosidad: Int = 0
    var rutaImagen: String = ""
    var favorito: Boolean = false
    var enlace: String = ""

    constructor(idPet: String, nombre: String?, nombreCientifico: String?, pelaje: String?, clase: String?, amorosidad: Int, rutaImagen: String?, b: Boolean, enlace: String?)

    override fun toString(): String {
        return "$id;$nombre;$nombreCientifico;$pelaje;$clase;$amorosidad;$rutaImagen;$favorito;$enlace"
    }
}