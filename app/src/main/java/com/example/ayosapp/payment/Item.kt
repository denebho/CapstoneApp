package com.example.ayosapp.payment
import java.math.BigDecimal

data class Item(
    val name: String,
    val currency: String,
    val value: BigDecimal,
    val code: String? = null,
    val description: String? = null,
    val discount: BigDecimal? = null
)