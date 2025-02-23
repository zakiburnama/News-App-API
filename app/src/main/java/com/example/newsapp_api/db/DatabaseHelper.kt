package com.example.newsapp_api.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.newsapp_api.db.DatabaseContract.NewsColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbnewsapp"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_NEWS = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.NewsColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.NewsColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseContract.NewsColumns.AUTHOR} TEXT NOT NULL," +
                " ${DatabaseContract.NewsColumns.TITLE} TEXT NOT NULL," +
                " ${DatabaseContract.NewsColumns.DESCRIPTION} TEXT NOT NULL," +
                " ${DatabaseContract.NewsColumns.URL} TEXT NOT NULL," +
                " ${DatabaseContract.NewsColumns.URL_TO_IMG} TEXT NOT NULL," +
                " ${DatabaseContract.NewsColumns.DATE} TEXT NOT NULL," +
                " ${DatabaseContract.NewsColumns.FAVORITE} INTEGER NOT NULL)"


    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NEWS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}

