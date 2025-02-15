package com.example.newsapp_api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AdapterNews (private val listNews: ArrayList<Items>) :
    RecyclerView.Adapter<AdapterNews.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNewsName: TextView = itemView.findViewById(R.id.tv_news_id)
        var tvNewsDesc: TextView = itemView.findViewById(R.id.tv_news_name)
        var imgNews: ImageView = itemView.findViewById(R.id.img_news)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.tvNewsName.text = listNews[position].title
        holder.tvNewsDesc.text = listNews[position].url

        val img = listNews[position].urlToImg

        Picasso.get().load(img).into(holder.imgNews)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listNews[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listNews.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Items)
    }

}