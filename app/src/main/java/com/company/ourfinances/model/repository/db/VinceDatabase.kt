package com.company.ourfinances.model.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.company.ourfinances.model.entity.CardEntity
import com.company.ourfinances.model.entity.CategoryExpenseEntity
import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.model.entity.FinanceRecordEntity
import com.company.ourfinances.model.repository.dao.CategoryExpenseDAO
import com.company.ourfinances.model.repository.dao.FinanceRecordDAO
import com.company.ourfinances.model.repository.dao.PaymentTypeDAO

@Database(
    entities = [
        CardEntity::class,
        PaymentTypeEntity::class,
        CategoryExpenseEntity::class,
        FinanceRecordEntity::class,
    ],
    version = VinceDatabase.VERSION
)
abstract class VinceDatabase : RoomDatabase() {

    abstract fun getFinanceRecordDAO(): FinanceRecordDAO
    abstract fun getCategoryExpenseDAO(): CategoryExpenseDAO
    abstract fun getPaymentTypeDAO(): PaymentTypeDAO

    companion object {
        private lateinit var INSTANCE: VinceDatabase

        fun getInstance(context: Context): VinceDatabase {
            synchronized(VinceDatabase::class) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context, VinceDatabase::class.java, DB_NAME)
                        .allowMainThreadQueries()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                db.execSQL("INSERT INTO expense_records (name) VALUES ('Alimentação'), ('Moradia'), ('Transporte'), ('Lazer'), ('Investimentos'), ('Compras'), ('Veículo'), ('Outro')")
                                db.execSQL("INSERT INTO payment_types (name) VALUES ('Pix'), ('Dinheiro'), ('Boleto'), ('Cartão')")
                            }
                        })
                        .build()
                }
            }
            return INSTANCE
        }

        private const val DB_NAME = "vince_database"
        const val VERSION = 1

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }

        }

    }

}