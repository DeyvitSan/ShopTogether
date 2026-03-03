package com.deyvieat.shoptogether.features.cart.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deyvieat.shoptogether.core.session.SessionManager
import com.deyvieat.shoptogether.features.cart.domain.entities.CartItem
import com.deyvieat.shoptogether.features.cart.domain.usecases.GetCartUseCase
import com.deyvieat.shoptogether.features.cart.domain.repositories.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartUiState(val isLoading: Boolean = false, val items: List<CartItem> = emptyList())

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCart: GetCartUseCase,
    private val cartRepo: CartRepository,
    private val session: SessionManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            session.userId.filterNotNull().collect { userId ->
                getCart(userId)
                    .onEach { items: List<CartItem> -> 
                        _uiState.update { state -> state.copy(items = items, isLoading = false) } 
                    }
                    .launchIn(this)
                cartRepo.refreshCart(userId)
            }
        }
    }
}
