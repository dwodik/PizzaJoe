package com.example.pizzajoe.ui

import android.app.Application
import android.text.Html
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.pizzajoe.R
import com.example.pizzajoe.data.DataSource
import com.example.pizzajoe.data.DataSource.priceForFlavor
import com.example.pizzajoe.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// надбавка за доставку в тот же день
private const val PRICE_FOR_SAME_DAY_PICKUP = 150
// коэффициент увеличения стоимости пиццы, если она выбрана не 25см
private const val COST_INCREASE_FACTOR_32 = 1.3
private const val COST_INCREASE_FACTOR_40 = 1.8


// Основная цель ViewModel — обеспечить работу с данными с помощью пользовательского интерфейса
class OrderViewModel(application: Application): AndroidViewModel(application) {

    // вот так получаем список пар из DataSource. напрямую не получается потому что к stringResource доступ есть только из Composable
    private val pizzaPrice = priceForFlavor.map { pair ->
        Pair(application.getString(pair.first), pair.second)
    }


    // запоминаем текущее состояние с помощью класса OrderUiState, передаём ему список четырех дат
    private val _uiState = MutableStateFlow( OrderUiState(pickupOptions = pickupOptions()))
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow() // записываем в переменную как состояние потока

    // установка вкуса пиццы для этого состояния заказа и обновление цены

    fun setFlavor(desiredFlavor: String) { // desired - желанный
        // пока что делаем так, что от вкуса пиццы не зависит цена
        _uiState.update { currentState ->
            currentState.copy(
                flavor = desiredFlavor,
                price = calculatePrice(flavor = desiredFlavor)
            )
        }
    }

    // установка размера пиццы для этого состояния заказа и обновление цены
    fun setSize(desiredSize: String) {
        _uiState.update { currentState ->
            currentState.copy(
                size = desiredSize.toInt(),
                price = calculatePrice(size = desiredSize.toInt())
            )
        }
    }

    // установка количества пицц для этого состояния заказа и обновление цены
    fun setQuantity(numberPizza: String) {
        _uiState.update { currentState ->
            currentState.copy(
                quantity = numberPizza.toInt(),
                price = calculatePrice(quantity = numberPizza.toInt())
            )
        }
    }
    // установка даты для этого состояния заказа и обновление цены
    fun setDate(pickupDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                date = pickupDate,
                price = calculatePrice(pickupDate = pickupDate)
            )
        }
    }
    // сброс состояния заказа
    fun resetOrder() {
        _uiState.value = OrderUiState(pickupOptions = pickupOptions())
    }



    // функция подсчета общей цены
    private fun calculatePrice( // получаем вкус, размер, количество пицц, дату доставки
        flavor: String = _uiState.value.flavor,
        size: Int = _uiState.value.size,
        quantity: Int = _uiState.value.quantity,
        pickupDate: String = _uiState.value.date
    ):String {
        // вводим стоимость пицц
        var pricePizza = 0
        for (item in pizzaPrice) {
            if( item.first == flavor) pricePizza = item.second.toInt()
        }
        // вводим коэффициент увеличения цены если пицца больше 25см
        var coefficient = 1.0
        if(size == 32) coefficient = COST_INCREASE_FACTOR_32
        if(size == 40) coefficient = COST_INCREASE_FACTOR_40

        // стоимость заказа = количество * цена * коэффициент
        var calculatedPrice = (quantity * pricePizza * coefficient).toInt()

        // если дата доставки - сегодня, то прибавляем к стоимости надбавку
        if (pickupOptions()[0] == pickupDate) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        return "$calculatedPrice \u20BD"
    }

    // возвращает список последующих трёх дат после сегодняшней
    private fun pickupOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        val formatter = SimpleDateFormat("EEEE, d MMMM", Locale("ru", "RUS"))
        val calendar = Calendar.getInstance()

        // repeat - повторить
        repeat(4) {
            dateOptions.add(formatter.format(calendar.time)) // добавляем сегодняшнюю дату в массив
            calendar.add(Calendar.DATE, 1) // и увеличиваем дату на один день
            // итого добавляем 4 дня
        }
        return dateOptions
    }



}




