package com.mccorby.photolabeller.executors

import com.mccorby.executors.ExecutionContext
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineDispatcher

class BackgroundExecutionContext: ExecutionContext {

    override fun getContext(): CoroutineDispatcher = CommonPool
}