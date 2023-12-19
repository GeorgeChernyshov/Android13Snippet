package com.example.post33.screen.speech

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.post33.components.AppBar
import com.example.post33.navigation.Screen

@Composable
fun SpeechScreen() {
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Speech.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                SpeechServiceBlock()
            }
        }
    )
}

@Composable
fun SpeechServiceBlock() {}

@Preview(showBackground = true)
@Composable
fun SpeechServiceBlockPreview() {
    SpeechServiceBlock()
}