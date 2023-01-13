package com.example.retosophos.model

data class ImageResponse(
    val Items: List<Image>,
    val Count: Int,
    val ScannedCount: Int
)

data class Image(
    val Ciudad: String,
    val Fecha: String,
    val TipoAdjunto: String,
    val Nombre: String,
    val Apellido: String,
    val Identificacion: String,
    val IdRegistro: String,
    val TipoId: String,
    val Correo: String,
    val Adjunto: String
)