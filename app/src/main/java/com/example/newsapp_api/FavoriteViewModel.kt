package com.example.newsapp_api

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp_api.db.NewsHelper
import com.example.newsapp_api.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val newsHelper = NewsHelper.getInstance(application)
    private val _favoriteNews = MutableLiveData<List<Items>>()
    val favoriteNews: LiveData<List<Items>> = _favoriteNews
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadFavoriteNews()
    }

    private fun loadFavoriteNews() {
        _isLoading.value = true
        viewModelScope.launch {
            val news = loadNewsFromDatabase()
            _favoriteNews.value = news
            _isLoading.value = false
        }
    }

    private suspend fun loadNewsFromDatabase(): List<Items> = withContext(Dispatchers.IO) {
        newsHelper.open()
        val cursor = newsHelper.queryAll()
        val news = MappingHelper.mapCursorToArrayList(cursor)
        newsHelper.close()
        Log.i(TAG, "#### FavoriteViewModel loadNewsFromDatabase : $news")
        news
    }

    fun addItem(item: Items) {
        val currentList = _favoriteNews.value.orEmpty().toMutableList()
        currentList.add(item)
        _favoriteNews.value = currentList
    }

    fun removeItem(position: Int) {
        val currentList = _favoriteNews.value.orEmpty().toMutableList()
        if (position >= 0 && position < currentList.size) {
            currentList.removeAt(position)
            _favoriteNews.value = currentList
        }
    }

    companion object {
        private const val TAG = "FavoriteViewModel"
    }
}