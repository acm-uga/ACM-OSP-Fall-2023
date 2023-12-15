package edu.uga.acm.osp.composables

data class SearchUiState(
    val searchCalled: Boolean = false,
    val query: String = "Search"
)
