package com.example.newsapp_api

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val listNews = ArrayList<Items>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel= ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        viewModel.getNews()

        supportActionBar?.title = "Breaking News"

        recyclerView = findViewById(R.id.listNews)

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container)

        showRecycler()

        viewModel.listNewsLive.observe(this) { news ->
            stopShimmerEffect()
            listNews.clear()
            listNews.addAll(news)
            showRecycler()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                viewModel.getNews(query)
                viewModel.listNewsLive.observe(this@MainActivity) { news ->
                    stopShimmerEffect()
                    listNews.clear()
                    listNews.addAll(news)
                    showRecycler()
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
//                viewModel.putQuery(newText)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
//                Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startShimmerEffect() {
        shimmerFrameLayout.startShimmer()
    }

    private fun stopShimmerEffect() {
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun showRecycler() {
        // Use GridLayoutManager with 2 columns
        val layoutManager = GridLayoutManager(this, 2)

        // Set SpanSizeLookup to make large items take full width
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 5 == 0) 2 else 1
            }
        }

        val adapter = AdapterNews(listNews)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object : AdapterNews.OnItemClickCallback{
            override fun onItemClicked(data: Items) {
                move(data)
            }
        })
    }

    private fun move(data: Items) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_DATA, data)
        startActivity(intent)
    }
}