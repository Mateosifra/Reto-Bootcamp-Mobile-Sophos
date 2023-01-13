package com.example.retosophos.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import com.example.retosophos.*
import com.example.retosophos.viewModels.sophosApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class SendDocumentsActivity : AppCompatActivity() {

    val REQUEST_CODE_SELECT_IMAGE = 1
    companion object {
        private const val PICK_IMAGE = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
    }
    private lateinit var base64Image: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documents)

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
                        startActivity(Intent(this@SendDocumentsActivity,SendDocumentsActivity::class.java))
                    }
                    "Ver documentos" -> {
                        // Acción para ver documentos
                        startActivity(Intent(this@SendDocumentsActivity, ViewDocumentsActivity::class.java))
                    }
                    "Oficinas" -> {
                        startActivity(Intent(this@SendDocumentsActivity, OfficesActivity::class.java))
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
            }
        }
        val toGetBack : TextView = findViewById(R.id.tv_toGoBack)
        toGetBack.setOnClickListener {
            val intent = Intent(this@SendDocumentsActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        val spinnerDocument: Spinner = findViewById(R.id.spinnerTypeDocuments)
        val listOptionsDocument = arrayListOf("Tipode de documento","CC", "TI", "PA", "CE")
        val adapterListDocument =
            ArrayAdapter(this, R.layout.spinner_type_documents, listOptionsDocument)
        spinnerDocument.adapter = adapterListDocument
        val spinnerTypeDocuments: Spinner = findViewById(R.id.spinnerCiudad)

        sophosApi.loadOffices().enqueue(
            object : Callback<Oficinas> {
                override fun onResponse(call: Call<Oficinas>, response: Response<Oficinas>) {

                    val oficinas = response.body()
                    val items = oficinas!!.Items
                    val ciudades = items.map { "${it.Ciudad} - ${it.Nombre}" }
                    val list = listOf("Selecciona una ciudad") + ciudades
                    val adapter =
                        ArrayAdapter(
                            this@SendDocumentsActivity,
                            R.layout.spinner_type_attached,
                            list
                        )
                    adapter.setDropDownViewResource(R.layout.spinner_type_attached)
                    spinnerTypeDocuments.adapter = adapter
                }

                override fun onFailure(call: Call<Oficinas>, t: Throwable) {
                }
            })

        val buttonDocument: Button = findViewById(R.id.btnDocument)
        buttonDocument.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"), PICK_IMAGE)
        }

        val imageViewCamera : ImageView = findViewById(R.id.ivCamera)
        imageViewCamera.setOnClickListener{
            // Crea un Intent para abrir la cámara
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Verifica que exista una aplicación que pueda manejar el Intent
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
                val imageUri = data?.data
                if (imageUri != null) {
                    val imageStream =contentResolver.openInputStream(imageUri)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    // Convertir la imagen seleccionada a base64
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                    val imageBytes = byteArrayOutputStream.toByteArray()
                    base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                }
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                //Convertir la imagen seleccionada a base64
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()
                base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            }

        val buttonSentDocument : Button = findViewById(R.id.btnSend)
        buttonSentDocument.setOnClickListener {
            // Comprueba si se ha seleccionado una imagen
            if (base64Image.isNotEmpty()) {
                val putDocumentRequest= PutDocumentRequest(
                    TipoId = findViewById<Spinner>(R.id.spinnerTypeDocuments).toString(),
                    Identificacion = findViewById<EditText>(R.id.etNumberDocument).text.toString(),
                    Nombre = findViewById<EditText>(R.id.etNames).text.toString(),
                    Apellido = findViewById<EditText>(R.id.etLastnames).text.toString(),
                    Ciudad = findViewById<Spinner>(R.id.spinnerCiudad).toString(),
                    Correo = findViewById<EditText>(R.id.etCorreo).text.toString(),
                    TipoAdjunto = findViewById<EditText>(R.id.etTypeAttached).text.toString(),
                    Adjunto = base64Image
                )
                val call = sophosApi.saveDocuments(putDocumentRequest)
                call.enqueue(object : Callback<PutDocumentResponse> {
                    override fun onResponse(call: Call<PutDocumentResponse>, response: Response<PutDocumentResponse>) {
                        if (response.isSuccessful) {
                            val putResponse = response.body()
                            if (putResponse != null) {
                                if (putResponse.put) {
                                    Log.d("Retrofit", "Documento subido correctamente")
                                    Toast.makeText(applicationContext, "Documento subido correctamente", Toast.LENGTH_SHORT).show()
                                } else {
                                    Log.d("Retrofit", "Error al subir el documento")
                                    Toast.makeText(applicationContext, "Error al subir el documento", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Log.d("Retrofit", "Error en la respuesta del servidor")
                            Toast.makeText(applicationContext, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<PutDocumentResponse>, t: Throwable) {
                        Log.d("Retrofit", "Error en la petición")
                        Toast.makeText(applicationContext, "Error en la petición", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Selecciona una imagen antes de subirla", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

















