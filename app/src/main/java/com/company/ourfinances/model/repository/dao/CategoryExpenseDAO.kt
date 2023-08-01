package com.company.ourfinances.model.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.company.ourfinances.model.entity.CategoryExpenseEntity

@Dao
interface CategoryExpenseDAO {

    @Insert
    fun insert(categoryExpenseEntity: CategoryExpenseEntity)

    @Update
    fun update(categoryExpenseEntity: CategoryExpenseEntity)

    @Delete
    fun delete(categoryExpenseEntity: CategoryExpenseEntity)

    @Query("SELECT * FROM expense_records")
    fun getAllCategories():List<CategoryExpenseEntity>

    @Query("SELECT * FROM expense_records WHERE id = :id")
    fun findById(id: Long): CategoryExpenseEntity

}