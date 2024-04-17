package com.example.ayosapp.payment

import com.example.ayosapp.payment.BackendRepository
import com.example.ayosapp.payment.CartRepository

internal object RepositoryModule {
    val cartRepository: CartRepository by lazy { CartRepository() }
    val backendRepository: BackendRepository by lazy { BackendRepository() }
}