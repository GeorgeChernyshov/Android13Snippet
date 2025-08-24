package com.example.post33.uistate

data class PerformanceScreenUiState(
    val serviceActive: Boolean,
    val jobResult: String?,
    val isServiceBound: Boolean
) {
    companion object {
        val DEFAULT = PerformanceScreenUiState(
            serviceActive = false,
            jobResult = null,
            isServiceBound = false
        )
    }
}