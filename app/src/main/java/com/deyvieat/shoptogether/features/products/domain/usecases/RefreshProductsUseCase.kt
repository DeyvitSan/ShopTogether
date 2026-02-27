package com.deyvieat.shoptogether.features.products.domain.usecases

import com.deyvieat.shoptogether.features.products.domain.repositories.ProductsRepository; import javax.inject.Inject

class RefreshProductsUseCase @Inject constructor(private val repo: ProductsRepository) {
    suspend operator fun invoke(roomId: String) = repo.refreshProducts(roomId)
}
