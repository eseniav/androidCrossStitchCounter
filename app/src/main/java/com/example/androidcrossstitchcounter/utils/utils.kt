package com.example.androidcrossstitchcounter.utils

fun String.toStringOrNull(): String? = this.ifBlank { null }
