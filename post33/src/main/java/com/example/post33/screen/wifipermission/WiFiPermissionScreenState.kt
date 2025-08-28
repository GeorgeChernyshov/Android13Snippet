package com.example.post33.screen.wifipermission

import android.location.Location

data class WiFiPermissionScreenState(
    val location: Location?,
    val discoveredDevices: List<String>
) {
    companion object {
        val DEFAULT = WiFiPermissionScreenState(
            location = null,
            discoveredDevices = emptyList()
        )
    }
}