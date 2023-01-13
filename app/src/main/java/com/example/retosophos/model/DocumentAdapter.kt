package com.example.retosophos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat


class DocumentAdapter(val documents: List<Registro>) : RecyclerView.Adapter<DocumentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_viewdocument, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val document = documents[position]
        holder.bind(document)
    }

    override fun getItemCount(): Int =  documents.size

}

class DocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val viewDocuments1: TextView = view.findViewById(R.id.tv_viewDocuments1)
    private val viewDocuments2: TextView = view.findViewById(R.id.tv_viewDocuments2)

    fun bind(document: Registro) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = inputFormat.parse(document.Fecha)
        val outputFormat = SimpleDateFormat("dd-MM-yy")
        val formattedDate = outputFormat.format(date)
        viewDocuments1.text = "${formattedDate} - ${document.TipoAdjunto}"
        viewDocuments2.text = "${document.Nombre} - ${document.Apellido}"
    }
}