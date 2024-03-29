package com.vinapp.intervaltrainingtimer.domain.entities

import com.vinapp.intervaltrainingtimer.common.IntervalColor

data class Interval(
    val id: Int,
    val timerId: String,
    val name: String,
    val durationInSeconds: Long,
    val color: IntervalColor
)
