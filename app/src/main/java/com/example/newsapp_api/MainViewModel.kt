package com.example.newsapp_api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val listNews = ArrayList<Items>()

    private val _listNews = MutableLiveData<List<Items>>()
    val listNewsLive: LiveData<List<Items>> = _listNews

    companion object{
        private const val TAG = "MainViewModel"
    }

    fun getNews(keyword : String = "cristiano ronaldo"){
        listNews.clear()
        getListNewsApi(keyword)
    }

    private fun getListNewsApi(keyword : String) {
        val client = AsyncHttpClient()
//        val url2 = "https://newsapi.org/v2/everything?q=$keyword&from=2025-01-16&sortBy=publishedAt&apiKey=fb8800c5137b4c8aa839a1287686065f"
        val url2 = "https://newsapi.org/v2/everything?q=$keyword&sortBy=publishedAt&apiKey=fb8800c5137b4c8aa839a1287686065f"
        val url = url2.filterNot { it.isWhitespace() }
        Log.i(TAG, "#### url : $url")
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

                    _listNews.value = listNews

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
}