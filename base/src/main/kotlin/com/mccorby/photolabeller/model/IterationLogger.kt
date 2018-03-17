package com.mccorby.photolabeller.model

interface IterationLogger {
    fun onIterationDone(message: String)
}