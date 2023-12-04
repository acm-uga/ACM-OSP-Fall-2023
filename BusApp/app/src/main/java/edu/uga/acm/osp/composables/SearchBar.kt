package edu.uga.acm.osp.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun searchBar(searchViewModel: SearchViewModel = viewModel()) {
    val searchUiState by searchViewModel.uiState.collectAsState()
    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { searchViewModel.search()}
        ),
        singleLine = true,
        value = searchViewModel.query,
        onValueChange = { searchViewModel.query = it},
        // To test the search button's functionality, label changes to match the entered search query
        // upon pressing the search button.
        // Once proper search functionality is added, the label should be changed to always be
        // Text("Search")
        label = {
            Text(searchUiState.query)},
        modifier = Modifier.fillMaxWidth()
    )
}