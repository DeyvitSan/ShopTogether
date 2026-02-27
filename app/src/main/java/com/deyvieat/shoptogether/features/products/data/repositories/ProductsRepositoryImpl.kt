package com.deyvieat.shoptogether.features.products.data.repositories

import com.deyvieat.shoptogether.features.products.data.datasources.local.dao.ProductDao
import com.deyvieat.shoptogether.features.products.data.datasources.remote.api.ProductsApi
import com.deyvieat.shoptogether.features.products.data.datasources.remote.mapper.toDomain
import com.deyvieat.shoptogether.features.products.data.datasources.remote.mapper.toEntity
import com.deyvieat.shoptogether.features.products.data.datasources.remote.models.CreateProductRequestDto
import com.deyvieat.shoptogether.features.products.domain.entities.Product
import com.deyvieat.shoptogether.features.products.domain.repositories.ProductsRepository
import kotlinx.coroutines.flow.Flow; import kotlinx.coroutines.flow.map; import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val api: ProductsApi, private val dao: ProductDao) : ProductsRepository {

    override fun observeProducts(roomId: String): Flow<List<Product>> = dao.observeByRoom(roomId).map { it.map { e -> e.toDomain() } }

    override suspend fun refreshProducts(roomId: String): Result<Unit> = runCatching { dao.upsertAll(api.getProducts(roomId).map { it.toEntity() }) }

    override suspend fun createProduct(roomId: String, name: String, price: Double, stock: Int, imageUrl: String): Result<Product> = runCatching { val p = api.createProduct(roomId, CreateProductRequestDto(name, price, stock, imageUrl)); dao.upsertAll(listOf(p.toEntity())); p.toEntity().toDomain() }
}
