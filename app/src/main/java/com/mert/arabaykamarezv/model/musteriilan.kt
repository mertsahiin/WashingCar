package com.mert.arabaykamarezv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class musteriilan(

    @ColumnInfo(name="Plaka")
    var Plaka : String,

    @ColumnInfo(name="Latitude")
    var latitude : Double,

    @ColumnInfo(name="Longitude")
    var longitude : Double
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

}