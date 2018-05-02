package com.mccorby.photolabeller.executors

import com.mccorby.executors.ExecutionContext
import kotlinx.coroutines.experimental.android.UI


class UIExecutionContext: ExecutionContext {

    override fun getContext() = UI
}