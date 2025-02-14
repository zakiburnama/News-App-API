package com.example.newsapp_api

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val listNews = ArrayList<Items>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.listNews)

        getListNews()
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
                getListNewsApi(query)
//                mainViewModel.getUsername(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
//                mainViewModel.getUsername(newText)
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

    private fun getListNewsApi(keyword : String = "Tesla") {
        val client = AsyncHttpClient()
//        val url = "https://newsapi.org/v2/everything?q=tesla&from=2025-01-13&sortBy=publishedAt&apiKey=fb8800c5137b4c8aa839a1287686065f"
        val url = "https://newsapi.org/v2/everything?q={$keyword}&from=2025-01-13&sortBy=publishedAt&apiKey=fb8800c5137b4c8aa839a1287686065f"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
            ) {
                val result = String(responseBody)
                Log.i("TAG", "#### onSuccess : $result")
                try {
                    val jsonObjectNews = JSONObject(result)
                    val status = jsonObjectNews.getString("status")
                    val totalResults = jsonObjectNews.getInt("totalResults")
                    val jsonArrayNews = jsonObjectNews.getJSONArray("articles")

                    Log.i("TAG", "#### onSuccess try jsonObjectNews : $jsonObjectNews")
                    Log.i("TAG", "#### onSuccess try status : $status")
                    Log.i("TAG", "#### onSuccess try totalResults : $totalResults")
                    Log.i("TAG", "#### onSuccess try jsonArrayNews : $jsonArrayNews")

                    for (i in 0 until jsonArrayNews.length()) {
                        val jsonObject = jsonArrayNews.getJSONObject(i)

                        val author = jsonObject.getString("author")
                        val title = jsonObject.getString("title")
                        val description = jsonObject.getString("description")
                        val url = jsonObject.getString("url")
                        val urlToImage = jsonObject.getString("urlToImage")
                        val date = jsonObject.getString("publishedAt")

                        val source = jsonObject.getJSONObject("source")
                        val name = source.getString("name")

//                        listNews.add(Items(author, name, urlToImage))
                        listNews.add(Items(
                            name,           // name
                            author,         //Author
                            title,          // Title
                            description,    // Description
                            url,            // Url
                            urlToImage,     //url to image
                            date,           // PublishedAt
                        ))

                        Log.i("TAG", "#### onSuccess try 1 : $author")
                        Log.i("TAG", "#### onSuccess try 1 : $name")
                    }
                    showRecycler()

                }catch (e: Exception) {
                    Log.i("TAG", "#### catch : ${e.message}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?,
            ) {
                Log.i("TAG", "#### onFailure statusCode : $statusCode")
                Log.i("TAG", "#### onFailure headers : $headers")
                Log.i("TAG", "#### onFailure responseBody : $responseBody")
                Log.i("TAG", "#### onFailure error : $error")
            }

        })

    }


    private fun getListNews() {
        val client = AsyncHttpClient()
        val keyword = "awda"
        val urlnews = "https://newsapi.org/v2/everything?apiKey=fb8800c5137b4c8aa839a1287686065f"
        val urlnews2 = "https://newsapi.org/v2/everything?q=tesla&from=2025-01-13&sortBy=publishedAt&apiKey=fb8800c5137b4c8aa839a1287686065f"
        val urlnews3 = "https://newsapi.org/v2/everything?q={$keyword}&from=2025-01-13&sortBy=publishedAt&apiKey=fb8800c5137b4c8aa839a1287686065f"
        val urlnews4 = "https://newsapi.org/v2/everything?q=keyword&apiKey=fb8800c5137b4c8aa839a1287686065f"
        val url = "https://api.restful-api.dev/objects"
        val img = "https://blog.cdn.own3d.tv/resize=fit:crop,height:400,width:600/tbv2RYWpReqNtof2dD0U"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = responseBody?.let { String(it) }
                    Log.i("TAG", "#### onSuccess : ${result}")

                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

//                        a.	Name
//                        b.	Author
//                        c.	Title
//                        d.	Description
//                        e.	Url ( Untukmembukahalaman baru )
//                        f.	UrlToImage ( Untukmenampilkangambar)
//                        g.	PublishedAt

                        val name = jsonObject.getString("name")
                        val id = jsonObject.getString("id")

//                        listNews.add(Items(name, id, img)) // name, authorm title
                        listNews.add(Items(
                            name, // name
                            "Author", //Author
                            "Title", // Title
                            "Description", // Description
                            "Url", // Url
                            img, //url to image
                            id, // PublishedAt
                        ))

                        Log.i("TAG", "#### onSuccess try 1 : $name")
                        Log.i("TAG", "#### onSuccess try 1 : $id")
                    }
                    showRecycler()
                }catch (e: Exception) {
                    Log.i("TAG", "#### catch : ${e.message}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?,
            ) {
                Log.i("TAG", "#### onFailure statusCode : $statusCode")
                Log.i("TAG", "#### onFailure headers : $headers")
                Log.i("TAG", "#### onFailure responseBody : $responseBody")
                Log.i("TAG", "#### onFailure error : $error")
            }
        })
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
        //
