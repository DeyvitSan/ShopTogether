package com.deyvieat.shoptogether.core.navigation



import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.deyvieat.shoptogether.features.auth.presentation.screens.LoginScreen
import com.deyvieat.shoptogether.features.auth.presentation.screens.RegisterScreen
import com.deyvieat.shoptogether.features.rooms.presentation.screens.RoomListScreen
import com.deyvieat.shoptogether.features.rooms.presentation.screens.AuctionRoomScreen
import com.deyvieat.shoptogether.features.cart.presentation.screens.CartScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {

        composable(AppRoutes.Login.route) {
            LoginScreen(
                onSuccess = {
                    navController.navigate(AppRoutes.RoomList.route) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                },
                onGoRegister = {
                    navController.navigate(AppRoutes.Register.route)
                }
            )
        }

        composable(AppRoutes.Register.route) {
            RegisterScreen(
                onSuccess = {
                    navController.navigate(AppRoutes.RoomList.route) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.RoomList.route) {
            RoomListScreen(
                onRoomClick = { id, name ->
                    navController.navigate(
                        AppRoutes.AuctionRoom.createRoute(id, name)
                    )
                },
                onCartClick = {
                    navController.navigate(AppRoutes.Cart.route)
                }
            )
        }

        composable(
            route = AppRoutes.AuctionRoom.route,
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType },
                navArgument("roomName") { type = NavType.StringType }
            )
        ) { backStack ->

            AuctionRoomScreen(
                roomId = backStack.arguments?.getString("roomId") ?: "",
                roomName = backStack.arguments
                    ?.getString("roomName")
                    ?.replace("_", " ") ?: "",
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.Cart.route) {
            CartScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}