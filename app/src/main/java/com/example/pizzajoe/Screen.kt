package com.example.pizzajoe

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pizzajoe.data.DataSource
import com.example.pizzajoe.ui.OrderViewModel
import com.example.pizzajoe.ui.SelectOptionScreen
import com.example.pizzajoe.ui.StartScreen
import com.example.pizzajoe.ui.SummaryScreen


enum class PizzaJoeScreen(@StringRes val title: Int) { // класс с перечислением имён экранов
    // @StringRes указывает, что передаваемое целое число является строковым ресурсом (из values/strings.xml)
    Start(title = R.string.start_name),
    Size(title = R.string.choose_size),
    Quantity(title = R.string.choose_quantity),
    Pickup(title = R.string.choose_pickup_date),
    Summary(title = R.string.order_summary)
}

// функция, описывающая верхний бар и кнопку "назад", чтобы это всё не громоздить в Scaffold
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzaJoeAppBar(
    currentScreen: PizzaJoeScreen, // принимаем название текущего экрана
    canNavigateBack: Boolean, // инфу, есть ли предыдущий экран в стеке
    navigateUp: () -> Unit, // переход на нужный экран
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) }, // задаём заголовок для этого экрана
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer // используем стандартные стили для цветов, адаптируемые под тему
        ),
        modifier = Modifier, // используем стандартный модификатор
        navigationIcon = {
            if (canNavigateBack) { // если возможен переход на один экран назад,
                IconButton(onClick = navigateUp) { // и если нажали, то переходим на предыдущий экран
                    Icon( // то отображается кнопка
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, // вот такая
                        contentDescription = stringResource(R.string.back_Button) // и резервное описание кнопки
                    )
                }
            }
        },
        actions = {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(70.dp)
            )
        }
    )

}

