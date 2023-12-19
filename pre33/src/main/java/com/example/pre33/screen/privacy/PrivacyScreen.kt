package com.example.pre33.screen.privacy

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pre33.R
import com.example.pre33.components.AppBar
import com.example.pre33.navigation.Screen
import com.example.pre33.preferences.ApplicationPreferences

@Composable
fun PrivacyScreen() {
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
            }
        }
    )
}

@Composable
fun NotificationPermissionBlock(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val preferences = ApplicationPreferences(
        context.getSharedPreferences(null, Context.MODE_PRIVATE)
    )

    Button(onClick = {
        preferences.notifications.putApply(true)
    }) {
        Text(text = stringResource(id = R.string.privacy_grant_permission))
    }

    Button(
        modifier = Modifier.padding(top = 16.dp),
        onClick = {
            preferences.notifications.putApply(false)
        }
    ) {
        Text(text = stringResource(id = R.string.privacy_revoke_permission))
    }

    Button(onClick = {
        if (preferences.notifications.get()) {
            NotificationHelper(context).showNotification()
        }
    }) {
        Text(text = stringResource(id = R.string.privacy_send_notification))
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationPermissionBlockPreview() {
    NotificationPermissionBlock()
}