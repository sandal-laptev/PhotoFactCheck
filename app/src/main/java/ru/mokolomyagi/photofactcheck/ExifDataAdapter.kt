package ru.mokolomyagi.photofactcheck

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExifDataAdapter(private val data: List<Pair<String, String>>) :
    RecyclerView.Adapter<ExifDataAdapter.ExifViewHolder>() {

    class ExifViewHolder(val view: TextView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExifViewHolder {
        val textView = TextView(parent.context).apply {
            setPadding(16, 8, 16, 8)
            textSize = 16f
        }
        return ExifViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ExifViewHolder, position: Int) {
        val (tag, value) = data[position]
        holder.view.text = "$tag: $value"
    }

    override fun getItemCount(): Int = data.size
}