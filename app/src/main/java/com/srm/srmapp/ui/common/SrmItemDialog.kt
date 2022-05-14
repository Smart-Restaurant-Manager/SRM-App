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
    addText: String,
    editText: String,
    moreText: String,
    deleteText: String,
    onClickAdd: () -> Unit,
    onClickEdit: () -> Unit,
    onClickMore: () -> Unit,
    onClickDelete: () -> Unit,
    onDismissRequest: () -> Unit,
    enableAdd: Boolean = true,
    enableEdit: Boolean = true,
    enableMore: Boolean = true,
    enableDelete: Boolean = true,
) {
    SrmDialog(onDismissRequest = onDismissRequest) {
        if (enableAdd)
            SrmSelectableRow(
                horizontalArrangement = Arrangement.Start,
                onClick = onClickAdd) {
                Spacer(modifier = Modifier.width(spacerWitdh))
                Icon(painter = painterResource(id = R.drawable.ic_baseline_add_24), moreText)
                Spacer(modifier = Modifier.width(spacerWitdh))
                SrmText(text = moreText)
            }

        if (enableEdit)
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

        if (enableMore)
            SrmSelectableRow(
                horizontalArrangement = Arrangement.Start,
                onClick = onClickMore) {
                Spacer(modifier = Modifier.width(spacerWitdh))
                Icon(painter = painterResource(id = R.drawable.ic_baseline_more_horiz_24), moreText)
                Spacer(modifier = Modifier.width(spacerWitdh))
                SrmText(text = moreText)
            }

        if (enableDelete)
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