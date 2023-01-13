package com.example.retosophos


data class DocumentResponse(
    val Items: List<Registro>,
    val Count: Int,
    val ScannedCount: Int
)

data class Registro(
    val IdRegistro: String,
    val Fecha: String,
    val TipoAdjunto: String,
    val Nombre: String,
    val Apellido: String
)

