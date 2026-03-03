package com.deyvieat.shoptogether.core.navigation

sealed class AppRoutes(val route: String) {

    data object Login : AppRoutes("login")
    data object Register : AppRoutes("register")
    data object RoomList : AppRoutes("rooms")
    data object Cart : AppRoutes("cart")

    data object AuctionRoom :
        AppRoutes("room/{roomId}/{roomName}") {

        fun createRoute(roomId: String, roomName: String) =
            "room/$roomId/${roomName.replace(" ", "_")}"
    }
}