package com.company.ourfinances.model.constants

class DatabaseConstants private constructor(){
    object User{
        const val id = "id"
        const val name = "name"
    }

    object FinanceRecord{
        const val recordId = "recordId"
    }

    object PreferencesConstants{
        const val KEY_TITLE_RECORD = "TITLE_RECORD"
    }
}