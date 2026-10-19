package com.uaslp.comedorapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuCatalogScreen(
    cartItemCount: Int,
    onAddToCart: (Dish) -> Unit,
    onNavigateToCart: () -> Unit,
    onBack: () -> Unit
) {
    // Filtro de día seleccionado ("Todos" o de Lunes a Viernes)
    val filterOptions = listOf("Todos") + MockRepository.daysOfWeek
    var selectedDay by remember { mutableStateOf("Lunes") }

    val filteredDishes = remember(selectedDay) {
        if (selectedDay == "Todos") {
            MockRepository.weeklyMenu
        } else {
            MockRepository.weeklyMenu.filter { it.dayOfWeek == selectedDay }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Menú") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToCart) {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge { Text(cartItemCount.toString()) }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Ir al carrito")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (cartItemCount > 0) {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Platillos seleccionados: $cartItemCount",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Button(onClick = onNavigateToCart) {
                            Text("Ver Carrito")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Selector horizontal de días (Lunes a Viernes)
            Text(
                text = "Selecciona el día de la semana para preordenar:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { day ->
                    val isSelected = day == selectedDay
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedDay = day },
                        label = { Text(day) },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else null
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Lista de platillos del día seleccionado
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(filteredDishes, key = { it.id }) { dish ->
                    DishItemCard(
                        dish = dish,
                        onAddClick = { onAddToCart(dish) }
                    )
                }
            }
        }
    }
}

@Composable
fun DishItemCard(
    dish: Dish,
    onAddClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Encabezado con Día y Precio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SuggestionChip(
                    onClick = { },
                    label = { Text(dish.dayOfWeek) }
                )
                Text(
                    text = "$${String.format("%.2f", dish.price)} MXN",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Nombre del platillo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = dish.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Descripción del platillo
            Text(
                text = dish.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Guarniciones incluidas
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Guarniciones incluidas:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = dish.sides.joinToString(separator = " • "),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Botón para agregar al carrito
            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AddShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar al Carrito")
            }
        }
    }
}
