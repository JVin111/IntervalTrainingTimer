package com.vinapp.intervaltrainingtimer.ui.timer

import com.vinapp.intervaltrainingtimer.entities.Interval
import com.vinapp.intervaltrainingtimer.logic.timer.TimerInput
import com.vinapp.intervaltrainingtimer.logic.timer.TimerOutput
import com.vinapp.intervaltrainingtimer.logic.timer.TimerState
import com.vinapp.intervaltrainingtimer.mvp.TimerContract

class TimerPresenter(private val timerInput: TimerInput): TimerContract.Presenter(), TimerOutput {

    var timerState: TimerState = TimerState.STOPPED

    override fun onTimerActionButtonClick() {
        when (timerState) {
            TimerState.STOPPED -> timerInput.start()
            TimerState.PAUSED -> timerInput.resume()
            TimerState.IN_PROGRESS -> timerInput.pause()
            TimerState.FINISHED -> timerInput.restart()
        }
    }

    override fun destroy() {
    }

    override fun attachView(view: TimerContract.View) {
        super.attachView(view)
        timerInput.registerOutput(this)
    }

    override fun detachView() {
        super.detachView()
        timerInput.unregisterOutput()
    }

    override fun provideState(state: TimerState) {
        timerState = state
        if (state == TimerState.FINISHED) {
            view!!.showTime("")
            view!!.setDefaultColor()
        }
    }

    override fun provideTime(time: Long) {
        view!!.showTime((time / 1000 + 1).toString())
    }

    override fun provideCurrentInterval(interval: Interval) {
        view!!.setColorByIntervalType(interval.type)
    }
}