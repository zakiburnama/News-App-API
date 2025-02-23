package com.example.newsapp_api

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.newsapp_api.databinding.ActivityDetailBinding
import com.example.newsapp_api.db.DatabaseContract
import com.example.newsapp_api.db.NewsHelper
import com.example.newsapp_api.helper.DateTimeUtils
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private var data: Items? = null
    private lateinit var newsHelper: NewsHelper

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "Breaking News"

        newsHelper = NewsHelper.getInstance(applicationContext)
        newsHelper.open()

        data = intent.getParcelableExtra<Items>(EXTRA_DATA)
        Log.i("TAG", "#### DetailActivity name : ${data?.name}")

        if (data?.isFavorite == true) {
            val drawable: Drawable? = binding.newsDetailFavoriteButton.drawable?.constantState?.newDrawable()?.mutate()
            drawable?.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
            binding.newsDetailFavoriteButton.setImageDrawable(drawable)
        }

        val date = data?.date?.let { DateTimeUtils.formatDateTime(it) }
        Picasso.get().load(data?.urlToImg).into(binding.newsDetailImage)
        binding.newsDetailName.text = data?.name
        binding.newsDetailTitle.text = data?.title
        binding.newsDetailAuthor.text = data?.author
        binding.newsDetailPublishedAt.text = date
        binding.newsDetailDescription.text = data?.description


        binding.newsDetailFavoriteButton.setOnClickListener {
            Log.d("TAG", "#### Favorite Button Clicked")
            data?.isFavorite?.let { it1 -> setFavorite(it1) }
        }

    }

    private fun setFavorite(fav: Boolean) {
        if (!fav) {
            val drawable: Drawable? = binding.newsDetailFavoriteButton.drawable?.constantState?.newDrawable()?.mutate()
            drawable?.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
            binding.newsDetailFavoriteButton.setImageDrawable(drawable)
            data?.isFavorite = true
            addNewNews(data as Items)
        } else {
            val drawable: Drawable? = binding.newsDetailFavoriteButton.drawable?.constantState?.newDrawable()?.mutate()
            drawable?.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
            binding.newsDetailFavoriteButton.setImageDrawable(drawable)
            data?.isFavorite = false
            deleteNews()
        }
    }

    private fun deleteNews(){
        val result = newsHelper.deleteById(data?._id.toString()).toLong()
        if (result > 0) {
            val intent = Intent()
            intent.putExtra(EXTRA_POSITION, 0)
            setResult(RESULT_DELETE, intent)
            finish()
        } else {
            Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addNewNews(news: Items){
        val intent = Intent()
        intent.putExtra(EXTRA_HOMEWORK, news)
        intent.putExtra(EXTRA_POSITION, 0)

        val values = ContentValues()

        values.put(DatabaseContract.NewsColumns.NAME, news.name)
        values.put(DatabaseContract.NewsColumns.AUTHOR, news.author)
        values.put(DatabaseContract.NewsColumns.TITLE, news.title)
        values.put(DatabaseContract.NewsColumns.DESCRIPTION, news.description)
        values.put(DatabaseContract.NewsColumns.URL, news.url)
        values.put(DatabaseContract.NewsColumns.URL_TO_IMG, news.urlToImg)
        values.put(DatabaseContract.NewsColumns.DATE, news.date)
        values.put(DatabaseContract.NewsColumns.FAVORITE, true)

        val result = newsHelper.insert(values)

        if (result > 0) {
            Toast.makeText(this, "Berhasil menambah data", Toast.LENGTH_SHORT).show()
            data?._id = result.toInt()
            setResult(RESULT_ADD, intent)
//            finish()
        } else {
            Toast.makeText(this, "Gagal menambah data", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        val EXTRA_DATA: String? = null

        const val EXTRA_HOMEWORK = "extra_homework"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 101
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20

        const val EXTRA_HOMEWORK_TITLE = "extra_homework_title"
        const val EXTRA_HOMEWORK_DESCRIPTION = "extra_homework_title_description"
    }
}