package com.example.pizzajoe.data

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.example.pizzajoe.R

object DataSource {

    val flavorOptions = listOf( // пары изображения пиццы и вкусов
        Pair(R.drawable.margarita, R.string.margarita),
        Pair(R.drawable.pepperoni, R.string.pepperoni),
        Pair(R.drawable.cheeseburger, R.string.cheeseburger),
        Pair(R.drawable.meat, R.string.meat),
        Pair(R.drawable.fourcheese, R.string.fourcheese),
        Pair(R.drawable.hunter, R.string.hunter),
        Pair(R.drawable.bbq, R.string.bbq),
        Pair(R.drawable.summermood, R.string.summermood),
        Pair(R.drawable.spicy, R.string.spicy),
        Pair(R.drawable.havayi, R.string.havayi),
    )
    val priceForFlavor = listOf(
        Pair(R.string.margarita, 500),
        Pair(R.string.pepperoni, 500),
        Pair(R.string.cheeseburger, 550),
        Pair(R.string.meat, 550),
        Pair(R.string.fourcheese, 600),
        Pair(R.string.hunter, 600),
        Pair(R.string.bbq, 650),
        Pair(R.string.summermood, 650),
        Pair(R.string.spicy, 700),
        Pair(R.string.havayi, 700)
    )

    val quantityOptions = listOf(
        R.string.one_pizza,
        R.string.two_pizza,
        R.string.three_pizza
    )

    val sizeOptions = listOf(
        R.string.twenty_five_size,
        R.string.thirty_two_size,
        R.string.fourty_size
    )
}