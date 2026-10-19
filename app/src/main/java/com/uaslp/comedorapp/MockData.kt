package com.uaslp.comedorapp

// Modelo para el Catálogo de Menú (Lunes a Viernes)
data class Dish(
    val id: Int,
    val dayOfWeek: String,
    val name: String,
    val description: String,
    val price: Double,
    val sides: List<String>
)

// Modelo para Promociones y Anuncios del Dashboard
data class Promotion(
    val id: Int,
    val title: String,
    val detail: String,
    val tag: String
)

// Datos de prueba estáticos provisionales
object MockRepository {

    val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")

    val promotions = listOf(
        Promotion(
            id = 1,
            title = "Semana Completa con 15% de Descuento",
            detail = "Preordena tus 5 comidas de lunes a viernes antes del domingo.",
            tag = "Tiempo limitado"
        ),
        Promotion(
            id = 2,
            title = "Envío sin costo en Zona Universitaria",
            detail = "Aplica en pedidos programados con geolocalización activa.",
            tag = "Anuncio"
        )
    )

    val weeklyMenu = listOf(
        Dish(
            id = 1,
            dayOfWeek = "Lunes",
            name = "Pechuga a la Plancha con Finas Hierbas",
            description = "Pechuga de pollo marinada acompañada de ensalada fresca.",
            price = 95.0,
            sides = listOf("Arroz blanco", "Ensalada verde", "Frijoles de la olla")
        ),
        Dish(
            id = 2,
            dayOfWeek = "Lunes",
            name = "Enchiladas Potosinas Tradicionales",
            description = "Orden de 5 enchiladas rellenas de queso con guacamole y crema.",
            price = 85.0,
            sides = listOf("Frijoles refritos", "Guacamole", "Cebolla curtida")
        ),
        Dish(
            id = 3,
            dayOfWeek = "Martes",
            name = "Filete de Pescado al Ajillo",
            description = "Filete basa dorado al ajillo con toque de limón.",
            price = 110.0,
            sides = listOf("Arroz a la jardinera", "Verduras al vapor")
        ),
        Dish(
            id = 4,
            dayOfWeek = "Miércoles",
            name = "Fajitas de Res con Pimientos",
            description = "Tiras de res salteadas con pimiento morrón y cebolla.",
            price = 115.0,
            sides = listOf("Arroz rojo", "Frijoles charros", "Tortillas de maíz")
        ),
        Dish(
            id = 5,
            dayOfWeek = "Jueves",
            name = "Milanesa de Pollo Empanizada",
            description = "Milanesa crujiente recién preparada con guarnición doble.",
            price = 95.0,
            sides = listOf("Puré de papa", "Ensalada de lechuga y jitomate")
        ),
        Dish(
            id = 6,
            dayOfWeek = "Viernes",
            name = "Salpicón de Res Fresco",
            description = "Carne deshebrada con jitomate, aguacate, rábano y vinagreta ligera.",
            price = 100.0,
            sides = listOf("Tostadas horneadas", "Sopa fría de coditos")
        )
    )
}
