package com.vinapp.intervaltrainingtimer.presentation.timer_editor_screen

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vinapp.intervaltrainingtimer.App
import com.vinapp.intervaltrainingtimer.base.presentation.BaseViewModel
import com.vinapp.intervaltrainingtimer.common.IntervalColor
import com.vinapp.intervaltrainingtimer.data.source.timer.TimerRepository
import com.vinapp.intervaltrainingtimer.domain.entities.Interval
import com.vinapp.intervaltrainingtimer.domain.entities.Timer
import com.vinapp.intervaltrainingtimer.mapping.IntervalMapper.mapIntervalToIntervalItemData
import com.vinapp.intervaltrainingtimer.utils.TimeConverter
import com.vinapp.intervaltrainingtimer.presentation.timer_editor_screen.TimerEditorScreenAction.NavigateToTimerScreen
import com.vinapp.intervaltrainingtimer.presentation.timer_editor_screen.TimerEditorScreenAction.NavigateBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

private const val MIN_NUMBER_OF_ROUNDS = 1
private const val MAX_NUMBER_OF_ROUNDS = 100
private const val MIN_START_DELAY = 0
private const val MAX_START_DELAY = 60
private const val MIN_TIME_BETWEEN_ROUNDS = 0

class TimerEditorScreenViewModel(
    val timerRepository: TimerRepository,
) : BaseViewModel<TimerEditorScreenState, TimerEditorScreenAction>() {

    override val mutableScreenStateFlow = MutableStateFlow(TimerEditorScreenState())
    override val mutableScreenActionChannel = Channel<TimerEditorScreenAction>()

    private var timer: Timer? = null
    private var intervalList: MutableList<Interval> = mutableListOf()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as App
                TimerEditorScreenViewModel(
                    timerRepository = app.container.timerRepository
                )
            }
        }
    }

    fun loadTimerById(timerId: String?) {
        if (timerId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                timer = timerRepository.getTimerById(timerId)
                timer?.let {
                    intervalList = it.intervalList.toMutableList()
                    updateState(
                        currentScreenState.copy(
                            timerName = it.name,
                            totalTimeDigits = TimeConverter.getTimeDigits(
                                numberOfRounds = it.numberOfRounds,
                                intervalList = it.intervalList,
                                timeBetweenRounds = it.timeBetweenRounds
                            ),
                            intervalList = it.intervalList.map(::mapIntervalToIntervalItemData),
                            numberOfRounds = it.numberOfRounds,
                            startDelay = it.startDelay,
                            timeBetweenRounds = it.timeBetweenRounds,
                            showDeleteButton = true,
                        )
                    )
                }
            }
        }
    }

    fun onTimerNameChanged(name: String) {
        updateState(
            currentScreenState.copy(
                timerName = name
            )
        )
    }

    fun onIncreaseRoundsClick() {
        var numberOfRounds = currentScreenState.numberOfRounds + 1
        if (numberOfRounds > MAX_NUMBER_OF_ROUNDS) {
            numberOfRounds = MIN_NUMBER_OF_ROUNDS
        }
        updateState(
            currentScreenState.copy(
                totalTimeDigits = TimeConverter.getTimeDigits(
                    numberOfRounds = numberOfRounds,
                    intervalList = intervalList,
                    timeBetweenRounds = currentScreenState.timeBetweenRounds
                ),
                numberOfRounds = numberOfRounds,
            )
        )
    }

    fun onDecreaseRoundsClick() {
        var numberOfRounds = currentScreenState.numberOfRounds - 1
        if (numberOfRounds < MIN_NUMBER_OF_ROUNDS) {
            numberOfRounds = MAX_NUMBER_OF_ROUNDS
        }
        updateState(
            currentScreenState.copy(
                totalTimeDigits = TimeConverter.getTimeDigits(
                    numberOfRounds = numberOfRounds,
                    intervalList = intervalList,
                    timeBetweenRounds = currentScreenState.timeBetweenRounds
                ),
                numberOfRounds = numberOfRounds,
            )
        )
    }

    fun onIncreaseStartDelayClick() {
        var startDelay = currentScreenState.startDelay + 1
        if (startDelay > MAX_START_DELAY) {
            startDelay = MIN_START_DELAY
        }
        updateState(
            currentScreenState.copy(
                startDelay = startDelay
            )
        )
    }

    fun onDecreaseStartDelayClick() {
        var startDelay = currentScreenState.startDelay - 1
        if (startDelay < MIN_START_DELAY) {
            startDelay = MAX_START_DELAY
        }
        updateState(
            currentScreenState.copy(
                startDelay = startDelay
            )
        )
    }

    fun onIncreaseTimeBetweenRoundsClick() {
        var timeBetweenRounds = currentScreenState.timeBetweenRounds + 1
        updateState(
            currentScreenState.copy(
                totalTimeDigits = TimeConverter.getTimeDigits(
                    numberOfRounds = currentScreenState.numberOfRounds,
                    intervalList = intervalList,
                    timeBetweenRounds = timeBetweenRounds,
                ),
                timeBetweenRounds = timeBetweenRounds,
            )
        )
    }

    fun onDecreaseTimeBetweenRoundsClick() {
        var timeBetweenRounds = currentScreenState.timeBetweenRounds - 1
        if (timeBetweenRounds >= MIN_TIME_BETWEEN_ROUNDS) {
            updateState(
                currentScreenState.copy(
                    totalTimeDigits = TimeConverter.getTimeDigits(
                        numberOfRounds = currentScreenState.numberOfRounds,
                        intervalList = intervalList,
                        timeBetweenRounds = timeBetweenRounds,
                    ),
                    timeBetweenRounds = timeBetweenRounds,
                )
            )
        }
    }

    fun onIntervalClick(index: Int) {
        updateState(
            currentScreenState.copy(
                selectedInterval = intervalList[index],
                showIntervalDialog = true
            )
        )
    }

    fun onAddIntervalClick() {
        updateState(
            currentScreenState.copy(
                selectedInterval = null,
                showIntervalDialog = true
            )
        )
    }

    fun onSaveIntervalClick(name: String, duration: Long, color: IntervalColor) {
        val selectedInterval = currentScreenState.selectedInterval
        if (selectedInterval != null) {
            intervalList[intervalList.indexOf(selectedInterval)] = selectedInterval.copy(
                name = name,
                durationInSeconds = duration,
                color = color
            )
        } else {
            intervalList.add(
                Interval(
                    id = getNewIntervalId(),
                    timerId = "",
                    name = name,
                    durationInSeconds = duration,
                    color = color,
                )
            )
        }
        updateState(
            currentScreenState.copy(
                totalTimeDigits = TimeConverter.getTimeDigits(
                    numberOfRounds = currentScreenState.numberOfRounds,
                    intervalList = intervalList,
                    timeBetweenRounds = currentScreenState.timeBetweenRounds
                ),
                intervalList = intervalList.map(::mapIntervalToIntervalItemData),
                showIntervalDialog = false
            )
        )
    }

    fun closeDialog() {
        updateState(
            currentScreenState.copy(
                selectedInterval = null,
                showIntervalDialog = false
            )
        )
    }

    fun onDeleteTimerClick() {
        timer?.let { timer ->
            viewModelScope.launch(Dispatchers.IO) {
                timerRepository.deleteTimer(timer.id)
            }
            sendAction(NavigateBack)
        }
    }

    fun onStartTimerClick() {
        validateTimerData(
            onValid = { timer ->
                saveTimer(timer)
                sendAction(
                    NavigateToTimerScreen(timerId = timer.id)
                )
            },
        )
    }

    fun onSaveTimerClick() {
        validateTimerData(
            onValid = { timer ->
                saveTimer(timer)
            },
        )
    }

    private fun getNewIntervalId(): Int {
        return intervalList.maxByOrNull { it.id }?.let {
            it.id + 1
        } ?: 0
    }

    private fun saveTimer(timer: Timer) {
        this.timer = timer
        viewModelScope.launch(Dispatchers.IO) {
            timerRepository.saveTimer(timer)
        }
        sendAction(NavigateBack)
    }

    private fun validateTimerData(
        onValid: (timer: Timer) -> Unit,
    ) {
        val name = currentScreenState.timerName ?: ""
        val isNameValid = name.isNotBlank()
        val isDurationValid = TimeConverter.getTimeInSeconds(
            numberOfRounds = currentScreenState.numberOfRounds,
            intervalList = intervalList,
            timeBetweenRounds = currentScreenState.timeBetweenRounds,
        ) > 0
        if (isNameValid && isDurationValid) {
            onValid(buildTimer(name))
        } else {
            updateState(
                currentScreenState.copy(
                    isTimerNameError = !isNameValid,
                    isDurationError = !isDurationValid
                )
            )
        }
    }

    private fun buildTimer(timerName: String): Timer {
        val timerId = timer?.id ?: UUID.randomUUID().toString()
        return Timer(
            id = timerId,
            name = timerName,
            intervalList = intervalList.map {
                it.copy(timerId = timerId)
            },
            numberOfRounds = currentScreenState.numberOfRounds,
            startDelay = currentScreenState.startDelay,
            timeBetweenRounds = currentScreenState.timeBetweenRounds,
            createdTime = timer?.createdTime ?: 0L,
            updatedTime = 0L,
        )
    }
}