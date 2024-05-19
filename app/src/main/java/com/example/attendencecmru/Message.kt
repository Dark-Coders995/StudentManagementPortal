package com.example.attendencecmru

import com.google.firebase.Timestamp

data class Message(
    val sender: String?,
    val content: String?,
    val timestamp: Timestamp?
)
