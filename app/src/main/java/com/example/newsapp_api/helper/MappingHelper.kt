package com.example.newsapp_api.helper

import android.database.Cursor
import com.example.newsapp_api.Items
import com.example.newsapp_api.db.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(newsCursor: Cursor?): ArrayList<Items> {
        val newsList = ArrayList<Items>()
        newsCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.NewsColumns._ID))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.NewsColumns.NAME))
                val author = getString(getColumnIndexOrThrow(DatabaseContract.NewsColumns.AUTHOR))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.NewsColumns.TITLE))
                val description = getString(getColumnIndexOrThrow(DatabaseContract.NewsColumns.DESCRIPTION))
                val url = getString(getColumnIndexOrThrow(DatabaseContract.NewsColumns.URL))
                val urlToImg = getString(getColumnIndexOrThrow(DatabaseContract.NewsColumns.URL_TO_IMG))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.NewsColumns.DATE))
//                val isFavorite = getInt(getColumnIndexOrThrow(DatabaseContract.NewsColumns.FAVORITE))
                newsList.add(Items(name, author, title, description, url, urlToImg,  date, id, true))
            }
        }
        return newsList
    }
}