package com.deyvieat.shoptogether.features.products.data.repositories

import com.deyvieat.shoptogether.features.products.data.datasources.local.dao.ProductDao
import com.deyvieat.shoptogether.features.products.data.datasources.remote.api.ProductsApi
import com.deyvieat.shoptogether.features.products.data.datasources.remote.mapper.*
import com.deyvieat.shoptogether.features.products.data.datasources.remote.models.CreateProductRequestDto
import com.deyvieat.shoptogether.features.products.domain.entities.Product
import com.deyvieat.shoptogether.features.products.domain.repositories.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val api: ProductsApi,
    private val dao: ProductDao
) : ProductsRepository {

    override fun observeProducts(roomId: String): Flow<List<Product>> =
        dao.observeByRoom(roomId)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun refreshProducts(roomId: String): Result<Unit> =
        runCatching {
            val remote = api.getProducts()
                .filter { it.roomId == roomId }

            dao.upsertAll(remote.map { it.toEntity() })
        }

    override suspend fun createProduct(
        roomId: String,
        name: String,
        price: Double,
        stock: Int,
        imageUrl: String
    ): Result<Product> =
        runCatching {
            val created = api.createProduct(
                CreateProductRequestDto(
                    name = name,
                    price = price,
                    stock = stock,
                    roomId = roomId,
                    imageUrl = imageUrl
                )
            )

            dao.upsertAll(listOf(created.toEntity()))
            created.toEntity().toDomain()
        }
}