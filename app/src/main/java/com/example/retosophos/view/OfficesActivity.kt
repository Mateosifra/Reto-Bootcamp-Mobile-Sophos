package com.example.retosophos.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.retosophos.Oficinas
import com.example.retosophos.R
import com.example.retosophos.viewModels.sophosApi
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfficesActivity : AppCompatActivity(), OnMapReadyCallback, OnMyLocationButtonClickListener {

    private lateinit var map:GoogleMap
    companion object{
        const val REQUESt_CODE_LOCATION = 0
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        office()
        enableLocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offices)

        val spinnerMenu : Spinner = findViewById(R.id.spinner_opciones)
        val listOptions = arrayListOf("","Enviar documentos","Ver documentos","Oficinas","Modo nocturno", "Idioma Ingles")
        val adapterList = ArrayAdapter<String>(this, R.layout.spinner_options, listOptions)
        spinnerMenu.adapter = adapterList
        spinnerMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val opcion = parent.getItemAtPosition(position) as String
                when (opcion) {
                    "Enviar documentos" -> {
                        startActivity( Intent(this@OfficesActivity, SendDocumentsActivity::class.java))
                    }
                    "Ver documentos" -> {
                        startActivity( Intent(this@OfficesActivity, ViewDocumentsActivity::class.java))
                    }
                    "Oficinas" -> {
                        startActivity( Intent(this@OfficesActivity, OfficesActivity::class.java))
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
        val toGetBack : TextView = findViewById(R.id.tv_toGoBack1)
        toGetBack.setOnClickListener {
            val intent = Intent(this@OfficesActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        createFragment()

    }

    private fun office() {

        sophosApi.loadOffices().enqueue(object : Callback<Oficinas>{
            override fun onResponse(call: Call<Oficinas>, response: Response<Oficinas>) {
                val oficinas = response.body()
                if (oficinas != null) {
                    for (item in oficinas.Items) {
                        val latLng = LatLng(item.Latitud.toDouble(), item.Longitud.toDouble())
                        val marker = map.addMarker(MarkerOptions().position(latLng))
                        marker.title = item.Nombre
                    }
                }
            }
            override fun onFailure(call: Call<Oficinas>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun createFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation(){
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()){
            map.isMyLocationEnabled = true
        } else{
            requestLocationPermission()
        }
    }
    private fun requestLocationPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
            Toast.makeText(this, "Ve ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUESt_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUESt_CODE_LOCATION ->if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }else{
                Toast.makeText(this, "Para activar la localizacion activa los permisos", Toast.LENGTH_SHORT).show()

            }
            else -> {}
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::map.isInitialized) return
        if (!isLocationPermissionGranted()){
            map.isMyLocationEnabled = false
            Toast.makeText(this, "Para activar la localizacion activa los permisos", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        return true
    }
}