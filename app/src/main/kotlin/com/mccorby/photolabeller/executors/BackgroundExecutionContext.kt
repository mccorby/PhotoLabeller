package com.mccorby.photolabeller.executors

import com.mccorby.executors.ExecutionContext
import kotlinx.coroutines.experimental.CommonPool

class BackgroundExecutionContext: ExecutionContext {

    override fun getContext() = CommonPool
}