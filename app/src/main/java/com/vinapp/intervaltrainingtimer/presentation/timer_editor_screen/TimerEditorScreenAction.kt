package com.vinapp.intervaltrainingtimer.presentation.timer_editor_screen

import com.vinapp.intervaltrainingtimer.base.presentation.ScreenAction

sealed interface TimerEditorScreenAction : ScreenAction {
    data class NavigateToTimerScreen(
        val timerId: String
    ) : TimerEditorScreenAction
    object NavigateBack : TimerEditorScreenAction
    object ShowDurationErrorSnackbar : TimerEditorScreenAction
}