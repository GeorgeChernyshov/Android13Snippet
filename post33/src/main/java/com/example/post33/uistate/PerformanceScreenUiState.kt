package com.example.post33.uistate

data class PerformanceScreenUiState(
    val jobResult: String?
) {
    companion object {
        val DEFAULT = PerformanceScreenUiState(null)
    }
}