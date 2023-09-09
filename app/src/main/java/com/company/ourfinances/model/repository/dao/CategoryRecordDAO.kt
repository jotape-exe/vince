package com.company.ourfinances.model.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.company.ourfinances.model.entity.CategoryRecordEntity

@Dao
interface CategoryRecordDAO {

    @Insert
    fun insert(categoryRecordEntity: CategoryRecordEntity)

    @Update
    fun update(categoryRecordEntity: CategoryRecordEntity)

    @Delete
    fun delete(categoryRecordEntity: CategoryRecordEntity)

    @Query("SELECT * FROM category_records")
    fun getAllCategories():List<CategoryRecordEntity>

    @Query("SELECT * FROM category_records WHERE id = :id")
    fun findById(id: Long): CategoryRecordEntity

}