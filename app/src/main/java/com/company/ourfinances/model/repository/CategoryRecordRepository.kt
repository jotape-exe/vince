package com.company.ourfinances.model.repository

import android.content.Context
import com.company.ourfinances.model.entity.CategoryRecordEntity
import com.company.ourfinances.model.repository.db.VinceDatabase

class CategoryRecordRepository(context: Context) {
    private val database = VinceDatabase.getInstance(context).getCategoryRecordDAO()

    fun getAll(): List<CategoryRecordEntity>{
        return database.getAllCategories()
    }

    fun findById(id: Long): CategoryRecordEntity{
        return database.findById(id)
    }
}