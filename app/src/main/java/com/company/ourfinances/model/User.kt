package com.company.ourfinances.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User {

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo("name")
    var name: String = ""

    @ColumnInfo("email")
    var email: String = ""

    @ColumnInfo("password")
    var password: String = ""

    @ColumnInfo("card")
    var card: Card = Card()
}