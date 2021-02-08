package com.vinapp.intervaltrainingtimer.mvp

import com.vinapp.intervaltrainingtimer.entities.Timer
import com.vinapp.intervaltrainingtimer.mvp.model.TimerModel
import com.vinapp.intervaltrainingtimer.mvp.presenter.sections.SectionPresenter
import com.vinapp.intervaltrainingtimer.mvp.view.sections.SectionView

interface TimerListContract {

    interface View: SectionView {

        fun showTimerList(timerList: List<Timer>)
    }

    abstract class Presenter: SectionPresenter<View>() {

        abstract val timerModel: TimerModel

        abstract fun onTimerItemClick(position: Int)

        abstract fun onAddTimerClick()
    }
}