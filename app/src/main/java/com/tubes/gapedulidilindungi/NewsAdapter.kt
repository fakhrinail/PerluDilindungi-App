package com.tubes.gapedulidilindungi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tubes.gapedulidilindungi.models.NewsModel
import kotlinx.android.synthetic.main.adapter_news.view.*

class NewsAdapter(val results : ArrayList<NewsModel.Results>, val listener: OnAdapterListener) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_news, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.textViewNews__title.text = results[position].title
        holder.view.textViewNews__date.text = results[position].pubDate.subSequence(0, results[position].pubDate.length - 6)
        Glide.with(holder.view)
            .load(results[position].enclosure._url)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .centerCrop()
            .into(holder.view.imageViewNews__thumbnail)
        holder.view.setOnClickListener {
            listener.onClick(results[position])
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view)

    fun setData (data: List<NewsModel.Results>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(result: NewsModel.Results)
    }
}