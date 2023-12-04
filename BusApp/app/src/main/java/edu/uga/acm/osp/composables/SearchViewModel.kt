package edu.uga.acm.osp.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {
    // The user input. This is query that searching will use.
    var query by mutableStateOf("")

    // Search UI state
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    // This is called when the search button on the keyboard is pressed
    fun search() {
        // Currently it updates the query value in the SearchUiState.
        // The contents of this function along with the contents of SearchUiState.kt should be changed
        // as needed to add proper search functionality.
        _uiState.update { currentState ->
            currentState.copy(query = this.query) }
    }
}