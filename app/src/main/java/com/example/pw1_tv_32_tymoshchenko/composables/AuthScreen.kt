package com.example.pw1_tv_32_tymoshchenko.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Proсунутий екран авторизації та реєстрації для енергетичної системи.
 * Включає валідацію полів, стани та розділення на менші composable-функції.
 */
@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    // Стан перемикання між режимом Входу (true) та Реєстрації (false)
    var isLoginMode by remember { mutableStateOf(true) }

    // Поля введення
    var loginInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }

    // Стан повідомлення та помилки
    var message by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Компонент заголовка
        AuthHeader(isLoginMode = isLoginMode)

        Spacer(modifier = Modifier.height(24.dp))

        // Поле введення логіну / email
        OutlinedTextField(
            value = loginInput,
            onValueChange = { loginInput = it },
            label = { Text("Логін / Електронна пошта") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Поле введення пароля
        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Додаткове поле для Реєстрації
        if (!isLoginMode) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = confirmPasswordInput,
                onValueChange = { confirmPasswordInput = it },
                label = { Text("Підтвердіть пароль") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Відображення повідомлення про помилку або успіх
        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Кнопка підтвердження дії
        Button(
            onClick = {
                // Бізнес-логіка перевірки введених даних (валідація)
                if (loginInput.isBlank() || passwordInput.isBlank()) {
                    message = "Будь ласка, заповніть усі поля!"
                    isError = true
                } else if (!isLoginMode && passwordInput != confirmPasswordInput) {
                    message = "Паролі не збігаються!"
                    isError = true
                } else {
                    message = if (isLoginMode) "Успішна авторизація!" else "Реєстрація успішна!"
                    isError = false
                    // Перехід до основного застосунку мікрогріду
                    onAuthSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoginMode) "Увійти в систему" else "Зареєструвати диспетчера")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Кнопка перемикання режимів
        TextButton(
            onClick = {
                isLoginMode = !isLoginMode
                message = ""
                loginInput = ""
                passwordInput = ""
                confirmPasswordInput = ""
            }
        ) {
            Text(
                if (isLoginMode) "Немає акаунту? Зареєструйтеся"
                else "Вже є акаунт? Увійдіть"
            )
        }
    }
}

/**
 * Окрема Composable-функція для заголовку екрану.
 */
@Composable
fun AuthHeader(isLoginMode: Boolean) {
    Text(
        text = if (isLoginMode) "Авторизація: Енергосистема" else "Реєстрація: Енергосистема",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )
}