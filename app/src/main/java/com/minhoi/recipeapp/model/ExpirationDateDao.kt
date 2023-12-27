package com.minhoi.recipeapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExpirationDateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpirationDate(expirationDate: ExpirationDateEntity)

    @Query("SELECT * FROM expiration_date_table WHERE savedDate = :todayDate")
    suspend fun getExpirationDatesForToday(todayDate: String): List<ExpirationDateEntity>?

    @Query("DELETE FROM expiration_date_table")
    suspend fun deleteAll()

}
