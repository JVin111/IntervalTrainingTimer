package com.vinapp.intervaltrainingtimer.ui.timer_editor_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vinapp.intervaltrainingtimer.R
import com.vinapp.intervaltrainingtimer.common.IntervalColor
import com.vinapp.intervaltrainingtimer.ui_components.TimeDigits
import com.vinapp.intervaltrainingtimer.ui_components.TimeText
import com.vinapp.intervaltrainingtimer.ui_components.TopBar
import com.vinapp.intervaltrainingtimer.ui_components.add_item.AddItem
import com.vinapp.intervaltrainingtimer.ui_components.interval_item.IntervalItem
import com.vinapp.intervaltrainingtimer.ui_components.interval_item.IntervalItemData
import com.vinapp.intervaltrainingtimer.ui_components.name_text_field.NameTextField
import com.vinapp.intervaltrainingtimer.ui_components.round_picker.TimePicker
import com.vinapp.intervaltrainingtimer.ui_components.theme.AppTheme

@Composable
fun TimerEditorScreen(
    timerId: String?
) {
    val viewModel: TimerEditorViewModel = viewModel(
        factory = TimerEditorViewModel.Factory
    )
    val state by viewModel.screenStateFlow.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.loadTimerById(timerId)
    }

    TimerEditorScreenContent(
        state = state,
        onIncreaseRoundsClick = {
            focusManager.clearFocus()
            viewModel.onIncreaseRoundsClick()
        },
        onDecreaseRoundsClick = {
            focusManager.clearFocus()
            viewModel.onDecreaseRoundsClick()
        },
        onIncreaseStartDelayClick = {
            focusManager.clearFocus()
            viewModel.onIncreaseStartDelayClick()
        },
        onDecreaseStartDelayClick = {
            focusManager.clearFocus()
            viewModel.onDecreaseStartDelayClick()
        },
        onIncreaseTimeBetweenRoundsClick = {
            focusManager.clearFocus()
            viewModel.onIncreaseTimeBetweenRoundsClick()
        },
        onDecreaseTimeBetweenRoundsClick = {
            focusManager.clearFocus()
            viewModel.onDecreaseTimeBetweenRoundsClick()
        },
        onIntervalClick = {
            focusManager.clearFocus()
            viewModel.onIntervalClick(it)
        },
        onAddIntervalClick = {
            focusManager.clearFocus()
            viewModel.onAddIntervalClick()
        },
        onIntervalDialogConfirmClick = { name, duration, color ->
            focusManager.clearFocus()
            viewModel.onSaveIntervalClick(name, duration, color)
        },
        onIntervalDialogCancelClick = {
            focusManager.clearFocus()
            viewModel.closeDialog()
        },
    )
}

