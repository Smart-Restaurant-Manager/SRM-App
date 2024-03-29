package com.srm.srmapp.ui.bookings

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.AppModule
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Booking
import com.srm.srmapp.ui.common.*
import java.time.LocalDateTime


@Composable
@Destination
fun BookingScreen(
    navigator: DestinationsNavigator,
    viewmodel: BookingViewModel,
) {
    // booking list state
    val bookingListState by viewmodel.bookingList.observeAsState(Resource.Empty())
    if (bookingListState.isEmpty()) viewmodel.refreshBookingsList()

    // search engine properties
    val searchProperties = SrmSearchProperties<Booking>(
        searchPredicate = viewmodel.predicate,
        searchLabel = "Buscar reservas",
        startSearchText = { it.name },
        endSearchText = { "Taula: ${it.table}" })

    // dialog content
    val crudDialogContent = SrmCrudDialogContent<Booking>(
        editDialogContent = { item ->
            BookingDialog(buttonText = stringResource(id = R.string.mod_booking),
                onClick = { viewmodel.putBooking(it) },
                bookingState = item)
        },
        addDialogContent = null,
        onDelete = { viewmodel.deleteBooking(it.bookingId) },
        moreDialogContent = {
            val items = listOf(
                Pair("Id", it.bookingId.toString()),
                Pair("Nombre", it.name),
                Pair("Email", it.email),
                Pair("Telefono", it.phone),
                Pair("Fecha", it.date.format(AppModule.dateTimeFormatter)),
                Pair("", ""),
                Pair("Personas", it.people.toString()),
                Pair("Mesa", it.table),
            )
            SrmInfoList(infoList = items)
        },
    )

    SrmListWithCrudActions(
        title = stringResource(id = R.string.reservas),
        itemList = bookingListState.data ?: emptyList(),
        onAddDialog = {
            BookingDialog(buttonText = stringResource(id = R.string.add_booking),
                onClick = { viewmodel.addBooking(it) },
                bookingState = null)
        },
        onBack = { navigator.navigateUp() },
        onRefresh = { viewmodel.refreshBookingsList() },
        refresState = rememberSwipeRefreshState(isRefreshing = bookingListState.isLoading()),
        listItemStartText = { "${it.name}\n${it.people} personas" },
        listItemEndText = { "Taula: ${it.table}\n ${it.date.format(AppModule.dateTimeFormatter)}" },
        searchProperties = searchProperties,
        crudDialogContent = crudDialogContent,
        baseViewModel = viewmodel)
}

@Composable
fun BookingDialog(
    buttonText: String,
    onClick: (Booking) -> Unit,
    bookingState: Booking?,
) {
    var name by remember { mutableStateOf(bookingState?.name ?: "") }
    var email by remember { mutableStateOf(bookingState?.email ?: "") }
    var phone by remember { mutableStateOf(bookingState?.phone ?: "") }
    var date by remember { mutableStateOf(bookingState?.date ?: LocalDateTime.now()) }
    var people by remember { mutableStateOf(bookingState?.people?.toString() ?: "") }
    var table by remember { mutableStateOf(bookingState?.table ?: "") }
    var error by remember { mutableStateOf(false) }
    SrmTextField(value = name, label = stringResource(R.string.food_name), onValueChange = { name = it })
    SrmTextField(value = people,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = stringResource(R.string.amount_of_people),
        onValueChange = { people = it })
    SrmDateTimeEditor(value = date,
        label = stringResource(id = R.string.fecha),
        onErrorAction = { error = true },
        onValueChange = {
            date = it
            error = false
        })
    SrmTextField(value = phone,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = stringResource(R.string.tel),
        onValueChange = { phone = it })
    SrmTextField(value = email, label = stringResource(R.string.mail), onValueChange = { email = it })
    SrmTextField(value = table, label = stringResource(R.string.table), onValueChange = { table = it })
    SrmTextButton(text = buttonText,
        enabled = !error,
        onClick = {
            onClick.invoke(Booking(
                bookingState?.bookingId ?: -1,
                name,
                email,
                phone,
                date,
                people.toInt(),
                table))
        })
}