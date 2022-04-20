package com.srm.srmapp.ui.common

import android.view.ContextThemeWrapper
import android.widget.DatePicker
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.srm.srmapp.R
import java.time.LocalDate
import java.util.*

@Composable
fun SrmCalendarView(onDateSelected: (LocalDate) -> Unit) {
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            DatePicker(ContextThemeWrapper(context, R.style.Theme_SRMapp))
        },
        update = { view ->
            view.minDate = Calendar.getInstance().timeInMillis
            view.setOnDateChangedListener { _, year, month, dayOfMonth ->
                onDateSelected(
                    LocalDate
                        .now()
                        .withMonth(month + 1)
                        .withYear(year)
                        .withDayOfMonth(dayOfMonth)
                )
            }
        }
    )
}
