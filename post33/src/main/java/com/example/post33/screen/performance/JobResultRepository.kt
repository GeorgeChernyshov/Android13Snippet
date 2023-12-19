package com.example.post33.screen.performance

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JobResultRepository {
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job )

    private val _jobResult: MutableStateFlow<String?> = MutableStateFlow(null)
    val jobResult = _jobResult.asStateFlow()

    fun setJobResult(text: String?) = coroutineScope.launch {
        _jobResult.emit(text)
    }
}