package com.company.ourfinances.model.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity

@Dao
interface FinanceRecordDAO{
    @Insert
    fun insert(financeRecordEntity: FinanceRecordEntity)

    @Update
    fun update(financeRecordEntity: FinanceRecordEntity)

    @Delete
    fun delete(financeRecordEntity: FinanceRecordEntity)

    @Query("SELECT * FROM finance_records WHERE recordId = :recordId")
    fun getRecordById(recordId: Long): FinanceRecordEntity

    @Query("SELECT * FROM finance_records")
    fun getAllRecords(): List<FinanceRecordEntity>

    @Query("SELECT * FROM finance_records WHERE typeRecord = :typeRecord")
    fun getAllByExpenseCategory(typeRecord: String): List<FinanceRecordEntity>

    @Query("SELECT cards.* FROM cards " +
            "INNER JOIN finance_records ON cards.cardId = finance_records.cardId " +
            "WHERE finance_records.cardId = :cardId")
    fun getCardById(cardId: Long): CardEntity

}