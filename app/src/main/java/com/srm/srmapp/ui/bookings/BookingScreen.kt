package com.srm.srmapp.ui.bookings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Booking
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart
import com.srm.srmapp.ui.theme.spacerWitdh
import timber.log.Timber


@OptIn(ExperimentalFoundationApi::class)
@Composable
@Destination
fun BookingScreen(
    navigator: DestinationsNavigator,
    viewmodel: BookingViewModel,
) {
    // booking list state
    val bookingListState by viewmodel.bookingList.observeAsState(Resource.Empty())
    if (bookingListState.isEmpty()) viewmodel.refreshBookingsList()

    // add book item
    var popupAddState by remember { mutableStateOf(false) }

    //Refresh booking List
    val refreshState = rememberSwipeRefreshState(bookingListState.isLoading())


    // Search booking
    val bookingList = remember(bookingListState.data) { bookingListState.data ?: emptyList() }
    var dialogSearchBook by remember { mutableStateOf(false) }

    val status by viewmodel.status.observeAsState(Resource.Empty())

    // Allow composabel inside function
    var popupSeeBooking by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = paddingStart, end = paddingEnd),
        horizontalAlignment = Alignment.CenterHorizontally) {
        SrmAddTitleSearch(title = stringResource(id = R.string.reservas),
            onClickSearch = { dialogSearchBook = true },
            onClickAdd = { popupAddState = true },
            onClickBack = { navigator.navigateUp() }
        )
        Spacer(modifier = Modifier.width(20.dp))
        SwipeRefresh(
            state = refreshState,
            modifier = Modifier.padding(0.dp, 30.dp),
            onRefresh = { viewmodel.refreshBookingsList() }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                stickyHeader {
                    SrmStickyHeader(headers = listOf(stringResource(id = R.string.name),
                        stringResource(id = R.string.amount_of_people_short),
                        stringResource(id = R.string.date)))
                }
                items(bookingList, key = { it.id }) {
                    var dialogItemState by remember { mutableStateOf(false) }
                    BookItem(book = it) { dialogItemState = true }
                    if (dialogItemState) {
                        BookItemPopup(
                            book = it,
                            viewmodel = viewmodel,
                            onDismissRequest = { dialogItemState = false }
                        )
                    }
                }
            }
        }
    }

    if (status.isSuccess()) {
        Timber.d("got status $status")
    }


    //Añadir Reserva
    if (popupAddState) {
        val booking = remember { BookingDataHolder() }
        BookingDialog(resId = R.string.add_booking,
            booking = booking,
            onDismissRequest = { popupAddState = false },
            onNameChange = { booking.name = it },
            onPeopleChange = { booking.people = it },
            onDateChange = { booking.date = it },
            onPhoneChange = { booking.phone = it },
            onEmailChange = { booking.email = it },
            onTableChange = { booking.table = it })
        {
            viewmodel.addBooking(booking)

        }
    }

    val statusMessage by viewmodel.status.observeAsState(Resource.Empty())

    if (statusMessage.isSuccess() || statusMessage.isError()) {
        val msg = statusMessage.data ?: statusMessage.message
        msg?.let {
            val openDialog = remember { mutableStateOf(true) }
            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        viewmodel.clearStatus()
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            openDialog.value = false
                            viewmodel.clearStatus()
                        })
                        { Text(text = "Confirmar") }
                    },
                    text = { Text(text = it) }
                )
            }

        }
    }
    //Editar Reserva
    if (dialogSearchBook) {
        SrmSearch(items = bookingList, onDismissRequest = { dialogSearchBook = false },
            predicate = { book, query ->
                book.name.startsWith(query, ignoreCase = true)
            }) { book ->
            SrmSelectableRow(
                onClick = { popupSeeBooking = true }
            ) {
                SrmText(text = book.name, textAlign = TextAlign.Center)
                SrmText(text = book.people.toString(), textAlign = TextAlign.Center)
                SrmText(text = book.date.toString(), textAlign = TextAlign.Center)
            }
            if (popupSeeBooking) {
                BookItemPopup(book = book, viewmodel = viewmodel, onDismissRequest = { popupSeeBooking = false })
            }
        }
    }

}

