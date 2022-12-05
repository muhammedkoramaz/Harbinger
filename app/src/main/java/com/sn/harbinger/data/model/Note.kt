package com.sn.harbinger.data.model

enum class Priority(val priority: String) {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high")
}

data class NoteList(var notes: List<Note>)

data class Note(
    val endDate: String,
    val priority: String,
    val completed: Boolean,
    val projectID: String,
    val noteID: String,
    val title: String,
    val description: String,
)
