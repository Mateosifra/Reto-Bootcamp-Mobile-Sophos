package com.example.retosophos.view

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retosophos.*
import com.example.retosophos.model.ImageResponse
import com.example.retosophos.model.SharedData
import com.example.retosophos.viewModels.retrofit
import com.example.retosophos.viewModels.sophosApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewDocumentsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_documents)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewDocuments)
        val spinnerMenu: Spinner = findViewById(R.id.spinner_opciones)
        val listOptions = arrayListOf(
            "",
            "Enviar documentos",
            "Ver documentos",
            "Oficinas",
            "Modo nocturno",
            "Idioma Ingles"
        )
        val adapterList = ArrayAdapter<String>(this, R.layout.spinner_options, listOptions)
        spinnerMenu.adapter = adapterList

        spinnerMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val opcion = parent.getItemAtPosition(position) as String
                when (opcion) {
                    "Enviar documentos" -> {
                        startActivity(Intent(this@ViewDocumentsActivity, SendDocumentsActivity::class.java))
                    }
                    "Ver documentos" -> {
                        startActivity(Intent(this@ViewDocumentsActivity, ViewDocumentsActivity::class.java))
                    }
                    "Oficinas" -> {
                        startActivity(Intent(this@ViewDocumentsActivity, OfficesActivity::class.java))
                    }
                    "Modo nocturno" -> {
                    }
                    "Idioma Ingles" -> {
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val toGetBack : TextView = findViewById(R.id.tv_toGoBack1)
        toGetBack.setOnClickListener {
            val intent = Intent(this@ViewDocumentsActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        val idImage = intent.getStringExtra("ID_IMAGE").toString()
        val imageView = findViewById<ImageView>(R.id.iv_document)
        val call1 = sophosApi.getImage(idImage)
        call1.enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if (response.isSuccessful) {
                    val image = response.body()?.Items?.get(0)
                    if (image != null) {
                        val imageBase64 = image.Adjunto
                        val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
                        val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        imageView.setImageBitmap(imageBitmap)
                    }
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@ViewDocumentsActivity, "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
            }
        })

        val correo = SharedData.sharedString.toString()
        val call = sophosApi.loadDocuments(correo)
        call.enqueue(object : Callback<DocumentResponse> {
            override fun onResponse(call: Call<DocumentResponse>, response: Response<DocumentResponse>) {
                if (response.isSuccessful) {
                    val documentResponse = response.body()
                    if (documentResponse != null) {
                        recyclerView.adapter = DocumentAdapter(documentResponse.Items)
                        recyclerView.layoutManager = LinearLayoutManager(this@ViewDocumentsActivity)
                    }
                }
            }

            override fun onFailure(call: Call<DocumentResponse>, t: Throwable) {
            }
        })
    }
}




