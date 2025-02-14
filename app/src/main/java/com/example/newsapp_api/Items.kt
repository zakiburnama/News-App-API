package com.example.newsapp_api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Items(
    var name: String?,
    var author: String?,
    var title: String?,
    var description: String?,
    var url: String?,
    var urlToImg: String?,
    var date: String?,
    var _id: Int? = 0,
    var isFavorite: Boolean? = false
) : Parcelable

//                        a.	Name
//                        b.	Author
//                        c.	Title
//                        d.	Description
//                        e.	Url ( Untukmembukahalaman baru )
//                        f.	UrlToImage ( Untukmenampilkangambar)
//                        g.	PublishedAt