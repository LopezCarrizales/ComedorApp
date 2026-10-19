package com.uaslp.comedorapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// 1. Vista de Carrito de Compras
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<Dish>,
    onRemoveItem: (Dish) -> Unit,
    onProceedToCheckout: () -> Unit,
    onBack: () -> Unit
) {
    val totalPrice = cartItems.sumOf { it.price }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total estimado:", style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = "$${String.format("%.2f", totalPrice)} MXN",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Button(
                            onClick = onProceedToCheckout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Proceder al Pago y Envío")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "Tu carrito está vacío",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Agrega platillos desde el catálogo de menú",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(cartItems, key = { it.id }) { dish ->
                    CartItemRow(dish = dish, onRemove = { onRemoveItem(dish) })
                }
            }
        }
    }
}

@Composable
fun CartItemRow(dish: Dish, onRemove: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SuggestionChip(
                    onClick = { },
                    label = { Text(dish.dayOfWeek, style = MaterialTheme.typography.labelSmall) }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dish.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format("%.2f", dish.price)} MXN",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar platillo",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// 2. Vista de Pago y Envío
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartItems: List<Dish>,
    onOrderComplete: () -> Unit,
    onBack: () -> Unit
) {
    var address by remember { mutableStateOf("") }
    var reference by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Tarjeta de Crédito / Débito") }
    var gpsSimulated by remember { mutableStateOf(false) }

    val totalPrice = cartItems.sumOf { it.price }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pago y Envío") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Sección de Dirección y GPS
            item {
                Text(
                    text = "Dirección de Entrega",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Calle, número y colonia") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = reference,
                    onValueChange = { reference = it },
                    label = { Text("Referencias (ej. Color de fachada, entre calles)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Botón para capturar ubicación con el hardware GPS del dispositivo (no sirve uwU)
                OutlinedButton(
                    onClick = {
                        gpsSimulated = true
                        address = "Av. Venustiano Carranza #2405, Col. Los Filtros"
                        reference = "Facultad de Ingeniería - UASLP (Capturado por GPS)"
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.GpsFixed, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (gpsSimulated) "Ubicación GPS Actualizada" else "Obtener Ubicación con GPS")
                }
            }

            // Sección de Forma de Pago
            item {
                Text(
                    text = "Método de Pago",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = paymentMethod == "Tarjeta de Crédito / Débito",
                        onClick = { paymentMethod = "Tarjeta de Crédito / Débito" },
                        label = { Text("Tarjeta") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = paymentMethod == "Efectivo contra entrega",
                        onClick = { paymentMethod = "Efectivo contra entrega" },
                        label = { Text("Efectivo") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Resumen de Orden
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Resumen del Pedido (${cartItems.size} platillos)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal platillos")
                            Text("$${String.format("%.2f", totalPrice)} MXN")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Costo de envío (GPS)")
                            Text("$0.00 MXN")
                        }
                        HorizontalDivider()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total a Pagar", fontWeight = FontWeight.Bold)
                            Text(
                                "$${String.format("%.2f", totalPrice)} MXN",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Botón de Confirmación final
            item {
                Button(
                    onClick = onOrderComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Confirmar y Enviar Pedido")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
