package com.example.minexp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.minexp.data.AppDatabase
import com.example.minexp.data.model.QuestItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class QuestViewModel(application: Application) : AndroidViewModel(application) {

    private val questDao = AppDatabase.getDatabase(application).questDao()

    val allQuests: Flow<List<QuestItem>> = questDao.getAllQuests()

    fun getQuestById(questId: Long): Flow<QuestItem> = questDao.getQuestById(questId).filterNotNull()

    fun insert(questItem: QuestItem) = viewModelScope.launch {
        questDao.insertQuest(questItem)
    }

    fun update(questItem: QuestItem) = viewModelScope.launch {
        questDao.updateQuest(questItem)
    }

    fun delete(questItem: QuestItem) = viewModelScope.launch {
        questDao.deleteQuest(questItem)
    }

    fun toggleQuestCompleted(questItem: QuestItem) = viewModelScope.launch {
        val updatedQuest = questItem.copy(isCompleted = !questItem.isCompleted)
        questDao.updateQuest(updatedQuest)
    }

    fun clearAllQuests() {
        viewModelScope.launch {
            questDao.clearAllQuests() // Panggil fungsi DAO untuk menghapus semua
        }
    }
}