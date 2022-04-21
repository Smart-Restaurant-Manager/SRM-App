package com.srm.srmapp.ui.common

import androidx.compose.material.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

@Composable
fun SrmSnackBar(
    timeout: Long = 2000,
    content: @Composable () -> Unit,
) {
    val show = remember {
        mutableStateOf(true)
    }

    if (show.value) {
        Snackbar(content = content)
    }

    if (show.value) {
        LaunchedEffect(key1 = show.value) {
            delay(timeout)
            show.value = false
        }
    }
}