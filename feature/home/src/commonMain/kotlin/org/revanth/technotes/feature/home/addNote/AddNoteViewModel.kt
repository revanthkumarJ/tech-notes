package org.revanth.technotes.feature.home.addNote

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.revanth.technotes.core.database.dao.NoteDao
import org.revanth.technotes.core.database.entity.NoteEntity
import org.revanth.technotes.feature.home.addNote.AddNoteEvent.*
import org.revanth.technotes.feature.home.addNote.AddNoteState.DialogState.*
import org.revanth.technotes.feature.home.utils.DateTimeUtils
import template.core.base.common.DataState
import template.core.base.ui.BaseViewModel

class AddNoteViewModel(
    private val noteDao: NoteDao
) : BaseViewModel<AddNoteState, AddNoteEvent, AddNoteAction>(
    initialState = AddNoteState()
) {

    private var saveJob: Job? = null

    private fun updateState(update: (AddNoteState) -> AddNoteState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: AddNoteAction) {
        when (action) {
            is AddNoteAction.TitleChanged -> {
                updateState { it.copy(title = action.title, isError = false, errorMsg = null) }
            }

            is AddNoteAction.DescriptionChanged -> {
                updateState { it.copy(description = action.description, isError = false, errorMsg = null) }
            }

            is AddNoteAction.SaveClicked -> saveNote()

            is AddNoteAction.ErrorDialogDismiss -> {
                updateState { it.copy(dialogState = null) }
            }

            is AddNoteAction.Internal.ReceiveSaveResult -> handleSaveResult(action.result)

            is AddNoteAction.OnNavigateBack -> {
                sendEvent(AddNoteEvent.NavigateBack)
            }
        }
    }

    private fun saveNote() {
        val title = state.title
        val description = state.description

        if (title.isBlank() || description.isBlank()) {
            updateState {
                it.copy(isError = true, errorMsg = "Title and description cannot be empty")
            }
            return
        }

        saveJob?.cancel()
        updateState { it.copy(dialogState = AddNoteState.DialogState.Loading) }

        saveJob = viewModelScope.launch {
            try {
                val note = NoteEntity(
                    title = title,
                    description = description,
                    createdAt = if(state.createdAt == 0L) DateTimeUtils.currentTimeMillis() else state.createdAt,
                    updatedAt= DateTimeUtils.currentTimeMillis()
                )
                val id = noteDao.insertNote(note)
                sendAction(AddNoteAction.Internal.ReceiveSaveResult(DataState.Success(id)))
            } catch (e: Exception) {
                sendAction(AddNoteAction.Internal.ReceiveSaveResult(DataState.Error(e)))
            }
        }
    }

    private fun handleSaveResult(result: DataState<Long>) {
        when (result) {
            is DataState.Success -> {
                updateState { it.copy(dialogState = null) }
                sendEvent(ShowToast("Note saved successfully"))
                sendEvent(AddNoteEvent.NavigateBack)
            }
            is DataState.Error -> {
                updateState {
                    it.copy(dialogState = Error(result.error.message ?: "Error saving note"))
                }
            }
            is DataState.Loading -> {
                updateState { it.copy(dialogState = AddNoteState.DialogState.Loading) }
            }

            else -> {}
        }
    }
}


data class AddNoteState(
    val title: String = "",
    val description: String = "",
    val createdAt:Long=0L,
    val id:Long=-1,
    val isError: Boolean = false,
    val errorMsg: String? = null,
    val dialogState: DialogState? = null,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState

        data object Loading : DialogState
    }

    val isSaveButtonEnabled: Boolean
        get() = title.isNotBlank() && description.isNotBlank()
}

sealed interface AddNoteEvent {
    data object NavigateBack : AddNoteEvent
    data class ShowToast(val message: String) : AddNoteEvent
}

sealed interface AddNoteAction {
    data class TitleChanged(val title: String) : AddNoteAction
    data class DescriptionChanged(val description: String) : AddNoteAction
    data object SaveClicked : AddNoteAction
    data object ErrorDialogDismiss : AddNoteAction
    data object OnNavigateBack: AddNoteAction

    sealed class Internal : AddNoteAction {
        data class ReceiveSaveResult(val result: DataState<Long>) : Internal()
    }
}