//        recyclerView.layoutManager = layoutManager
//        binding.listNews.layoutManager = layoutManager
        recyclerView.layoutManager = layoutManager


        val adapter = AdapterNews(listNews)
//        binding.listNews.adapter = adapter
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


    private fun getListNews2() {
        val client = AsyncHttpClient()

//        fb8800c5137b4c8aa839a1287686065f
//        58be7a445f224078aa57ffb2ee12326e

//        val url = "https://newsapi.org/v2/everything?q=tesla&from=2025-01-12&sortBy=publishedAt&apiKey=API_KEY"
//        val url = "https://newsapi.org/v2/everything?q=tesla&from=2025-01-12&sortBy=publishedAt&apiKey=fb8800c5137b4c8aa839a1287686065f"
        val url = "https://newsapi.org/v2/everything?q=tesla&from=2025-01-12&sortBy=publishedAt&apiKey=58be7a445f224078aa57ffb2ee12326e"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>,responseBody: ByteArray) {
                val result = String(responseBody)

                Log.i("TAG", "#### onSuccess : ${result}")

                try {

                    val jsonObjectNews = JSONObject(result)
                    val status = jsonObjectNews.getString("status")
                    val totalResults = jsonObjectNews.getInt("totalResults")
                    val jsonArrayNews = jsonObjectNews.getJSONArray("articles")

//                    Log.i("TAG", "#### onSuccess try jsonObjectNews : ${jsonObjectNews}")
//                    Log.i("TAG", "#### onSuccess try status : ${status}")
//                    Log.i("TAG", "#### onSuccess try totalResults : ${totalResults}")
//                    Log.i("TAG", "#### onSuccess try jsonArrayNews : ${jsonArrayNews}")

//                    val jsonArray = JSONArray(result) //

//                    for (i in 0 until jsonArrayNews.length()) {
//                        val jsonObject = jsonArrayNews.getJSONObject(i)
//
//                        val author = jsonObject.getString("author")
//                        val title = jsonObject.getString("title")
//                        val urlToImage = jsonObject.getString("urlToImage")
//
//                        val source = jsonObject.getJSONObject("source")
//                        val name = source.getString("name")
//
////                        listCat.add(Cat(author, name, urlToImage))
//
//                        Log.i("TAG", "#### onSuccess try 1 : $author")
//                        Log.i("TAG", "#### onSuccess try 1 : $name")
//                    }

//                    Log.i("TAG", "#### onSuccess try 2 : $jsonArray")


//                    showRecycler()
                } catch (e: Exception) {
                    Log.i("TAG", "#### catch : ${e.message}")
//                    Toast.makeText(this@MainActivity,e.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.i("TAG", "#### onFailure statusCode : $statusCode")
                Log.i("TAG", "#### onFailure headers : $headers")
                Log.i("TAG", "#### onFailure responseBody : $responseBody")
                Log.i("TAG", "#### onFailure error : $error")
//                Toast.makeText(this@MainActivity, statusCode, Toast.LENGTH_SHORT).show()
            }
        })
    }
}