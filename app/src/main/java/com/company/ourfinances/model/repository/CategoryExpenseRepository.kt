package com.company.ourfinances.model.repository

import android.content.Context
import com.company.ourfinances.model.entity.CategoryExpenseEntity
import com.company.ourfinances.model.repository.db.VinceDatabase

class CategoryExpenseRepository(context: Context) {
    private val database = VinceDatabase.getInstance(context).getCategoryExpenseDAO()

    fun getAll(): List<CategoryExpenseEntity>{
        return database.getAllCategories()
    }
}