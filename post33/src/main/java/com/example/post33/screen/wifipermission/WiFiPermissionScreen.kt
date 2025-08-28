package com.example.post33.screen.wifipermission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.wifi.WifiManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.post33.R
import com.example.post33.components.AppBar
import com.example.post33.navigation.Screen
import com.example.post33.theme.Android13SnippetTheme
import com.google.android.gms.location.LocationServices

@Composable
fun WiFiPermissionScreen(viewModel: WiFiPermissionViewModel = viewModel()) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            tryRequestData(
                context = context,
                onLocationAcquired = { viewModel.setLocation(it) },
                onDevicesAcquired = { viewModel.setDiscoveredDevices(it) }
            )
        }
    }

    LaunchedEffect(Unit) {
        tryRequestData(
            context = context,
            onLocationAcquired = { viewModel.setLocation(it) },
            onDevicesAcquired = { viewModel.setDiscoveredDevices(it) }
        )
    }

    WiFiPermissionScreenContent(
        uiState = state.value,
        displayRequestNearbyDevicesButton =
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU),
        onRequestLocationPermission = {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        },
        onRequestNearbyDevicesPermission = {
            requestPermissionLauncher.launch(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    Manifest.permission.NEARBY_WIFI_DEVICES
                else Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    )
}

@Composable
fun WiFiPermissionScreenContent(
    uiState: WiFiPermissionScreenState,
    displayRequestNearbyDevicesButton: Boolean,
    onRequestLocationPermission: () -> Unit,
    onRequestNearbyDevicesPermission: () -> Unit
) {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.WifiPermission.resourceId)) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(stringResource(id =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        R.string.wifi_permission_hint_new
                    else R.string.wifi_permission_hint_old
                ))

                Button(onClick = onRequestLocationPermission) {
                    Text(stringResource(id = R.string.wifi_permission_request_location))
                }

                if (displayRequestNearbyDevicesButton) {
                    Button(onClick = onRequestNearbyDevicesPermission) {
                        Text(stringResource(id = R.string.wifi_permission_request_nearby_devices))
                    }
                }

                LocationBlock(uiState.location)
                DiscoveredDevicesBlock(uiState.discoveredDevices)
            }
        }
    )
}

@Composable
fun LocationBlock(location: Location?) {
    Column {
        Text(stringResource(id = R.string.wifi_permission_location_title))
        Text(
            text = location?.let {
                "${it.latitude}, ${it.longitude}"
            } ?: stringResource(R.string.wifi_permission_location_unknown)
        )
    }
}

@Composable
fun DiscoveredDevicesBlock(devices: List<String>) {
    Column {
        Text(stringResource(id = R.string.wifi_permission_devices_title))

        if (devices.isEmpty())
            Text(stringResource(id = R.string.wifi_permission_devices_unknown))
        else {
            LazyColumn {
                items(devices) { Text(it) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationBlockPreview() {
    Android13SnippetTheme {
        LocationBlock(null)
    }
}

@Preview(showBackground = true)
@Composable
fun DiscoveredDevicesBlockPreview() {
    Android13SnippetTheme {
        DiscoveredDevicesBlock(
            devices = listOf("Device 1", "Device 2")
        )
    }
}

fun tryRequestData(
    context: Context,
    onLocationAcquired: (Location) -> Unit,
    onDevicesAcquired: (List<String>) -> Unit
) {
    if (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener(onLocationAcquired)
    }

    val devicesPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.NEARBY_WIFI_DEVICES
    else Manifest.permission.ACCESS_FINE_LOCATION

    if (ContextCompat.checkSelfPermission(
        context,
        devicesPermission
    ) == PackageManager.PERMISSION_GRANTED) {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val devices = wifiManager.scanResults
            .map { it.SSID }

        onDevicesAcquired(devices)
    }
}