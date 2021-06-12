package com.vinapp.intervaltrainingtimer.mvp

import com.vinapp.intervaltrainingtimer.entities.base.Interval
import com.vinapp.intervaltrainingtimer.mvp.presenter.MVPPresenter
import com.vinapp.intervaltrainingtimer.mvp.view.MVPView
import com.vinapp.intervaltrainingtimer.ui.SectionsEventHandler

interface MainContract {

    interface View: MVPView {

        fun showSection(position: Int)

        fun showIntervalKeyboard(interval: Interval?, onIntervalKeyboardListener: SectionsEventHandler.OnIntervalKeyboardListener)

        fun hideIntervalKeyboard()

        fun showStartButton()

        fun hideStartButton()

        fun showLeftButton(text: String)

        fun hideLeftButton()

        fun showRightButton(text: String)

        fun hideRightButton()
    }

    abstract class Presenter: MVPPresenter<View>(), SectionsEventHandler {

        abstract fun onStartButtonClick()

        abstract fun onSaveButtonClick()

        abstract fun onEditButtonClick()

        abstract fun sectionSelected(section: Int)

        abstract fun sectionScrolled()
    }
}