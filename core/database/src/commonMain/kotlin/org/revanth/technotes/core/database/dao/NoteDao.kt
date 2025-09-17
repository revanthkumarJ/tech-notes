package org.revanth.technotes.core.database.dao

import kotlinx.coroutines.flow.Flow
import org.revanth.technotes.core.database.entity.NoteEntity
import org.revanth.technotes.core.database.entity.SampleEntity
import template.core.base.database.Dao
import template.core.base.database.Delete
import template.core.base.database.Insert
import template.core.base.database.OnConflictStrategy
import template.core.base.database.Query
import template.core.base.database.Update


@Dao
interface NoteDao {

    @Insert(entity = NoteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

//    @Update
//    suspend fun updateNote(note: NoteEntity)
//
    @Delete
    suspend fun deleteNote(note: NoteEntity)
//
    @Query("SELECT * FROM notes ORDER BY pinned DESC, updatedAt DESC")
     fun getAllNotes(): Flow<List<NoteEntity>>
//
//    @Query("SELECT * FROM notes WHERE id = :id")
//    suspend fun getNoteById(id: Long): NoteEntity?
//
//    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
//    suspend fun searchNotes(query: String): List<NoteEntity>
}
