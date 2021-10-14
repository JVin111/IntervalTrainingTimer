package com.vinapp.intervaltrainingtimer.data.database

import androidx.room.*
import com.vinapp.intervaltrainingtimer.entities.Timer

@Dao
interface TimerDao {

    @Insert
    fun insert(timer: Timer)

    @Update
    fun update(timer: Timer)

    @Delete
    fun delete(timer: Timer)

    @Query("SELECT * FROM timer")
    fun getAll(): List<Timer>

    @Query("SELECT * FROM timer WHERE id = :id")
    fun getById(id: Int): Timer?
}