package com.srm.srmapp.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.srm.srmapp.Resource

@Composable
fun <T> SrmLazyRow(itemListResource: Resource<List<T>>, itemContent: @Composable() (LazyItemScope.(item: T) -> Unit)) {
    when (itemListResource) {
        is Resource.Error -> SrmText(text = "Error!")
        is Resource.Loading, is Resource.Empty -> CircularProgressIndicator()
        is Resource.Success -> {
            itemListResource.data?.let {
                if (it.isNotEmpty()) {
                    LazyColumn(modifier = Modifier
                        .heightIn(0.dp, 300.dp)
                        .fillMaxWidth()) {
                        items(it, itemContent = itemContent)
                    }
                } else {
                    SrmText(text = "No content")
                }
            } ?: SrmText(text = "No content")
        }
    }
}

@Composable
fun <T> SrmLazyRow2(l: List<T>, itemContent: @Composable() (LazyItemScope.(item: T) -> Unit)) {
    if (l.isNotEmpty()) {
        LazyColumn(modifier = Modifier
            .heightIn(0.dp, 300.dp)
            .fillMaxWidth()) {
            items(l, itemContent = itemContent)
        }
    } else {
        SrmText(text = "No content")
    }
}