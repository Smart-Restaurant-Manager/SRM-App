package com.srm.srmapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart

@Composable
fun SrmInfoList(infoList: List<Pair<String, String>>, center: Boolean = false, maxHeight: Dp = 300.dp) {
    LazyColumn(modifier = Modifier
        .heightIn(0.dp, maxHeight)
        .fillMaxWidth(), contentPadding = PaddingValues(start = paddingStart, end = paddingEnd)) {
        items(infoList) { (title, value) ->
            Column(modifier = Modifier.fillParentMaxWidth(),
                horizontalAlignment = if (center) Alignment.CenterHorizontally else Alignment.Start) {
                if (title.isNotEmpty() && value.isNotEmpty()) {
                    SrmText(text = title, fontWeight = FontWeight.Bold)
                    SrmText(modifier = Modifier.padding(bottom = 4.dp),
                        text = value)
                } else {
                    Spacer(modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.Gray))
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SrmListInfoPreview() {
    val l = listOf(Pair("Title", "value"), Pair("", ""), Pair("Title2", "value2"), Pair("Title3", "value3"))
    SrmInfoList(infoList = l, center = false)
}