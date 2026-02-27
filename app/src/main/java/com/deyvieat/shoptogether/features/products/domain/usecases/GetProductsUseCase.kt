package com.deyvieat.shoptogether.features.products.domain.usecases

import com.deyvieat.shoptogether.features.products.domain.repositories.ProductsRepository; import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repo: ProductsRepository) {
    operator fun invoke(roomId: String) = repo.observeProducts(roomId)
}
