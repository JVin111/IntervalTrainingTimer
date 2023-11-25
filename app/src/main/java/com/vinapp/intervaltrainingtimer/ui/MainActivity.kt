package com.vinapp.intervaltrainingtimer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vinapp.intervaltrainingtimer.ui.timer_editor_screen.TimerEditorScreen
import com.vinapp.intervaltrainingtimer.ui.timer_list_screen.TimerListScreen
import com.vinapp.intervaltrainingtimer.ui_components.theme.AppTheme

class MainActivity: ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.TimerListScreen.route,
                ) {
                    composable(
                        route = Screen.TimerListScreen.route
                    ) {
                        TimerListScreen(
                            navigateToTimerEditorScreen = {
                                navController.navigate(
                                    route = Screen.TimerEditorScreen.route
                                )
                            }
                        )
                    }
                    composable(
                        route = Screen.TimerEditorScreen.route
                    ) {
                        TimerEditorScreen(
                            timerId = null
                        )
                    }
                }
            }
        }
    }
}