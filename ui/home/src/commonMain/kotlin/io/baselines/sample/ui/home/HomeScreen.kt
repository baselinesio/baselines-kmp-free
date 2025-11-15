package io.baselines.sample.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.baselines.sample.ui.designsystem.theme.AppTheme

@Composable
fun HomeScreen(
    onOpenPlaygroundClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            AppTheme.spacings.elementMedium,
            Alignment.CenterVertically,
        )
    ) {
        Text("{ Home }")
        Button(onClick = onOpenPlaygroundClicked) {
            Text("Go to Playground")
        }
    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    AppTheme {
        HomeScreen(
            onOpenPlaygroundClicked = { /* no-op */ },
        )
    }
}
