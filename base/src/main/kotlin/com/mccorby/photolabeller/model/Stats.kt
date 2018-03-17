package com.mccorby.photolabeller.model

data class Stats(val summary: String, val selectedResult: Int = -1, val scores: List<Double> = arrayListOf())