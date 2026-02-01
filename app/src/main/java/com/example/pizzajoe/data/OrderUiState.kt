package com.example.pizzajoe.data

// класс, представляющий текущее состояние пользовательского интерфейса, хранящий данные для отображения на экране
data class OrderUiState (

    // вкус (маргарита, пепперони, и т.д.)
    val flavor: String = "",

    // размер (25, 32, 40)
    val size: Int = 25,

    // количество (1, 2, 3)
    val quantity: Int = 1,

    // выбранная дата получения
    val date: String = "",

    // общая стоимость заказа
    val price: String = "",

    // доступные даты получения заказа
    val pickupOptions: List<String> = listOf()
)