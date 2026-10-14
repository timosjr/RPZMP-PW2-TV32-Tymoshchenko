package com.example.pw1_tv_32_tymoshchenko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.pw1_tv_32_tymoshchenko.composables.AdvancedNavHostController
import com.example.pw1_tv_32_tymoshchenko.composables.AuthScreen
import com.example.pw1_tv_32_tymoshchenko.ui.theme.PW1_TV32_TymoshchenkoTheme

/**
 * Головна активність застосунку (MainActivity), яка керує станом авторизації
 * та запускає просунуту навігацію по екранах мікрогріду після успішного входу.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PW1_TV32_TymoshchenkoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Стан для перемикання між екраном авторизації та головним меню мікрогріду
                    var isAuthenticated by remember { mutableStateOf(false) }
                    val navController = rememberNavController()

                    if (!isAuthenticated) {
                        // Екран входу / реєстрації
                        AuthScreen(
                            onAuthSuccess = {
                                isAuthenticated = true
                            }
                        )
                    } else {
                        // Після успішної авторизації відкриваємо навігатор мікрогріду
                        AdvancedNavHostController(navController = navController)
                    }
                }
            }
        }
    }
}