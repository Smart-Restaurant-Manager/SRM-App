package com.srm.srmapp.ui.common

import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.srm.srmapp.R

@Composable
fun SrmDeleteDialog(onDismissRequest: () -> Unit, onDelete: () -> Unit) {
    AlertDialog(onDismissRequest = onDismissRequest,
        confirmButton = {
            SrmTextButton(
                text = stringResource(R.string.affirmativeButton),
                onClick = {
                    onDelete.invoke()
                    onDismissRequest.invoke()
                },
            )
        },
        dismissButton = { SrmTextButton(onClick = onDismissRequest, text = stringResource(R.string.negativeButton)) },
        text = { SrmText(text = "Estas seguro?") })
}