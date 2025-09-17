package org.revanth.technotes.feature.home.addNote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.revanth.technotes.core.designsystem.components.RevanthOutlinedTextField
import org.revanth.technotes.core.designsystem.components.TextFieldConfig
import org.revanth.technotes.core.ui.scaffold.KptScaffold
import template.core.base.ui.EventsEffect


@Composable
fun AddNoteScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddNoteViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is AddNoteEvent.NavigateBack -> navigateBack()
            is AddNoteEvent.ShowToast -> {
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }
        }
    }

    AddNoteDialogs(
        dialogState = state.dialogState,
        onDismissRequest = { viewModel.trySendAction(AddNoteAction.ErrorDialogDismiss) }
    )

    AddNoteContent(
        state = state,
        onAction = { viewModel.trySendAction(it) },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun AddNoteDialogs(
    dialogState: AddNoteState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is AddNoteState.DialogState.Error -> {
            Text(dialogState.message)
        }

        is AddNoteState.DialogState.Loading -> {
            CircularProgressIndicator()
        }

        null -> Unit
    }
}

@Composable
fun AddNoteContent(
    state: AddNoteState,
    onAction: (AddNoteAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    KptScaffold(
        title = if(state.id==-1L) "Add Note" else "Edit Note",
        onNavigationIconClick = {
            onAction(AddNoteAction.OnNavigateBack)
        },
        modifier = modifier,
    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RevanthOutlinedTextField(
                value = state.title,
                onValueChange = { onAction(AddNoteAction.TitleChanged(it)) },
                label = "Title",
                config = TextFieldConfig(isError = state.isError && state.title.isBlank()),
            )

            RevanthOutlinedTextField(
                value = state.description,
                onValueChange = { onAction(AddNoteAction.DescriptionChanged(it)) },
                label = "Description",
                config = TextFieldConfig(
                    maxLines = 10,
                    singleLine = false,
                    keyboardOptions = KeyboardOptions.Default,
                ),
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onAction(AddNoteAction.SaveClicked) },
                enabled = state.isSaveButtonEnabled,
            ) {
                Text("Save")
            }
        }
    }
}
