package com.example.post33.screen.performance

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.post33.components.AppBar
import com.example.post33.navigation.Screen
import com.example.post33.R
import com.example.post33.screen.performance.SimpleForegroundService.ActionValues.START
import com.example.post33.screen.performance.SimpleForegroundService.ActionValues.STOP
import com.example.post33.theme.Android13SnippetTheme
import com.example.post33.viewmodel.PerformanceViewModel
import kotlinx.coroutines.launch

@Composable
fun PerformanceScreen(
    viewModel: PerformanceViewModel = viewModel(),
    onNextClicked: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val serviceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(
                name: ComponentName?,
                service: IBinder?
            ) {
                val binder = service as SimpleForegroundService.SimpleForegroundServiceBinder
                coroutineScope.launch {
                    binder.state.collect { state ->
                        viewModel.setServiceActive(state)
                    }
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                viewModel.setServiceActive(SimpleForegroundService.ServiceState.STOPPED)
            }
        }
    }

    val permissionRequestLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            startSimpleForegroundService(
                context = context,
                isServiceBound = state.value.isServiceBound,
                setServiceBound = { viewModel.setServiceBound(it) },
                serviceConnection = serviceConnection
            )
        }
    }

    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Performance.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    TaskManagerBlock(
                        serviceActive = state.value.serviceActive,
                        onStartServiceClicked = {
                            tryStartSimpleForegroundService(
                                context = context,
                                isServiceBound = state.value.isServiceBound,
                                setServiceBound = { viewModel.setServiceBound(it) },
                                serviceConnection = serviceConnection,
                                requestPermissionLauncher = permissionRequestLauncher
                            )
                        },
                        onStopServiceClicked = {
                            stopSimpleForegroundService(context)
                        }
                    )
                }

                JobResultBlock(
                    modifier = Modifier.padding(top = 16.dp),
                    result = state.value.jobResult
                )

                Button(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = { onNextClicked.invoke() }
                ) {
                    Text(text = stringResource(id = R.string.button_go_next))
                }
            }
        }
    )
}

@Composable
fun TaskManagerBlock(
    modifier: Modifier = Modifier,
    serviceActive: Boolean,
    onStartServiceClicked: () -> Unit,
    onStopServiceClicked: () -> Unit
) {
    Column(modifier) {
        Text(text = stringResource(id = R.string.performance_check_active_apps))

        if (serviceActive) {
            Button(onClick = onStopServiceClicked) {
                Text(stringResource(
                    R.string.performance_stop_service)
                )
            }
        } else {
            Button(onClick = onStartServiceClicked) {
                Text(stringResource(
                    R.string.performance_start_service)
                )
            }
        }
    }
}

@Composable
fun JobResultBlock(
    modifier: Modifier = Modifier,
    result: String?
) {
    Column(modifier) {
        Text(text = stringResource(id = R.string.performance_prefetch_description))
//        Text(text = stringResource(id = R.string.performance_prefetch))
//
//        if (result != null) {
//            Text(text = result)
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskManagerBlockPreview() {
    Android13SnippetTheme {
        TaskManagerBlock(
            serviceActive = false,
            onStartServiceClicked = {},
            onStopServiceClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JobResultBlockPreview() {
    Android13SnippetTheme {
        JobResultBlock(result = "Test")
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun tryStartSimpleForegroundService(
    context: Context,
    isServiceBound: Boolean,
    setServiceBound: (Boolean) -> Unit,
    serviceConnection: ServiceConnection,
    requestPermissionLauncher: ActivityResultLauncher<Array<String>>
) {
    val requiredPermissions = listOf(
        android.Manifest.permission.POST_NOTIFICATIONS,
        android.Manifest.permission.FOREGROUND_SERVICE
    )

    val permissionsToRequest = requiredPermissions.filter { permission ->
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    }

    if (permissionsToRequest.isEmpty()) {
        startSimpleForegroundService(
            context = context,
            isServiceBound = isServiceBound,
            setServiceBound = setServiceBound,
            serviceConnection = serviceConnection
        )
    } else {
        requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
    }
}

fun startSimpleForegroundService(
    context: Context,
    isServiceBound: Boolean,
    setServiceBound: (Boolean) -> Unit,
    serviceConnection: ServiceConnection
) {
    val intent = Intent(context, SimpleForegroundService::class.java)
    intent.action = START.actionName

    ContextCompat.startForegroundService(context, intent)

    if (!isServiceBound) {
        bindSimpleForegroundService(
            context = context,
            serviceConnection = serviceConnection
        )

        setServiceBound(true)
    }
}

fun stopSimpleForegroundService(context: Context) {
    val intent = Intent(context, SimpleForegroundService::class.java)
    intent.action = STOP.actionName

    ContextCompat.startForegroundService(context, intent)
}

fun bindSimpleForegroundService(
    context: Context,
    serviceConnection: ServiceConnection
) {
    val intent = Intent(context, SimpleForegroundService::class.java)
    context.bindService(
        intent,
        serviceConnection,
        Context.BIND_AUTO_CREATE
    )
}