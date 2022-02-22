package com.tubes.gapedulidilindungi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.adapter_news.view.*

class NewsAdapter(val results : ArrayList<NewsModel.Results>) : RecyclerView.Adapter<NewsAdapter.viewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = viewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_news, parent, false))

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.view.textViewNews__title.text = results[position].title
    }

    override fun getItemCount(): Int {
        return results.size
    }

    class viewHolder (val view: View) : RecyclerView.ViewHolder(view)

    fun setData (data: List<NewsModel.Results>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}