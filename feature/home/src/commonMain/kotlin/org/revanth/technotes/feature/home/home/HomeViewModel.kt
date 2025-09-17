package org.revanth.technotes.feature.home.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.revanth.technotes.core.database.dao.NoteDao
import org.revanth.technotes.core.database.entity.NoteEntity
import template.core.base.ui.BaseViewModel

class HomeViewModel(
    private val noteDao: NoteDao
) : BaseViewModel<HomeState, HomeEvent, HomeAction>(
    initialState = HomeState(dialogState = HomeState.DialogState.Loading)
) {

    init {
        observeNotes()
    }

    private fun observeNotes() {
        viewModelScope.launch {
            noteDao.getAllNotes()
                .collect { notes ->
                    updateState {
                        it.copy(
                            notes = notes,
                            dialogState = null
                        )
                    }
                }
        }
    }

    private fun updateState(update: (HomeState) -> HomeState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnAddNote -> sendEvent(HomeEvent.NavigateToAddNote)
            is HomeAction.OnDeleteNote -> deleteNote(action.note)
            is HomeAction.OnMoreClick -> sendEvent(HomeEvent.ShowNoteOptions(action.note))
        }
    }

    private fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            try {
                noteDao.deleteNote(note)
                sendEvent(HomeEvent.ShowToast("Note deleted successfully"))
            } catch (e: Exception) {
                sendEvent(HomeEvent.ShowToast(e.message ?: "Error deleting note"))
            }
        }
    }
}

data class HomeState(
    val notes: List<NoteEntity> = emptyList(),
    val dialogState: DialogState? = null,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data object Loading : DialogState
    }
}

sealed interface HomeEvent {
    data object NavigateToAddNote : HomeEvent
    data class NavigateToEdit(val note: NoteEntity) : HomeEvent
    data class ShowNoteOptions(val note: NoteEntity) : HomeEvent
    data class ShowToast(val message: String) : HomeEvent
}

sealed interface HomeAction {
    data object OnAddNote : HomeAction
    data class OnDeleteNote(val note: NoteEntity) : HomeAction
    data class OnMoreClick(val note: NoteEntity) : HomeAction
}
