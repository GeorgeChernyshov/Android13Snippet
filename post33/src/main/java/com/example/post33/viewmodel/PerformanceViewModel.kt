package com.example.post33.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.post33.screen.performance.DIUtils
import com.example.post33.uistate.PerformanceScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PerformanceViewModel : ViewModel() {

    private val _state = MutableStateFlow(PerformanceScreenUiState.DEFAULT)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            DIUtils.jobResultRepository.jobResult.collect {
                _state.emit(state.value.copy(jobResult = it))
            }
        }
    }
}