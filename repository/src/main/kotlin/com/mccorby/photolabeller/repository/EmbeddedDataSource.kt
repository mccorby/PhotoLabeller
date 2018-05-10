package com.mccorby.photolabeller.repository

import java.io.File

interface EmbeddedDataSource {
    fun getModel(): File
}