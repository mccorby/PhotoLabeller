package com.mccorby.executors

import kotlinx.coroutines.experimental.CoroutineDispatcher

interface ExecutionContext {
    fun getContext(): CoroutineDispatcher
}