package com.mert.arabaykamarezv.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mert.arabaykamarezv.model.Place

@Database(entities = [Place::class], version = 2)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}