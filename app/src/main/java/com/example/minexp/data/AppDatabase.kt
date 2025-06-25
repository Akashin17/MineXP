package com.example.minexp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.minexp.data.dao.NoteCardDao
import com.example.minexp.data.dao.QuestDao // <-- IMPORT DAO BARU
import com.example.minexp.data.model.NoteCard
import com.example.minexp.data.model.QuestItem // <-- IMPORT ENTITAS BARU

@Database(
    entities = [NoteCard::class, QuestItem::class], // <-- TAMBAHKAN QuestItem DI SINI
    version = 3, // <-- NAIKKAN VERSI DATABASE KARENA ADA PERUBAHAN SKEMA
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteCardDao(): NoteCardDao
    abstract fun questDao(): QuestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "note_card_database" // Nama database bisa tetap sama atau diubah jika perlu
                )
                    .fallbackToDestructiveMigration() // Ingat ini akan menghapus data saat versi berubah
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}