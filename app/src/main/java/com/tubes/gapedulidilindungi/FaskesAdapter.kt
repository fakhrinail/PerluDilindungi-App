package com.tubes.gapedulidilindungi

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tubes.gapedulidilindungi.data.BookmarkData
import kotlinx.android.synthetic.main.adapter_faskes.view.*

class FaskesAdapter(val results : ArrayList<BookmarkData>, val listener: FaskesAdapter.OnAdapterListener) : RecyclerView.Adapter<FaskesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_faskes, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = results[position]
        holder.view.textViewFaskes__title.text = "Faskes " + currentItem.namaFaskes
        holder.view.textViewFaskes__type.text = currentItem.jenisFaskes
        if (currentItem.jenisFaskes == "PUSKESMAS") {
            holder.view.textViewFaskes__type.setBackgroundColor(Color.parseColor("#5551FF"))
        }
        else if (currentItem.jenisFaskes == "KLINIK") {
            holder.view.textViewFaskes__type.setBackgroundColor(Color.parseColor("#41A7F6"))
        }
        else {
            holder.view.textViewFaskes__type.setBackgroundColor(Color.parseColor("#EF5DA8"))
        }
        holder.view.textViewFaskes__address.text = currentItem.alamatFaskes
        holder.view.textViewFaskes__contact.text = currentItem.noTelpFaskes
        holder.view.textViewFaskes__code.text = "KODE: " + currentItem.kodeFaskes

        holder.view.setOnClickListener {
            listener.onClick(results[position])
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view)

    fun setData (data: List<BookmarkData>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(result: BookmarkData)
    }
}