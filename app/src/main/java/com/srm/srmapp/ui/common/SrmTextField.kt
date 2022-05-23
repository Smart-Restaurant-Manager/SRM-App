package com.srm.srmapp.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.srm.srmapp.AppModule
import com.srm.srmapp.ui.theme.textFieldPadding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException

@Composable
fun SrmTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    errorMessage: String = "",
    isError: Boolean = false,
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 2,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.Transparent),
) {
    Column(modifier = modifier
        .padding(textFieldPadding),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        OutlinedTextField(enabled = enabled,
            readOnly = readOnly,
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.wrapContentSize(),
            singleLine = singleLine,
            textStyle = textStyle,
            label = { SrmText(text = label) },
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors)
        if (isError)
            SrmText(text = errorMessage, color = Color.Red)
    }
}


@Composable
fun SrmDateEditor(value: LocalDate?, label: String, onErrorAction: () -> Unit = {}, onValueChange: (LocalDate) -> Unit) {
    val formatter = AppModule.dateFormatter
    val pattern = AppModule.datePattern
    var isError by remember { mutableStateOf(false) }
    var dateString by remember { mutableStateOf(value?.format(formatter) ?: LocalDate.now().format(formatter)) }
    SrmTextField(
        value = dateString,
        label = label,
        onValueChange = {
            dateString = it
            val dateOut: LocalDate = try {
                LocalDate.parse(dateString, formatter)
            } catch (e: DateTimeParseException) {
                isError = true
                onErrorAction.invoke()
                return@SrmTextField
            }
            isError = false
            onValueChange.invoke(dateOut)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = isError,
        errorMessage = pattern)
}

@Composable
fun SrmTimeEditor(value: LocalTime?, label: String, onErrorAction: () -> Unit, onValueChange: (LocalTime) -> Unit) {
    val formatter = AppModule.timeFormatter
    val pattern = AppModule.timePattern
    var isError2 by remember { mutableStateOf(false) }
    var dateString by remember { mutableStateOf(value?.format(formatter) ?: LocalTime.now().format(formatter)) }
    SrmTextField(
        value = dateString,
        label = label,
        onValueChange = {
            dateString = it
            val dateOut: LocalTime = try {
                LocalTime.parse(dateString, formatter)
            } catch (e: DateTimeParseException) {
                isError2 = true
                onErrorAction.invoke()
                return@SrmTextField
            }
            isError2 = false
            onValueChange.invoke(dateOut)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = isError2,
        errorMessage = pattern)
}

@Composable
fun SrmDateTimeEditor(value: LocalDateTime?, label: String, onErrorAction: () -> Unit, onValueChange: (LocalDateTime) -> Unit) {
    val formatter = AppModule.dateTimeFormatter
    val pattern = AppModule.dateTimePattern
    var isError2 by remember { mutableStateOf(false) }
    var dateString by remember {
        mutableStateOf(value?.format(formatter) ?: LocalDateTime.now().format(formatter))
    }
    SrmTextField(
        value = dateString,
        label = label,
        onValueChange = {
            dateString = it
            val dateOut: LocalDateTime = try {
                LocalDateTime.parse(dateString, formatter)
            } catch (e: DateTimeParseException) {
                isError2 = true
                onErrorAction.invoke()
                return@SrmTextField
            }
            isError2 = false
            onValueChange.invoke(dateOut)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = isError2,
        errorMessage = pattern.lowercase())
}

@Composable
fun SrmTextFieldHint(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    shape: Shape = TextFieldDefaults.TextFieldShape,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.wrapContentSize(),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = { Text(text = placeholder) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        shape = shape,
        colors = colors)
}

@Composable
@Preview(showBackground = true)
fun PreviewTextField() {
    SrmTextField(value = "", label = "Label", onValueChange = {}, isError = true, errorMessage = "Error")
}