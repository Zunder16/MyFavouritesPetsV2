package com.example.myfavouritespetsv2.bd

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.myfavouritespetsv2.model.MyPet

class MyDBOpenHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    private val TAG = "SQLite"

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "mypets.db"
        const val TABLA_PETS = "pets"
        const val COLUMNA_ID = "_id"
        const val COLUMNA_NOMBRE = "nombre"
        const val COLUMNA_NOMBRECIENTIFICO = "nombre_cientifico"
        const val COLUMNA_PELAJE = "pelaje"
        const val COLUMNA_CLASE = "clase"
        const val COLUMNA_AMOROSIDAD = "amorosidad"
        const val COLUMNA_IMAGEN = "imagen"
        const val COLUMNA_FAVORITO = "favorito"
        const val COLUMNA_ENLACE = "enlace"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val crearTablaPets = "CREATE TABLE $TABLA_PETS " +
                    "($COLUMNA_ID TEXT PRIMARY KEY, " +
                    "$COLUMNA_NOMBRE TEXT, " +
                    "$COLUMNA_NOMBRECIENTIFICO TEXT, " +
                    "$COLUMNA_PELAJE TEXT, " +
                    "$COLUMNA_CLASE TEXT, " +
                    "$COLUMNA_AMOROSIDAD INTEGER, " +
                    "$COLUMNA_IMAGEN TEXT, " +
                    "$COLUMNA_FAVORITO INTEGER, " +
                    "$COLUMNA_ENLACE TEXT)"
            db!!.execSQL(crearTablaPets)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onCreate)", e.message.toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            val dropTablaAmigos = "DROP TABLE IF EXISTS $TABLA_PETS"
            db!!.execSQL(dropTablaAmigos)
            onCreate(db)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onUpgrade)", e.message.toString())
        }
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        Log.d("$TAG (onOpen)", "¡¡Base de datos abierta!!")
    }

    @SuppressLint("Range")
    fun getPet(idPet: String): MyPet? {
        val myPet = MyPet()
        val db = this.writableDatabase
        val selectQuery = "SELECT  * FROM $TABLA_PETS WHERE $COLUMNA_ID = ?"
        db.rawQuery(selectQuery, arrayOf(idPet)).use{
            if (it.moveToFirst()) {
            myPet.nombre = it.getString(it.getColumnIndex(COLUMNA_NOMBRE))
            myPet.nombreCientifico = it.getString(it.getColumnIndex(COLUMNA_NOMBRECIENTIFICO))
            myPet.pelaje = it.getString(it.getColumnIndex(COLUMNA_PELAJE))
            myPet.clase = it.getString(it.getColumnIndex(COLUMNA_CLASE))
            myPet.amorosidad = it.getInt(it.getColumnIndex(COLUMNA_AMOROSIDAD))
            myPet.rutaImagen = it.getString(it.getColumnIndex(COLUMNA_IMAGEN))
            myPet.favorito = it.getInt(it.getColumnIndex(COLUMNA_FAVORITO)) > 0
            myPet.enlace = it.getString(it.getColumnIndex(COLUMNA_ENLACE))
            return myPet
            }
        }
        return null
    }

    fun addPet(myPet: MyPet) {
        val data = ContentValues()
        data.put(COLUMNA_ID, myPet.id)
        data.put(COLUMNA_NOMBRE, myPet.nombre)
        data.put(COLUMNA_NOMBRECIENTIFICO, myPet.nombreCientifico)
        data.put(COLUMNA_PELAJE, myPet.pelaje)
        data.put(COLUMNA_CLASE, myPet.clase)
        data.put(COLUMNA_AMOROSIDAD, myPet.amorosidad)
        data.put(COLUMNA_IMAGEN, myPet.rutaImagen)
        data.put(COLUMNA_FAVORITO, myPet.favorito)
        data.put(COLUMNA_ENLACE, myPet.enlace)

        val db = this.writableDatabase
        db.insert(TABLA_PETS, null, data)
        db.close()
    }

    fun delPet(idPet: String): Int {
        val args = arrayOf(idPet)

        val db = this.writableDatabase
        val result = db.delete(TABLA_PETS, "$COLUMNA_ID = ?", args)

        db.close()
        return result
    }


    fun updatePet(idPet: String, myPet: MyPet) {
        val args = arrayOf(idPet)

        // Se crea un ArrayMap<>() con los datos nuevos.
        val data = ContentValues()
        data.put(COLUMNA_ID, myPet.id)
        data.put(COLUMNA_NOMBRE, myPet.nombre)
        data.put(COLUMNA_NOMBRECIENTIFICO, myPet.nombreCientifico)
        data.put(COLUMNA_PELAJE, myPet.pelaje)
        data.put(COLUMNA_CLASE, myPet.clase)
        data.put(COLUMNA_AMOROSIDAD, myPet.amorosidad)
        data.put(COLUMNA_IMAGEN, myPet.rutaImagen)
        data.put(COLUMNA_FAVORITO, myPet.favorito)
        data.put(COLUMNA_ENLACE, myPet.enlace)

        val db = this.writableDatabase
        db.update(TABLA_PETS, data, "$COLUMNA_ID = ?", args)
        db.close()
    }

    /*fun getAmigos():List<Amigo>{
        val result = ArrayList<Amigo>()

        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMNA_ID, $COLUMNA_NOMBRE, $COLUMNA_APELLIDOS " +
                " FROM $TABLA_AMIGOS" , null)
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val apellido = cursor.getString(2)
                val amigo = Amigo(id, nombre, apellido)
                result.add(amigo)
            }while (cursor.moveToNext())
        }
        return result
    }*/

    /*override fun getAmigosById(id:Int): Amigo?{
        var amigo : Amigo? = null
        val args = arrayOf(id.toString())

        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMNA_ID, $COLUMNA_NOMBRE, $COLUMNA_APELLIDOS " +
                " FROM $TABLA_AMIGOS WHERE $COLUMNA_ID = ?" , args)
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val apellido = cursor.getString(2)
                amigo = Amigo(id, nombre, apellido)

            }while (cursor.moveToNext())
        }
        return amigo
    }*/
}