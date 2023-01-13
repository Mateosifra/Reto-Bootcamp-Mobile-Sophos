    package com.example.retosophos.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.retosophos.LoginResponse
import com.example.retosophos.R
import com.example.retosophos.model.SharedData
import com.example.retosophos.viewModels.sophosApi
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

    class MainActivity : AppCompatActivity() {

        private lateinit var executor: Executor
        private lateinit var biometricPrompt: BiometricPrompt
        private lateinit var promptInfo: BiometricPrompt.PromptInfo

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            val botonHuella = findViewById<Button>(R.id.btn_Huella)
            botonHuella.setOnClickListener{

                executor = ContextCompat.getMainExecutor(this)
                biometricPrompt =
                    BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            // Mostrar mensaje de error al usuario
                            Toast.makeText(applicationContext, "mal hecho", Toast.LENGTH_SHORT).show()
                        }
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            // Autenticación exitosa, puedes hacer algo aquí
                            Toast.makeText(applicationContext, "bien buena", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity, MenuActivity::class.java)
                            startActivity(intent)
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            // Autenticación fallida, puedes mostrar un mensaje al usuario
                            Toast.makeText(applicationContext, "Algo salio mal", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })

                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Autenticación con huella digital")
                    .setSubtitle("Utiliza tu huella digital para autenticarte")
                    .setDescription("La autenticación con huella digital es más segura que usar una contraseña")
                    .setNegativeButtonText("Cancelar")
                    .build()

                // Iniciar el proceso de autenticación con huella digital
                biometricPrompt.authenticate(promptInfo)


                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Autenticación con huella digital")
                    .setSubtitle("Utiliza tu huella digital para autenticarte")
                    .setDescription("La autenticación con huella digital es más segura que usar una contraseña")
                    .setNegativeButtonText("Cancelar")
                    .build()

                // Iniciar el proceso de autenticación con huella digital
                biometricPrompt.authenticate(promptInfo)
            }


        val textViewLogin =findViewById<Button>(R.id.btn_Ingresar)
        textViewLogin.setOnClickListener{

            init()
        }
    }
      private fun init() {

        var email = findViewById<EditText>(R.id.et_Account).text.toString().trim()
          SharedData.sharedString = email
        var password = findViewById<TextInputEditText>(R.id.et_Key).text.toString().trim()
          SharedData.sharedPass = password


        sophosApi.loginUser(email,password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response.body()

                if (loginResponse?.acceso == true) {
                    Toast.makeText(applicationContext, "Sesion iniciada.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, MenuActivity::class.java)
                    startActivity(intent)


                } else {
                    // Inicio de sesión fallido, muestra el mensaje de error
                    Toast.makeText(applicationContext, "El usuario o la contraseña son incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Error en el servidor.", Toast.LENGTH_SHORT).show()
            }
        })
    }



}