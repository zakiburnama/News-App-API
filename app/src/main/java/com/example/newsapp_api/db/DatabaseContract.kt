package com.example.newsapp_api.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class NewsColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "news"
            const val _ID = "_id"
            const val NAME = "name"
            const val AUTHOR = "author"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val URL = "url"
            const val URL_TO_IMG = "url_to_img"
            const val DATE = "date"
            const val FAVORITE = "favorite"
        }
    }
}