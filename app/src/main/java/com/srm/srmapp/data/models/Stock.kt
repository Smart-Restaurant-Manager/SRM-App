package com.srm.srmapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Stock(val id: Int, val quantity: Float, val expirationDate: String) : Parcelable