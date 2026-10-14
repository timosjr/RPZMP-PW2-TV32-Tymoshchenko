package com.example.pw1_tv_32_tymoshchenko

/**
 * Sealed-клас для визначення маршрутів навігації в застосунку мікрогріду.
 */
sealed class Screen(val route: String) {
    sealed class AdvancedScreen(val title: String) : Screen(title) {
        object MainScreen : AdvancedScreen("microgrid_main_menu")
        object UserInfo : AdvancedScreen("microgrid_user_info")
        object SolarForecast : AdvancedScreen("microgrid_solar_forecast")
        object SolarDetails : AdvancedScreen("microgrid_solar_details")
    }
}