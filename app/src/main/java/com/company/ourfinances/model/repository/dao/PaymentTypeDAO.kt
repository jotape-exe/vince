package com.company.ourfinances.model.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.company.ourfinances.model.entity.PaymentTypeEntity

@Dao
interface PaymentTypeDAO {

    @Insert
    fun insert(paymentType: PaymentTypeEntity)

    @Update
    fun update(paymentType: PaymentTypeEntity)

    @Delete
    fun delete(paymentType: PaymentTypeEntity)

    @Query("SELECT * FROM payment_types")
    fun getAllPaymentTypes():List<PaymentTypeEntity>

    @Query("SELECT * FROM payment_types WHERE paymentId = :id")
    fun findById(id: Long): PaymentTypeEntity
}