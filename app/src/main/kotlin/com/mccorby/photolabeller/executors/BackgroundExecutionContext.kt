package com.mccorby.photolabeller.executors

import com.mccorby.executors.ExecutionContext
import kotlinx.coroutines.Dispatchers

class BackgroundExecutionContext: ExecutionContext {

    override fun getContext() = Dispatchers.Default
}