package com.srm.srmapp.data.models

data class User(val username: String, val token: String, val tokenExpired: Boolean = false)