package com.example.testapp.model

data class InputData(
    val value: Int,
    val ts: Long = System.currentTimeMillis()
) {
}