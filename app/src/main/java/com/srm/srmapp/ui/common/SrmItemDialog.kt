package com.srm.srmapp.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.srm.srmapp.R
import com.srm.srmapp.ui.theme.spacerWitdh


@Composable
fun SrmItemDialog(
    editText: String,
    deleteText: String,
    moreText: String,
    onDismissRequest: () -> Unit,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    onClickMore: () -> Unit,
) {
    SrmDialog(onDismissRequest = onDismissRequest) {
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = onClickMore) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_add_24), moreText)
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = moreText)
        }
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = onClickEdit
        ) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = editText)
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = editText)
        }
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = onClickDelete
        ) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_delete_24), contentDescription = deleteText)
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = deleteText)
        }
    }
}