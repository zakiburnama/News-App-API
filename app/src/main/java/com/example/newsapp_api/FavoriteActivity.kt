package com.example.newsapp_api

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp_api.databinding.ActivityFavoriteBinding
import com.example.newsapp_api.db.NewsHelper
import com.example.newsapp_api.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: AdapterFavorite

    val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.data != null) {
            when (result.resultCode) {
                DetailActivity.RESULT_ADD -> {
                    val homework =
                        result.data?.getParcelableExtra<Items>(DetailActivity.
                        EXTRA_HOMEWORK) as Items
                    adapter.addItem(homework)
                    binding.listFavorite.smoothScrollToPosition(adapter.itemCount - 1)
                    showSnackbarMessage("Data berhasil ditambahkan")
                }
//                AddNewHomeworkActivity.RESULT_UPDATE -> {
//                    val homework =
//                        result.data?.getParcelableExtra<Homework>(AddNewHomeworkActivity.
//                        EXTRA_HOMEWORK) as Homework
//                    val position =
//                        result?.data?.getIntExtra(AddNewHomeworkActivity.EXTRA_POSITION, 0)
//                                as Int
//                    adapter.updateItem(position, homework)
//                    binding.rvHomework.smoothScrollToPosition(position)
//                    showSnackbarMessage("Data berhasil diubah")
//                }
                DetailActivity.RESULT_DELETE -> {
                    val position =
                        result.data?.getIntExtra(DetailActivity.EXTRA_POSITION, 0)
                                as Int
                    adapter.removeItem(position)
                    showSnackbarMessage("Data berhasil dihapus")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_favorite)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main3)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        showRecyler()
        binding.listFavorite.layoutManager = LinearLayoutManager(this)
        binding.listFavorite.setHasFixedSize(true)

        adapter = AdapterFavorite(object : AdapterFavorite.OnItemClickCallback {
            override fun onItemClicked(selectedNote: Items?, position: Int?) {
                Toast.makeText(this@FavoriteActivity, selectedNote?.title, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, selectedNote)
                intent.putExtra(DetailActivity.EXTRA_POSITION, position)
                resultLauncher.launch(intent)
            }
        })
        binding.listFavorite.adapter = adapter

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Items>(EXTRA_STATE)
            if (list != null) {
                adapter.listNews = list
            }
        }

//        loadNotesAsync()
    }

    private fun loadNotesAsync() {
        lifecycleScope.launch {
//            binding.progressbar.visibility = View.VISIBLE
            val newsHelper = NewsHelper.getInstance(applicationContext)
            newsHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = newsHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
//            binding.progressbar.visibility = View.INVISIBLE
            val news = deferredNotes.await()
            if (news.size > 0) {
                adapter.listNews = news
                Log.i("TAG", "#### FavoriteActivity size : ${news.size}")
                Log.i("TAG", "#### FavoriteActivity  : ${news}")
            } else {
                adapter.listNews = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
            newsHelper.close()
        }
    }

    private fun showRecyler(){


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNews)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.listFavorite, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
}