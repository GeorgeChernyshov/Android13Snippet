package com.example.pre33.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pre33.theme.Android13SnippetTheme

@Composable
fun AppBar(name: String, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h4,
            text = name,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    Android13SnippetTheme {
        AppBar("Title")
    }
}