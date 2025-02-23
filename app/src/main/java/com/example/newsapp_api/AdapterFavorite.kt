package com.example.newsapp_api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp_api.databinding.ItemNewsBinding
import com.squareup.picasso.Picasso


class AdapterFavorite(private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<AdapterFavorite.FavoriteViewHolder>() {
    var listNews = ArrayList<Items>()
        set(listBerita) {
            if (listBerita.size > 0) {
                this.listNews.clear()
            }
            this.listNews.addAll(listBerita)
            notifyDataSetChanged()
        }

    fun addItem(news: Items) {
        this.listNews.add(news)
        notifyItemInserted(this.listNews.size - 1)
    }

    fun updateItem(position: Int, news: Items) {
        this.listNews[position] = news
        notifyItemChanged(position, news)
    }

    fun removeItem(position: Int) {
        this.listNews.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNews.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    override fun getItemCount(): Int = this.listNews.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemNewsBinding.bind(itemView)
        fun bind(news: Items) {
            Picasso.get().load(news.urlToImg).into(binding.imgNews)
            binding.tvNewsId.text = news.title
            binding.tvNewsName.text = news.url
            binding.cardView.setOnClickListener {
                onItemClickCallback.onItemClicked(news, adapterPosition)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(selectedNews: Items?, position: Int?)
    }
}
