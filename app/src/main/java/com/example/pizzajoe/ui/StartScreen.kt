package com.example.pizzajoe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzajoe.R
import com.example.pizzajoe.data.DataSource
import com.example.pizzajoe.data.DataSource.priceForFlavor
import com.example.pizzajoe.ui.theme.PizzaJoeTheme

// ЭТО ПЕРВЫЙ ЭКРАН - ЭКРАН ВЫБОРА ВКУСА
// это UI-элемент (user interface - интерфейс пользователя)

@Composable
fun StartScreen(  // на вход принимаем:
    flavorOptions: List<Pair<Int, Int>>, // список пар изображение + фактическое название пиццы
    onNextButtonClicked: (String) -> Unit, // "наКнопкуКликаДальше", принимаем выбранный вкус
    modifier: Modifier = Modifier // и данные модификатора
) {
    LazyColumn {
        // 1 ЗАГОЛОВОК С ПРИВЕТСТВИЕМ
        item { // разделяем экран на 3 компонента, т.к. так требует LazyColumn
            HorizontalDivider(
                thickness = dimensionResource(R.dimen.thickness_divider),
                color = MaterialTheme.colorScheme.onSecondary
            )
            Box( // текст пришлось поместить в Бокс чтобы выставить его по центру
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    // Текст-заголовок, приветствие
                    text = stringResource(R.string.welcome_text),
                    //style = MaterialTheme.typography.headlineMedium,
                    fontSize = 20.sp
                )
            }
            HorizontalDivider(
                thickness = dimensionResource(R.dimen.thickness_divider),
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        // 2 ТЕКСТ С АГИТАЦИЕЙ СКОРЕЙШЕГО ЗАКАЗА
        item {
            Box( // текст пришлось поместить в Бокс чтобы выставить его по центру
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text( // Текст - с агитацией скорейшего заказа
                    text = stringResource(R.string.select_pizza),
                    //style = MaterialTheme.typography.headlineSmall,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                )
            }
            HorizontalDivider(
                thickness = dimensionResource(R.dimen.thickness_divider),
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        // 3 САМ КОНТЕНТ - ДАННЫЕ О ПИЦЦЕ
        items(flavorOptions) { item ->

            Row( // Строка с контентом
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                horizontalArrangement = Arrangement.Start
            ) {
                Box( // бокс с изображением
                    Modifier.size(width = 225.dp, height = 250.dp)
                ) {
                    Image(
                        bitmap = ImageBitmap.imageResource(item.first),
                        contentDescription = "PizzaImage",
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Box( // бокс с названием и кнопкой
                    Modifier.fillMaxWidth().padding(start = 0.dp, top = 2.dp, end = 2.dp, bottom = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column ( // колонна с названием и кнопкой ниже
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight()
                    ){
                        Spacer(modifier = Modifier.height(15.dp))

                        // наименование пиццы
                        val pizzaName = stringResource(item.second)
                        Text (
                            text = pizzaName,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center // здесь по центру, чтобы при переносе строки было выравнивание по центру строки
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // цена пиццы
                        var pizzaPrice = 0
                        for (pizza in priceForFlavor) {
                            if(stringResource(pizza.first) == pizzaName) pizzaPrice = pizza.second
                        }
                        Text (
                            text = stringResource(R.string.price) + " " + pizzaPrice + "₽",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center // здесь по центру, чтобы при переносе строки было выравнивание по центру строки
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // кнопка "заказать"
                        Button(
                            onClick = { onNextButtonClicked( pizzaName ) },
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Заказать")
                        }
                    }
                }

            }
            HorizontalDivider(
                thickness = dimensionResource(R.dimen.thickness_divider),
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

    }
}


@Preview
@Composable
fun StartScreenPreview() {
    PizzaJoeTheme {
        StartScreen(
            flavorOptions = DataSource.flavorOptions,
            onNextButtonClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
