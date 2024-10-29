package com.edurda77.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.edurda77.domain.utils.FAVORITE_DEVICE_ID
import com.edurda77.domain.utils.FAVORITE_ID
import com.edurda77.domain.utils.FAVORITE_TABLE

@Entity(tableName = FAVORITE_TABLE)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = FAVORITE_ID)
    val id: Int = 0,
    @ColumnInfo(name = FAVORITE_DEVICE_ID)
    val deviceId: Int
)
