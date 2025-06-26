package com.example.minexp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AllData(
    val quests: List<QuestItem>,
    val notes: List<NoteCard>
)
