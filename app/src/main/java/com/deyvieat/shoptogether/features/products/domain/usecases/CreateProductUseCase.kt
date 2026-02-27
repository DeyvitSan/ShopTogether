package com.deyvieat.shoptogether.features.products.domain.usecases

import com.deyvieat.shoptogether.features.products.domain.repositories.ProductsRepository; import javax.inject.Inject

class CreateProductUseCase @Inject constructor(private val repo: ProductsRepository) {
    suspend operator fun invoke(roomId: String, name: String, price: Double, stock: Int, imageUrl: String) =
        repo.createProduct(roomId, name, price, stock, imageUrl)
}