@Composable
fun PizzaJoeApp(
    viewModel: OrderViewModel = viewModel(), // viewModel() это встроенная функция в compose, возвращающая экземпляр класса viewModel
    navController: NavHostController = rememberNavController() // и так же создаем объект класса navController
) {
    // Кнопка «назад» должна отображаться только при наличии компонуемого элемента в стеке. Если у приложения нет экранов в стеке (StartOrderScreen если отображается), кнопка «назад» не должна отображаться. Для проверки этого необходима ссылка на стек.
    val backStackEntry by navController.currentBackStackEntryAsState() // entry - "вход"
    val currentScreen = PizzaJoeScreen.valueOf(backStackEntry?.destination?.route ?: PizzaJoeScreen.Start.name) // (если стек = null : тогда результат - первый экран)

    // valueOf - встроенная функция в enum
    // `backStackEntry` Это объект типа NavBackStackEntry, представляющий текущий элемент стека навигации.
    // `.destination` Свойство destination возвращает объект типа NavDestination, который представляет цель текущего маршрута (route) в стеке навигации.
    // `.route` Свойство route возвращает строку, представляющую путь (маршрут) текущего экрана в навигационном графе.
    // `?:` (Elvis operator) Если значение слева от оператора равно null, оператор вернёт значение справа.
    // `CupcakeScreen.Start.name` Если свойство route равно null, будет возвращено строковое представление имени стартового экрана класса CupcakeScreen.

    // Полностью интерпретируемое выражение означает следующее:
    //Если текущий экран имеет определённый маршрут (route), то возвращается этот маршрут. Иначе используется начальное состояние экрана (Start.name) из перечисления CupcakeScreen.
    //Таким образом, эта конструкция обеспечивает получение пути текущего экрана, обеспечивая запасной вариант, если текущий экран не имеет явно заданного маршрута.

    Scaffold(
        topBar ={
            PizzaJoeAppBar(
                currentScreen = currentScreen, // передаем имя текущего экрана
                canNavigateBack = navController.previousBackStackEntry != null, // "previous" - предыдущий - есть ли предыдущий пункт назначения
                navigateUp = { navController.navigateUp() }
            )
        }
    ) {
        innerPadding -> val uiState by viewModel.uiState.collectAsState() // объявление переменной -> выполняемое действие
        // "внутренний отступ"
        // by - в. это делегирование. by говорит что все вызовы uiState будут делегироваться сюда: viewModel.uiState.collectAsState()
        // с помощью collectAsState получаем текущее состояние потока из uiState

        // создаем NavHost
        // это специальный компонент, который добавляется в макет пользовательского интерфейса действия и служит заполнителем для страниц, по которым будет перемещаться пользователь.
        //При вызове в NavHost передается объект NavHostController, компонент, который будет служить начальным экраном/начальным пунктом назначения

        Box(modifier = Modifier.fillMaxSize()) {
            // Добавляем изображение фона
            Image(
                painter = painterResource(id = R.drawable.background_image_1),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            NavHost(
                navController = navController,
                startDestination = PizzaJoeScreen.Start.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                // вызов 1го экрана
                composable(route = PizzaJoeScreen.Start.name) {
                    StartScreen(
                        flavorOptions = DataSource.flavorOptions,
                        onNextButtonClicked = {
                            viewModel.setFlavor(it)
                            navController.navigate(PizzaJoeScreen.Size.name)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                // вызов 2го экрана
                composable(route = PizzaJoeScreen.Size.name) {
                    val context = LocalContext.current
                    SelectOptionScreen(
                        subtotal = uiState.price,
                        additive = stringResource(R.string.size_cm),
                        options = DataSource.sizeOptions.map { id -> context.resources.getString(id) },
                        onSelectionChanged = { viewModel.setSize(it) },
                        onNextButtonClicked = { navController.navigate(PizzaJoeScreen.Quantity.name) },
                        onCancelButtonClicked = {
                            cancelOrderAndNavigateToStart(
                                viewModel,
                                navController
                            )
                        },
                        modifier = Modifier.fillMaxHeight()
                    )
                }
                // вызов 3го экрана
                composable(route = PizzaJoeScreen.Quantity.name) {
                    val context = LocalContext.current
                    SelectOptionScreen(
                        subtotal = uiState.price,
                        additive = stringResource(R.string.piece),
                        options = DataSource.quantityOptions.map { id ->
                            context.resources.getString(
                                id
                            )
                        },
                        onSelectionChanged = { viewModel.setQuantity(it) },
                        onNextButtonClicked = { navController.navigate(PizzaJoeScreen.Pickup.name) },
                        onCancelButtonClicked = {
                            cancelOrderAndNavigateToStart(
                                viewModel,
                                navController
                            )
                        },
                        modifier = Modifier.fillMaxHeight()
                    )
                }
                // вызов 4го экрана
                composable(route = PizzaJoeScreen.Pickup.name) {
                    SelectOptionScreen(
                        subtotal = uiState.price,
                        options = uiState.pickupOptions,
                        onSelectionChanged = { viewModel.setDate(it) },
                        onNextButtonClicked = { navController.navigate(PizzaJoeScreen.Summary.name) },
                        onCancelButtonClicked = {
                            cancelOrderAndNavigateToStart(
                                viewModel,
                                navController
                            )
                        },
                        modifier = Modifier.fillMaxHeight()
                    )
                }
                // 5 итоговый экран
                composable(route = PizzaJoeScreen.Summary.name) {
                    val context =
                        LocalContext.current // получаем контекст для использования ф-ции отправки в другое приложение
                    SummaryScreen(
                        orderUiState = uiState,
                        onCancelButtonClicked = {
                            cancelOrderAndNavigateToStart(viewModel, navController)
                        },
                        onSendButtonClicked = { subject: String, summary: String ->
                            shareOrder(context, subject, summary)
                        },
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }


        }
    }
}
// это будет функция возврата к начальному экрану, её будем передавать при нажатии кнопки отмена на каждом из экранов
private fun cancelOrderAndNavigateToStart( viewModel: OrderViewModel, navController: NavHostController) {
    viewModel.resetOrder() // сбрасываем состояние заказа
    navController.popBackStack(PizzaJoeScreen.Start.name, inclusive = false) // выполняем переход на стартовый экран.
    // inclusive: логическое значение, которое при значении true также удаляет указанный маршрут. При значении false popBackStack()удаляет все пункты назначения, расположенные выше начального пункта назначения (кроме него), оставляя его в качестве самого верхнего экрана, видимого пользователю.
}

// это будет функция отправки в другое приложениие - типо "поделиться"
private fun shareOrder(context: Context, subject: String, summary: String) { // summary - "краткое содержание"
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain" // указание типа отправляемых данных
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    // теперь открываем новое активити
    context.startActivity(
        Intent.createChooser( // "создать выборщик"
            intent, context.getString(R.string.new_order)
        )
    )
}