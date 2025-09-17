package org.revanth.technotes.core.database.entity

import kotlinx.datetime.Clock
import template.core.base.database.Entity
import template.core.base.database.PrimaryKey

@Entity(
    tableName = "notes",
    indices = [],
    inheritSuperIndices = false,
    primaryKeys = [],
    foreignKeys = [],
    ignoredColumns = [],
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val pinned: Boolean = false,
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)