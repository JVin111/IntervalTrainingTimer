package com.vinapp.intervaltrainingtimer.logic.timer

import com.vinapp.intervaltrainingtimer.entities.Timer
import com.vinapp.intervaltrainingtimer.utils.IntervalTimer

class TimerInteractor(private val timer: Timer, private var timerOutput: TimerOutput?): TimerInput {

    private var timerDuration: Int = timer.getDuration()
    private var remainingTime: Int? = null
    private var currentIntervalIndex: Int? = null
    private val intervalTimer: IntervalTimer = object : IntervalTimer(timer) {
        override fun onTick(remainingTime: Long) {
            timerOutput?.provideTime(remainingTime)
        }

        override fun onIntervalEnded(finishedIntervalIndex: Int) {
            if (timer.intervals.size > finishedIntervalIndex + 1) {
                timerOutput?.provideCurrentInterval(timer.intervals[finishedIntervalIndex + 1])
            }
        }

        override fun onRoundEnded(remainingRounds: Int) {
        }

        override fun onFinish() {
        }
    }

    override fun start() {
        timerOutput?.provideCurrentInterval(timer.intervals.first())
        timerOutput?.provideState(TimerState.IN_PROGRESS)
        intervalTimer.start()
    }

    override fun pause() {
        intervalTimer.pause()
    }

    override fun resume() {
        var intervalRemainingTime = timer.intervals[currentIntervalIndex!!]
    }

    override fun stop() {
    }

    override fun restart() {
    }

    override fun registerOutput(output: TimerOutput) {
        this.timerOutput = output
        timerOutput?.provideTime(remainingTime?.toLong() ?: timerDuration.toLong())
    }

    override fun unregisterOutput() {
        this.timerOutput = null
    }
}