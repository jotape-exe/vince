package com.company.ourfinances.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.company.ourfinances.model.enums.CardTypeEnum
import java.util.Date

@Entity
class Card {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "bankName")
    var bankName: String = ""

    @ColumnInfo(name = "balance")
    var balance: Double = 0.0

    @ColumnInfo(name = "numberCode")
    var numberCode: String = ""

    @ColumnInfo(name = "dueDate")
    var dueDate: Date = Date()

    @ColumnInfo(name = "type")
    var type: CardTypeEnum? = null

    @ColumnInfo(name = "invoiceDueDate")
    var invoiceDueDate: Date? = null
}