package com.srm.srmapp.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SrmHeader(title: String, onClickBack: () -> Unit = {}) {
    SrmAddTitleSearch(title = title, onClickBack = onClickBack, enableAdd = false, enableSearch = false)
}

@Composable
@Preview(showBackground = true)
fun PreviewHeader() {
    SrmHeader("Preview Title")
}