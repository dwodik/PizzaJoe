package com.example.pizzajoe.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.pizzajoe.R
import com.example.pizzajoe.ui.components.FormattedPriceLabel
import com.example.pizzajoe.ui.theme.PizzaJoeTheme

// ЭТО второй, третий, четвёртый ЭКРАН - ЭКРАН ВЫБОРА РАЗМЕРА, КОЛИЧЕСТВА, ДАТЫ
// это UI-элемент (user interface - интерфейс пользователя)

@Composable
fun SelectOptionScreen(
    subtotal: String,
    additive: String = "",
    options: List<String>,
    onSelectionChanged: (String) -> Unit = {},
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier

    /*     (String) — обозначает, что функция принимает один аргумент типа String.
           -> Unit — обозначает, что функция ничего не возвращает (возвращаемый тип Unit аналогичен типу void в Java).
           = {} — показывает, что значение по умолчанию для этого аргумента — пустое лямбда-выражение, которое не делает ничего (ничего не принимает и ничего не возвращает). Это называется необязательным параметром (default parameter value).
    Таким образом, строка целиком означает, что ожидается функция, принимающая строку и ничего не возвращающая, а если её не передать явно, то используется пустая реализация.
    */
) {
    /* - Создается переменная selectedValue, которой присваивается состояние (сначала оно пустое), хранящее свое значение даже при смене конфигураций.
       - Изменяя значение selectedValue, компилятор автоматически вызывает повторный рендеринг нужного участка композиции.
    */
    var selectedValue by rememberSaveable { mutableStateOf("") }
    /*
    - rememberSaveable: специальная функция из Jetpack Compose, предназначенная для хранения значений состояния между рендерами и восстановления сохраненных состояний при изменении конфигурации устройства (например, повороте экрана).
    - Внутри фигурных скобок передаётся инициализатор состояния: { mutableStateOf("") }. Здесь создаётся начальное состояние с пустой строкой (""), используя mutableStateOf, что даёт возможность изменять это значение позже.
    - Оператор делегирования by используется для упрощенного синтаксиса обращения к состоянию, предоставленному rememberSaveable. Благодаря этому оператору, переменную selectedValue можно воспринимать как обычную переменную, несмотря на то, что за ней скрывается объект состояния.
    */

    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                options.forEach { item ->
                    Row(
                        // Обычно такая логика применяется в ситуациях, когда необходимо реализовать интерактивные списки или меню, где пользователь может выбрать единственный элемент среди множества предложенных
                        /*
                        - .selectable(...): Метод, применяющий специальную семантику поведения для выделяемых элементов. Этот модификатор добавляет возможность выбрать элемент, изменив его визуализацию и выполнив необходимое действие при клике.
                        Параметры метода :
                               - `selected = selectedValue == item`:
                               - Проверяется условие: совпадает ли текущее значение item с ранее выбранным значением (selectedValue)? Если да, элемент выделяется визуально.
                               - Таким образом, указанный элемент становится активным ("выделенным"), если он соответствует текущему выбору.
                               - `onClick = {...}`: - Обработчик события нажатия на элемент. Выполняется каждый раз, когда пользователь нажимает на соответствующий элемент.
                        */
                        modifier = Modifier.selectable(
                            selected = selectedValue == item,
                            onClick = {
                                selectedValue = item
                                onSelectionChanged(item)
                            }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedValue == item,
                            onClick = {
                                selectedValue = item
                                onSelectionChanged(item)
                            }
                        )
                        Text(
                            text = "$item $additive",
                            fontSize = 20.sp,
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium)),
                    // разделитель в виде горизонтальной полосы
                    // thickness - толщина
                    thickness = dimensionResource(R.dimen.thickness_divider),
                    color = MaterialTheme.colorScheme.onSecondary
                )

                // относится к UI-элементу, который предназначен для отображения цены товара или услуги в приятном и понятном формате
                FormattedPriceLabel(
                    subtotal = subtotal,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(
                            top = dimensionResource(R.dimen.padding_medium),
                            bottom = dimensionResource(R.dimen.padding_medium)
                        )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_medium)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(2f),
                    onClick = onCancelButtonClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    modifier = Modifier.weight(2f),
                    enabled = selectedValue.isNotEmpty(),
                    onClick = onNextButtonClicked
                ) {
                    Text(stringResource(R.string.next))
                }

            }
        }
    }
}


// ПРОСТО ПРЕВЬЮ
@Preview
@Composable
fun SelectOptionPreview() {
    PizzaJoeTheme {
        SelectOptionScreen(
            subtotal = "299.99",
            additive = "",
            options = listOf("Option 1", "Option 2", "Option 3", "Option 4"),
            modifier = Modifier.fillMaxHeight()
        )
    }
}