package org.revanth.technotes.feature.home.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.revanth.technotes.core.database.entity.NoteEntity
import template.core.base.ui.EventsEffect


@Composable
fun HomeScreen(
    navigateToAddNote: () -> Unit,
    navigateToEditNote: (NoteEntity) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is HomeEvent.NavigateToAddNote -> navigateToAddNote()
            is HomeEvent.NavigateToEdit -> navigateToEditNote(event.note)
            is HomeEvent.ShowToast -> scope.launch { snackbarHostState.showSnackbar(event.message) }
            is HomeEvent.ShowNoteOptions -> { /* open bottom sheet or menu */ }
        }
    }

    HomeDialogs(
        dialogState = state.dialogState,
        onDismissRequest = { /* handle error dismiss */ }
    )

    HomeContent(
        state = state,
        onAction = { viewModel.trySendAction(it) },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun HomeDialogs(
    dialogState: HomeState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is HomeState.DialogState.Error -> Text(dialogState.message)
        is HomeState.DialogState.Loading -> CircularProgressIndicator()
        null -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Tech Notes") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  onAction(HomeAction.OnAddNote)},
                modifier = Modifier.padding(16.dp)
            ) { Icon(Icons.Filled.Add, contentDescription = "Add Note") }
        },
        modifier = modifier,
    ) { innerPadding ->
        if (state.notes.isEmpty() && state.dialogState==null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No notes yet. Add one!")
            }
        } else {
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(state.notes) { note ->
                    NoteItem(
                        note = note,
                        onEditClick = {  },
                        onDeleteClick = { onAction(HomeAction.OnDeleteNote(it)) },
                        onMoreClick = { onAction(HomeAction.OnMoreClick(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Composable
fun NoteItem(
    note: NoteEntity,
    onEditClick: (NoteEntity) -> Unit,
    onDeleteClick: (NoteEntity) -> Unit,
    onMoreClick: (NoteEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onMoreClick(note) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Row 1: Title + Icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Title takes all remaining space
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp) // optional spacing from icons
                )

                // Icons on the right
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = { onEditClick(note) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                    IconButton(onClick = { onDeleteClick(note) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row 2: Description
            Text(
                text = note.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

