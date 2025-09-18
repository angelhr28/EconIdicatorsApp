package com.yourapp.econ.domain.entity

data class User(
    val id: Long = 0,
    val email: String,
    val name: String,
    val lastName: String,
    val passwordHash: String
)
