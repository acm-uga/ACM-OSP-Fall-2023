package edu.uga.acm.osp.components

data class SearchUiState(
    val searchCalled: Boolean = false,
    val query: String = "Search"
)
