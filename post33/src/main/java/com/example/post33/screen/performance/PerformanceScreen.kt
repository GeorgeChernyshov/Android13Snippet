package com.example.post33.screen.performance

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.post33.components.AppBar
import com.example.post33.navigation.Screen
import com.example.post33.R
import com.example.post33.viewmodel.PerformanceViewModel

@Composable
fun PerformanceScreen(
    viewModel: PerformanceViewModel = viewModel(),
    onNextClicked: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    
    Scaffold(
        topBar = { AppBar(name = stringResource(id = Screen.Performance.resourceId)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    Text(text = stringResource(id = R.string.performance_check_active_apps))

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
fun JobResultBlock(
    modifier: Modifier = Modifier,
    result: String?
) {
    Column(modifier) {
        Text(text = stringResource(id = R.string.performance_prefetch))

        if (result != null) {
            Text(text = result)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JobResultBlockPreview() {
    JobResultBlock(result = "Test")
}