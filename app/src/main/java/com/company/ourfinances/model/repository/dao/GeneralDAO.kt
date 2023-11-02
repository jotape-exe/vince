package com.company.ourfinances.model.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query

@Dao
interface GeneralDAO {
    @Query("DELETE FROM cards")
    fun deleteCards()

    @Query("DELETE FROM goals")
    fun deleteGoals()

    @Query("DELETE FROM payment_types")
    fun deletePaymentTypes()


    @Query("DELETE FROM finance_records")
    fun deleteFinanceRecords()

    @Query("DELETE FROM category_records")
    fun deleteCategoryRecords()
}

