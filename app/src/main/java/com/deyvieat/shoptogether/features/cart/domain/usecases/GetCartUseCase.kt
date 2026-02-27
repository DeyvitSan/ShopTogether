package com.deyvieat.shoptogether.features.cart.domain.usecases

import com.deyvieat.shoptogether.features.cart.domain.repositories.CartRepository
import javax.inject.Inject

class GetCartUseCase @Inject constructor(private val repo: CartRepository){

    operator fun invoke(userId: String) = repo.observeCart(userId)
}