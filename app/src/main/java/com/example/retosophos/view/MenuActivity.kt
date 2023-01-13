package com.example.retosophos.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.retosophos.LoginResponse
import com.example.retosophos.R
import com.example.retosophos.model.SharedData
import com.example.retosophos.viewModels.sophosApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val spinnerMenu : Spinner = findViewById(R.id.spinner_opciones)
        val listOptions = arrayListOf("","Enviar documentos","Ver documentos","Oficinas","Modo nocturno", "Idioma Ingles")
        val adapterList = ArrayAdapter<String>(this, R.layout.spinner_options, listOptions)
        spinnerMenu.adapter = adapterList
        spinnerMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val opcion = parent.getItemAtPosition(position) as String
                when (opcion) {
                    "Enviar documentos" -> {
                        startActivity( Intent(this@MenuActivity, SendDocumentsActivity::class.java))
                    }
                    "Ver documentos" -> {
                        val intent2 = Intent(this@MenuActivity, ViewDocumentsActivity::class.java)
                        val intent = intent
                        val correo = intent.getStringExtra("Correo").toString().trim()
                        intent2.putExtra("email",correo )
                        startActivity(intent2)
                    }
                    "Oficinas" -> {
                        startActivity( Intent(this@MenuActivity, OfficesActivity::class.java))
                    }
                    "Modo nocturno" -> {
                        // Acción para activar el modo nocturno
                    }
                    "Idioma Ingles" -> {
                        // Acción para cambiar el idioma a inglés
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se ha seleccionado ningún elemento
            }
        }

        user()

        val tvSentDocuments =findViewById<Button>(R.id.btn_sentDocuments)
        tvSentDocuments.setOnClickListener{
            val intent = Intent(this@MenuActivity, SendDocumentsActivity::class.java)
            startActivity(intent)
        }

        val tvViewDocuments =findViewById<Button>(R.id.btn_viewDocuments)
        tvViewDocuments.setOnClickListener{
            val intent = Intent(this@MenuActivity, ViewDocumentsActivity::class.java)
            startActivity(intent)
        }

        val tvOffices =findViewById<Button>(R.id.btn_oficces)
        tvOffices.setOnClickListener{
            val intent = Intent(this@MenuActivity, OfficesActivity::class.java)
            startActivity(intent)
        }
    }

        private fun user() {

            val correo = SharedData.sharedString.toString()
            val clave = SharedData.sharedPass.toString()
            val request = sophosApi.loginUser(correo,clave)
            request.enqueue(object : Callback<LoginResponse> {

                override fun onResponse(
                    call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val tvDatos2 = findViewById<TextView>(R.id.tv_toGoBack)
                    val userResponse2 = response.body()
                    userResponse2?.nombre?.let {
                        println(it)
                        it.forEach {
                            tvDatos2.append("${it}")
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    println(t.message)
                }
            })
        }
}