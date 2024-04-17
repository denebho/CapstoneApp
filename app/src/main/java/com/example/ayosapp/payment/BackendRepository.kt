package com.example.ayosapp.payment

import java.math.BigDecimal
import java.math.RoundingMode

class BackendRepository {
    fun getShopItems(): List<ShopItem> =
        listOf(
            ShopItem(
                name = "Shoes",
                currency = "PHP",
                value = toAmount(99.0),
                code = null,
                description = null,
                discount = toAmount(20)
            ),
            ShopItem(
                name = "Shirt",
                currency = "PHP",
                value = toAmount(19.99),
                code = null,
                description = null,
                discount = toAmount(10)
            ),
            ShopItem(
                name = "Pants",
                currency = "PHP",
                value = toAmount(20.9),
                code = null,
                description = null,
                discount = toAmount(2)
            )
        )

    companion object {
        fun toAmount(amount: Double): BigDecimal = toAmount(BigDecimal(amount))

        fun toAmount(amount: Int): BigDecimal = toAmount(BigDecimal(amount))

        private fun toAmount(amount: BigDecimal): BigDecimal =
            amount.setScale(2, RoundingMode.HALF_DOWN)
    }
}