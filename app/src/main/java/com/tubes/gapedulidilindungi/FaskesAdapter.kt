package com.tubes.gapedulidilindungi

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
        holder.view.textViewFaskes__title.text = currentItem.namaFaskes
        holder.view.textViewFaskes__type.text = currentItem.jenisFaskes
        holder.view.textViewFaskes__address.text = currentItem.alamatFaskes
        holder.view.textViewFaskes__contact.text = currentItem.noTelpFaskes
        holder.view.textViewFaskes__code.text = currentItem.kodeFaskes

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