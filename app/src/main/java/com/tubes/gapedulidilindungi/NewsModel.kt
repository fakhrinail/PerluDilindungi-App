package com.tubes.gapedulidilindungi

import java.util.*
import kotlin.collections.ArrayList

//"title": "Situasi COVID-19 di Indonesia (Update per 12 Februari 2022)",
//"link": [
//"https://covid19.go.id/artikel/2022/02/12/situasi-covid-19-di-indonesia-update-12-februari-2022"
//],
//"guid": "https://covid19.go.id/artikel/2022/02/12/situasi-covid-19-di-indonesia-update-12-februari-2022",
//"pubDate": "Sat, 12 Feb 2022 19:45:00 +0700",
//"description": {
//    "__cdata": " <p>Situasi COVID-19 di Indonesia (Update per 12 Februari 2022)</p> "
//},
//"enclosure": {
//    "_url": "https://covid19.go.id/storage/app/uploads/public/620/7ab/d42/6207abd427c6f835861278.jpeg",
//    "_length": "196485",
//    "_type": "image/jpeg"
//}

data class Description (val __cdata: String)

data class Enclosure (val _url: String, val _length: String, val _type: String)

data class NewsModel (val success: Boolean, val message: String, val count_total: Int, val results: ArrayList<Results>) {
    data class Results (val title: String, val link: ArrayList<String>,
                        val guid: String, val pubDate: String,
                        val description: Description, val enclosure: Enclosure)

}