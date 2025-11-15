package io.baselines.ui.playground.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import io.baselines.sample.ui.designsystem.components.scrollbar.LazyColumnScrollbar
import io.baselines.sample.ui.designsystem.loading.LoadingStateUm
import io.baselines.sample.ui.designsystem.theme.AppTheme
import io.baselines.ui.playground.SectionFactory
import io.baselines.ui.playground.spacings.SpacingsSectionFactory
import io.baselines.ui.playground.typography.TypographySectionFactory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PlaygroundScreen(
    appVersion: String,
    loading: LoadingStateUm?,
    sectionFactories: ImmutableList<SectionFactory>,
    searchInput: String,
    onSearchInputChanged: (String) -> Unit,
) {
    LazyColumnScrollbar { state ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colorScheme.surface),
            contentPadding = WindowInsets.systemBars.asPaddingValues(),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacings.elementMedium),
            state = state,
        ) {
            item {
                Column(
                    modifier = Modifier
                        .padding(vertical = AppTheme.spacings.layoutMedium),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacings.elementMedium),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1F)
                                .padding(horizontal = AppTheme.spacings.layoutMedium),
                            text = "Playground",
                            style = AppTheme.typography.headlineMedium,
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = AppTheme.spacings.layoutMedium),
                            text = "app v$appVersion",
                            style = AppTheme.typography.bodyMedium,
                        )
                    }
                    AnimatedVisibility(visible = loading != null) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = AppTheme.spacings.layoutMedium),
                        value = searchInput,
                        singleLine = true,
                        textStyle = AppTheme.typography.bodyMedium,
                        placeholder = {
                            Text(
                                text = "Search...",
                                style = AppTheme.typography.bodyMedium,
                            )
                        },
                        trailingIcon = {
                            TextButton(
                                modifier = Modifier.padding(
                                    horizontal = AppTheme.spacings.elementMedium
                                ),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = AppTheme.colorScheme.surfaceContainer,
                                ),
                                onClick = { onSearchInputChanged("") },
                                contentPadding = PaddingValues()
                            ) {
                                Text(
                                    text = "\uD83D\uDDD1\uFE0F",
                                    textAlign = TextAlign.Center,
                                )
                            }
                        },
                        shape = AppTheme.shapes.roundMedium,
                        onValueChange = onSearchInputChanged,
                    )
                }
            }

            items(sectionFactories) { sectionFactory ->
                with(sectionFactory) { Create() }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewPlaygroundScreen() {
    AppTheme {
        PlaygroundScreen(
            appVersion = "1.0.1",
            loading = null,
            sectionFactories = persistentListOf(
                SpacingsSectionFactory(),
                TypographySectionFactory(),
            ),
            searchInput = "",
            onSearchInputChanged = { /* no-op */ }
        )
    }
}
