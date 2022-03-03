package com.tubes.gapedulidilindungi.models

data class CityModel(val curr_val: String, val message: String, val results: List<CityResults>)

data class CityResults(val key: String, val value: String)