@Composable
fun BookingDialog(
    resId: Int,
    booking: BookingDataHolder,
    onDismissRequest: () -> Unit,
    onNameChange: (String) -> Unit,
    onPeopleChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTableChange: (String) -> Unit,
    onClick: () -> Unit,
) {
    SrmDialog(onDismissRequest = onDismissRequest) {
        SrmTextFieldHint(value = booking.name, placeholder = stringResource(R.string.food_name), onValueChange = onNameChange)
        SrmTextFieldHint(value = booking.people, placeholder = stringResource(R.string.amount_of_people), onValueChange = onPeopleChange)
        SrmTextFieldHint(value = booking.date, placeholder = stringResource(R.string.date), readOnly = true, onValueChange = onDateChange)
        SrmTextFieldHint(value = booking.phone, placeholder = stringResource(R.string.tel), onValueChange = onPhoneChange)
        SrmTextFieldHint(value = booking.email, placeholder = stringResource(R.string.mail), onValueChange = onEmailChange)
        SrmTextFieldHint(value = booking.table, placeholder = stringResource(R.string.table), onValueChange = onTableChange)
        SrmTextButton(text = stringResource(resId), onClick = {
            onClick.invoke()
            onDismissRequest.invoke()
        })
    }
}

@Composable
fun BookItem(book: Booking, onClick: () -> Unit) {
    SrmSelectableRow(onClick = onClick, horizontalArrangement = Arrangement.SpaceEvenly) {
        SrmText(text = book.name, textAlign = TextAlign.Center)
        SrmText(text = book.people.toString(), textAlign = TextAlign.Center)
        SrmText(text = book.date.toString(), textAlign = TextAlign.Center)
    }
}

@Composable
fun BookItemPopup(
    book: Booking,
    viewmodel: BookingViewModel,
    onDismissRequest: () -> Unit = {},

    ) {
    // see bookings
    var popupSeeBooking by remember { mutableStateOf(false) }
    var popupEditBooking by remember { mutableStateOf(false) }


    SrmDialog(onDismissRequest = onDismissRequest) {
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = {
                popupSeeBooking = true

            }
        ) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_history_24), contentDescription = stringResource(R.string.mostrar_reserva))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.mostrar_reserva))

        }
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = {
                popupEditBooking = true
            }
        ) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = stringResource(R.string.Editar))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.Editar))
        }
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = {
                viewmodel.deleteBooking(book.id)
                onDismissRequest.invoke()
            }
        ) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_delete_24), contentDescription = stringResource(R.string.delete))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.delete))
        }
    }

    // Ver reserva
    if (popupSeeBooking) {
        SrmDialog(onDismissRequest = { popupSeeBooking = false }) {
            Spacer(modifier = Modifier.size(20.dp))

            SrmText(text = "Nombre:  ${book.name}")
            Spacer(modifier = Modifier.size(20.dp))

            SrmText(text = "Telefono:  ${book.phone}")
            Spacer(modifier = Modifier.size(20.dp))

            SrmText(text = "Email:  ${book.email}")
            Spacer(modifier = Modifier.size(20.dp))

            SrmText(text = "Personas:  ${book.people}")
            Spacer(modifier = Modifier.size(20.dp))

            SrmText(text = "Fecha:  ${book.date}")
            Spacer(modifier = Modifier.size(20.dp))

            SrmText(text = "Mesa:  ${book.table}")
            Spacer(modifier = Modifier.size(20.dp))
        }
    }


    if (popupEditBooking) {
        val booking = remember { BookingDataHolder.fromBooking(book) }
        BookingDialog(resId = R.string.mod_booking,
            booking = booking,
            onDismissRequest = { popupEditBooking = false },
            onNameChange = { booking.name = it },
            onPeopleChange = { booking.people = it },
            onDateChange = { booking.date = it },
            onPhoneChange = { booking.phone = it },
            onEmailChange = { booking.email = it },
            onTableChange = { booking.table = it }) {
            viewmodel.putBooking(book.id, booking)
        }
    }

}