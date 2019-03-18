package com.mccorby.executors

import kotlinx.coroutines.CoroutineDispatcher

interface ExecutionContext {
    fun getContext(): CoroutineDispatcher
}