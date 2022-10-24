package com.hafiztaruligani.movieday.presentation

data class CommonUiState <T> (
    val loading : Boolean = false,
    val error : String = "",
    val data: T? = null
)