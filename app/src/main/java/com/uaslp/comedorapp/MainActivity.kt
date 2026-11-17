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
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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

        // 1. Autenticación (Modificada con división de flujo)
        composable("login") {
            LoginScreen(
                onLoginSuccess = { email ->
                    // Validación simple para el prototipo
                    if (email.contains("admin")) {
                        navController.navigate("admin_dashboard") { popUpTo("login") { inclusive = true } }
                    } else {
                        navController.navigate("dashboard") { popUpTo("login") { inclusive = true } }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToRecovery = { navController.navigate("recovery") }
            )
        }

        // --- RUTAS DEL CLIENTE ---
        composable("register") { RegisterScreen(onRegisterSuccess = { navController.popBackStack() }, onBack = { navController.popBackStack() }) }
        composable("recovery") { PasswordRecoveryScreen(onRecoverySuccess = { navController.popBackStack() }, onBack = { navController.popBackStack() }) }

        composable("dashboard") {
            DashboardScreen(
                cartItemCount = cartItems.size,
                onNavigateToMenu = { navController.navigate("menu") },
                onNavigateToCart = { navController.navigate("cart") },
                onLogout = { navController.navigate("login") { popUpTo("dashboard") { inclusive = true } } }
            )
        }
        composable("menu") {
            MenuCatalogScreen(
                cartItemCount = cartItems.size,
                onAddToCart = { dish -> cartItems.add(dish) },
                onNavigateToCart = { navController.navigate("cart") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("cart") {
            CartScreen(
                cartItems = cartItems,
                onRemoveItem = { dish -> cartItems.remove(dish) },
                onProceedToCheckout = { navController.navigate("checkout") },
                onBack = { navController.popBackStack() }
            )
        }
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

        composable("admin_dashboard") {
            AdminDashboardScreen(
                onNavigateToMenuManager = { navController.navigate("admin_menu_manager") },
                onNavigateToDispatch = { navController.navigate("admin_dispatch") },
                onLogout = { navController.navigate("login") { popUpTo("admin_dashboard") { inclusive = true } } }
            )
        }
        composable("admin_menu_manager") {
            MenuManagerScreen(onBack = { navController.popBackStack() })
        }
        composable("admin_dispatch") {
            DispatchScreen(onBack = { navController.popBackStack() })
        }
    }
}
