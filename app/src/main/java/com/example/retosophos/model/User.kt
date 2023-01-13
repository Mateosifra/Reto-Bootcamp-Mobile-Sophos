package com.example.retosophos




data class LoginResponse(
    val id: String,
    val nombre: String,
    val apellido: String,
    val acceso: Boolean,
    val admin: Boolean
)

data class Oficinas(

    val Items: List<Item>,
    val Count: Int,
    val ScannedCount: Int
)

data class Item(
    val Ciudad: String,
    val Longitud: String,
    val IdOficina: Int,
    val Latitud: String,
    val Nombre: String
)

data class PutDocumentRequest(
    val TipoId: String,
    val Identificacion: String,
    val Nombre: String,
    val Apellido: String,
    val Ciudad: String,
    val Correo: String,
    val TipoAdjunto: String,
    val Adjunto: String
)

data class PutDocumentResponse(
    val put: Boolean
)
