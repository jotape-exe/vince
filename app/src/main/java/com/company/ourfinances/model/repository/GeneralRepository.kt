package com.company.ourfinances.model.repository

import android.content.Context
import androidx.room.Query
import com.company.ourfinances.model.repository.db.VinceDatabase

class GeneralRepository(context: Context) {
    private val database = VinceDatabase.getInstance(context).generalDAO()

    fun deleteAll() {
        database.deleteCards()
        database.deleteGoals()
        database.deleteFinanceRecords()
    }

}