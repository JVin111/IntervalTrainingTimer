package com.vinapp.intervaltrainingtimer.data.source.interval

import com.vinapp.intervaltrainingtimer.base.data.Repository
import com.vinapp.intervaltrainingtimer.domain.entities.Interval
import kotlinx.coroutines.flow.Flow

interface IntervalRepository : Repository {
    suspend fun saveInterval(interval: Interval)
    suspend fun updateInterval(interval: Interval)
    suspend fun deleteInterval(timerId: String, intervalId: String)
    fun getIntervalListFlow(timerId: String): Flow<List<Interval>>
}