@Composable
private fun TimerEditorScreenContent(
    state: TimerEditorScreenState,
    onIncreaseRoundsClick: () -> Unit,
    onDecreaseRoundsClick: () -> Unit,
    onIncreaseStartDelayClick: () -> Unit,
    onDecreaseStartDelayClick: () -> Unit,
    onIncreaseTimeBetweenRoundsClick: () -> Unit,
    onDecreaseTimeBetweenRoundsClick: () -> Unit,
    onIntervalClick: (index: Int) -> Unit,
    onAddIntervalClick: () -> Unit,
    onIntervalDialogConfirmClick: (name: String, duration: Long, color: IntervalColor) -> Unit,
    onIntervalDialogCancelClick: () -> Unit,
) {
    Scaffold(
        backgroundColor = AppTheme.colors.darkGray,
        topBar = {
            TopBar(
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                    ),
                content = {
                    NameTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = "",
                        isError = false,
                        placeholderText = stringResource(R.string.timerName),
                        onValueChange = {}
                    )
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 14.dp,
                        top = 10.dp,
                        end = 10.dp,
                        bottom = 16.dp,
                    ),
            ) {
                TotalTimeRow(
                    timeDigits = state.totalTimeDigits
                )
                NumberOfRoundsRow(
                    numberOfRounds = state.numberOfRounds,
                    onIncreaseClick = onIncreaseRoundsClick,
                    onDecreaseClick = onDecreaseRoundsClick
                )
                StartDelayRow(
                    startDelay = state.startDelay,
                    onIncreaseClick = onIncreaseStartDelayClick,
                    onDecreaseClick = onDecreaseStartDelayClick
                )
                TimeBetweenRoundsRow(
                    timeBetweenRounds = state.timeBetweenRounds,
                    onIncreaseClick = onIncreaseTimeBetweenRoundsClick,
                    onDecreaseClick = onDecreaseTimeBetweenRoundsClick
                )
            }
            Divider(
                color = AppTheme.colors.mediumGray
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(AppTheme.colors.lightGray),
                contentPadding = PaddingValues(
                    horizontal = 14.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.intervalList?.let { intervals ->
                    itemsIndexed(intervals) { index, interval ->
                        IntervalItem(
                            intervalItemData = interval,
                            onClick = {
                                onIntervalClick(index)
                            }
                        )
                    }
                }
                item {
                    AddItem(
                        onClick = onAddIntervalClick
                    )
                }
            }
        }
        if (state.showIntervalDialog) {
            IntervalDialog(
                interval = state.selectedInterval,
                onConfirmClick = onIntervalDialogConfirmClick,
                onCancelClick = onIntervalDialogCancelClick
            )
        }
    }
}

@Composable
private fun SettingsRow(
    title: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1F),
            text = "$title:",
            textAlign = TextAlign.End,
            style = AppTheme.typography.title.copy(
                color = AppTheme.colors.grayFontColor
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8F),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
private fun TotalTimeRow(
    timeDigits: TimeDigits,
) {
    SettingsRow(
        title = stringResource(R.string.totalTimeText)
    ) {
        TimeText(
            timeDigits = timeDigits,
            forceColored = true
        )
    }
}

@Composable
private fun NumberOfRoundsRow(
    numberOfRounds: Int,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
) {
    SettingsRow(
        title = stringResource(R.string.numberOfRoundsText)
    ) {
        TimePicker(
            value = numberOfRounds,
            onIncreaseClick = onIncreaseClick,
            onDecreaseClick = onDecreaseClick
        )
    }
}

@Composable
private fun StartDelayRow(
    startDelay: Int,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
) {
    SettingsRow(
        title = stringResource(R.string.delayBeforeStart)
    ) {
        TimePicker(
            value = startDelay,
            onIncreaseClick = onIncreaseClick,
            onDecreaseClick = onDecreaseClick
        )
    }
}

@Composable
private fun TimeBetweenRoundsRow(
    timeBetweenRounds: Int,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
) {
    SettingsRow(
        title = stringResource(R.string.timeBetweenRounds)
    ) {
        TimePicker(
            value = timeBetweenRounds,
            onIncreaseClick = onIncreaseClick,
            onDecreaseClick = onDecreaseClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimerEditorScreenPreview() {
    AppTheme {
        TimerEditorScreenContent(
            state = TimerEditorScreenState(
                timerName = "Timer name",
                intervalList = listOf(
                    IntervalItemData(
                        id = 0,
                        name = "Interval name",
                        duration = "15:00",
                        color = IntervalColor.RED
                    ),
                    IntervalItemData(
                        id = 0,
                        name = "Interval name",
                        duration = "15:00",
                        color = IntervalColor.RED
                    )
                ),
                numberOfRounds = 4
            ),
            onIncreaseRoundsClick = {},
            onDecreaseRoundsClick = {},
            onIncreaseStartDelayClick = {},
            onDecreaseStartDelayClick = {},
            onIncreaseTimeBetweenRoundsClick = {},
            onDecreaseTimeBetweenRoundsClick = {},
            onIntervalClick = {},
            onAddIntervalClick = {},
            onIntervalDialogConfirmClick = { _, _, _ -> },
            onIntervalDialogCancelClick = {},
        )
    }
}
