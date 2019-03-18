package com.mccorby.photolabeller.executors

import com.mccorby.executors.ExecutionContext
import kotlinx.coroutines.Dispatchers

class UIExecutionContext : ExecutionContext {
    override fun getContext() = Dispatchers.Main
}