package com.uaslp.comedorapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// 1. Dashboard del Administrador
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateToMenuManager: () -> Unit,
    onNavigateToDispatch: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Gestión del Negocio",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            ElevatedCard(
                onClick = onNavigateToMenuManager,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.RestaurantMenu, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Gestor de Menú", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Configura, edita y publica la planeación quincenal.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            ElevatedCard(
                onClick = onNavigateToDispatch,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DeliveryDining, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Despacho y Logística", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Gestiona pedidos, valida pagos y actualiza envíos.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

// 2. Pantalla Gestor de Menú
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuManagerScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestor de Menú") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Regresar") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Acción para agregar platillo */ }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar platillo")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockRepository.weeklyMenu) { dish ->
                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = dish.dayOfWeek, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            Text(text = dish.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(text = "$${dish.price} MXN", style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = { /* Acción editar */ }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DispatchScreen(onBack: () -> Unit) {
    val mockOrders = listOf(
        mapOf("id" to "#001", "cliente" to "Benjamín López", "estado" to "Pendiente de Pago", "total" to "$95.00"),
        mapOf("id" to "#002", "cliente" to "María González", "estado" to "Preparando", "total" to "$180.00")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Despacho y Logística") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Regresar") }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockOrders) { order ->
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Pedido ${order["id"]}", fontWeight = FontWeight.Bold)
                            SuggestionChip(onClick = {}, label = { Text(order["estado"] ?: "") })
                        }
                        Text(text = "Cliente: ${order["cliente"]}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Total: ${order["total"]}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Button(onClick = { /* Acción para actualizar estado */ }) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Avanzar Estado")
                            }
                        }
                    }
                }
            }
        }
    }
}
