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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp_api.databinding.ActivityFavoriteBinding
import com.google.android.material.snackbar.Snackbar

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: AdapterFavorite
    private lateinit var viewModel: FavoriteViewModel

    val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.i(TAG, "#### FavoriteActivity resultLauncher : $result")
        if (result.data != null) {
            when (result.resultCode) {
                DetailActivity.RESULT_ADD -> {
                    val homework =
                        result.data?.getParcelableExtra<Items>(DetailActivity.
                        EXTRA_HOMEWORK) as Items
//                    adapter.addItem(homework)
                    viewModel.addItem(homework)
                    binding.recyclerFavorite.smoothScrollToPosition(adapter.itemCount - 1)
                    showSnackbarMessage("Data berhasil ditambahkan")
                }
                DetailActivity.RESULT_DELETE -> {
                    val position =
                        result.data?.getIntExtra(DetailActivity.EXTRA_POSITION, 0)
                                as Int
//                    adapter.removeItem(position)
                    viewModel.removeItem(position)
                    showSnackbarMessage("Data berhasil dihapus")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "#### FavoriteActivity onCreate $savedInstanceState")
        enableEdgeToEdge()

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        supportActionBar?.title = "Favorite News"

        setupWindowInsets()

        showRecyler()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.favoriteNews.observe(this) { news ->
            adapter.listNews = news as ArrayList<Items>
            if (news.isEmpty()) {
                showSnackbarMessage("Tidak ada data saat ini")
            }
            Log.i(TAG, "#### FavoriteActivity observeViewModel : $news")
        }
        viewModel.isLoading.observe(this) { isLoading ->
            // Update UI based on loading state
            // For example, show/hide a progress bar or shimmer effect
            Log.i(TAG, "#### FavoriteActivity isLoading : $isLoading")
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main3)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showRecyler() {
        binding.recyclerFavorite.layoutManager = LinearLayoutManager(this)
        binding.recyclerFavorite.setHasFixedSize(true)

        adapter = AdapterFavorite(object : AdapterFavorite.OnItemClickCallback {
            override fun onItemClicked(selectedNews: Items?, position: Int?) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, selectedNews)
                intent.putExtra(DetailActivity.EXTRA_POSITION, position)
                resultLauncher.launch(intent)
            }
        })

        binding.recyclerFavorite.adapter = adapter
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.recyclerFavorite, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "FavoriteActivity"
    }
}