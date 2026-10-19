package com.uaslp.comedorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uaslp.comedorapp.ui.theme.ComedorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComedorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigationGraph()
                }
            }
        }
    }
}

@Composable
fun AppNavigationGraph() {
    val navController = rememberNavController()

    val cartItems = remember { mutableStateListOf<Dish>() }

    NavHost(navController = navController, startDestination = "login") {

        // 1. Autenticación
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("dashboard") { popUpTo("login") { inclusive = true } } },
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToRecovery = { navController.navigate("recovery") }
            )
        }

        // 2. Registro
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // 3. Recuperación de contraseña
        composable("recovery") {
            PasswordRecoveryScreen(
                onRecoverySuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // 4. Dashboard Principal
        composable("dashboard") {
            DashboardScreen(
                cartItemCount = cartItems.size,
                onNavigateToMenu = { navController.navigate("menu") },
                onNavigateToCart = { navController.navigate("cart") },
                onLogout = {
                    navController.navigate("login") { popUpTo("dashboard") { inclusive = true } }
                }
            )
        }

        // 5. Catálogo de Menú
        composable("menu") {
            MenuCatalogScreen(
                cartItemCount = cartItems.size,
                onAddToCart = { dish -> cartItems.add(dish) },
                onNavigateToCart = { navController.navigate("cart") },
                onBack = { navController.popBackStack() }
            )
        }

        // 6. Carrito de Compras
        composable("cart") {
            CartScreen(
                cartItems = cartItems,
                onRemoveItem = { dish -> cartItems.remove(dish) },
                onProceedToCheckout = { navController.navigate("checkout") },
                onBack = { navController.popBackStack() }
            )
        }

        // 7. Pago y Envío
        composable("checkout") {
            CheckoutScreen(
                cartItems = cartItems,
                onOrderComplete = {
                    cartItems.clear()
                    navController.navigate("dashboard") { popUpTo("dashboard") { inclusive = true } }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
