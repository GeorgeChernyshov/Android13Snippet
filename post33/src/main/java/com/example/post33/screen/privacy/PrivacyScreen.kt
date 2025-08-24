package com.example.post33.screen.privacy

import android.Manifest
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.os.Build
import android.os.PersistableBundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.example.post33.components.AppBar
import com.example.post33.navigation.Screen
import com.example.post33.R

@Composable
fun PrivacyScreen(onNextClicked: () -> Unit) {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Privacy.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                NotificationPermissionBlock()
                ClipDataBlock(Modifier.padding(top = 16.dp))

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
fun NotificationPermissionBlock(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {}

    Column(modifier) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Text(stringResource(R.string.privacy_notification_permission_hint))

            Button(onClick = { 
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) 
            }) {
                Text(text = stringResource(id = R.string.privacy_grant_permission))
            }
            
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    context.revokeSelfPermissionOnKill(Manifest.permission.POST_NOTIFICATIONS)
                }
            ) {
                Text(text = stringResource(id = R.string.privacy_revoke_permission))
            }

            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                text = stringResource(id = R.string.privacy_revoke_hint)
            )
        }
        
        Button(onClick = {
            NotificationHelper(context).showNotification()
        }) {
            Text(text = stringResource(id = R.string.privacy_send_notification))
        }
    }
}

@Composable
fun ClipDataBlock(modifier: Modifier = Modifier) {
    val clipboardManager = LocalContext.current.getSystemService(ClipboardManager::class.java)

    var textToCopy by remember { mutableStateOf("") }
    var textToPaste by remember { mutableStateOf("") }

    Column(modifier) {
        Text(text = stringResource(id = R.string.privacy_clip_data_copy))

        TextField(
            modifier = Modifier.padding(top = 16.dp),
            value = textToCopy,
            onValueChange = { textToCopy = it }
        )

        Button(onClick = {
            val clipData = ClipData.newPlainText(textToCopy.take(3), textToCopy).apply {
                description.extras = PersistableBundle().apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                    else putBoolean("android.content.extra.IS_SENSITIVE", true)
                }
            }

            clipboardManager.setPrimaryClip(clipData)
        }) {
            Text(text = stringResource(id = R.string.privacy_clip_data_copy_button))
        }

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.privacy_clip_data_paste)
        )

        TextField(
            modifier = Modifier.padding(top = 16.dp),
            value = textToPaste,
            onValueChange = { textToPaste = it }
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(
                id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    R.string.privacy_clip_data_hint_hidden
                else R.string.privacy_clip_data_hint_visible
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationPermissionBlockPreview() {
    NotificationPermissionBlock()
}

@Preview(showBackground = true)
@Composable
fun ClipDataBlockPreview() {
    ClipDataBlock()
}