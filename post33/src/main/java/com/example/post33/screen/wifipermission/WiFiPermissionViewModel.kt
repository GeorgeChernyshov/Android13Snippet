package com.example.post33.screen.wifipermission

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WiFiPermissionViewModel : ViewModel() {

    private val _state = MutableStateFlow(WiFiPermissionScreenState.DEFAULT)
    val state = _state.asStateFlow()

    fun setLocation(location: Location) = viewModelScope.launch {
        _state.emit(state.value.copy(location = location))
    }

    fun setDiscoveredDevices(devices: List<String>) = viewModelScope.launch {
        _state.emit(state.value.copy(discoveredDevices = devices))
    }

    fun addDiscoveredDevice(device: String) = viewModelScope.launch {
        _state.emit(state.value.copy(
            discoveredDevices = state.value.discoveredDevices + device
        ))
    }
}