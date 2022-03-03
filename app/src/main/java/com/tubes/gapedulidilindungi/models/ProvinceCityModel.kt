package com.tubes.gapedulidilindungi.models

data class ProvinceCityModel(
    val curr_val: String,
    val message: String,
    val results: List<ProvinceCityResults>
)

data class ProvinceCityResults(val key: String, val value: String)
