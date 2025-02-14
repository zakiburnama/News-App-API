package com.example.newsapp_api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp_api.databinding.ItemNewsBinding
import com.squareup.picasso.Picasso


class AdapterFavorite(private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<AdapterFavorite.NoteViewHolder>() {
    var listNews = ArrayList<Items>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listNews.clear()
            }
            this.listNews.addAll(listNotes)
        }

    fun addItem(note: Items) {
        this.listNews.add(note)
        notifyItemInserted(this.listNews.size - 1)
    }

    fun updateItem(position: Int, note: Items) {
        this.listNews[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listNews.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNews.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    override fun getItemCount(): Int = this.listNews.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemNewsBinding.bind(itemView)
        fun bind(news: Items) {
            Picasso.get().load(news.urlToImg).into(binding.imgNews)
            binding.tvNewsId.text = news.name
            binding.tvNewsName.text = news.date
            binding.cardView.setOnClickListener {
                onItemClickCallback.onItemClicked(news, adapterPosition)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(selectedNote: Items?, position: Int?)
    }
}
