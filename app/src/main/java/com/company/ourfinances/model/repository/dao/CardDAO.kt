package com.company.ourfinances.model.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.company.ourfinances.model.entity.CardEntity

@Dao
interface CardDAO {
    @Insert
    fun insert(cardEntity: CardEntity)

    @Update
    fun update(cardEntity: CardEntity)

    @Delete
    fun delete(id: Int)

    @Query("SELECT * FROM cards")
    fun getAllCards(): List<CardEntity>
}