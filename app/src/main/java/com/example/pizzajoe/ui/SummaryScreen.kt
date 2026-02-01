package com.example.pizzajoe.ui

import android.inputmethodservice.Keyboard.Row
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pizzajoe.R
import com.example.pizzajoe.data.OrderUiState
import com.example.pizzajoe.ui.components.FormattedPriceLabel
import kotlinx.coroutines.launch


// ЭТО пятый ЭКРАН - ЭКРАН РЕЗУЛЬТАТА ЗАКАЗА
// это UI-элемент (user interface - интерфейс пользователя)

@Composable
fun SummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val resources = LocalContext.current.resources

    //Загрузить и отформатировать строковый ресурс с параметрами - это текст, который будем отправлять
    val orderSummary = stringResource(
        R.string.order_details,
        orderUiState.flavor,
        orderUiState.size,
        orderUiState.quantity,
        orderUiState.date,
        orderUiState.price
    )

    val newOrder = stringResource(R.string.new_order)
    //Создаем список сводок заказов для отображения
    val items = listOf(
        Pair(stringResource(R.string.flavor), orderUiState.flavor),
        Pair(
            stringResource(R.string.size),
            orderUiState.size.toString() + " " + stringResource(R.string.size_cm)
        ),
        Pair(
            stringResource(R.string.quantity),
            orderUiState.quantity.toString() + " " + stringResource(R.string.piece)
        ),
        Pair(stringResource(R.string.pickup_date), orderUiState.date)
    )

    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
    ) {

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                items.forEach { item ->
                    Text(item.first.uppercase())
                    Text(text = item.second, fontWeight = FontWeight.Bold)
                    HorizontalDivider(thickness = dimensionResource(R.dimen.thickness_divider))
                }
                SnackbarHost(hostState = snackBarHostState) // ставим эту "метку" сюда, и тогда этот snack отобразится именно в этом положении на экране!
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                FormattedPriceLabel(
                    subtotal = orderUiState.price,
                    modifier = Modifier.align(Alignment.End)
                )

                Row(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = resources.getString(R.string.send_order), // stringResource(R.string.send_order) не работает, жалуется на composable. вверху мы импортировали ресурсы и можно таким образом получить строку
                                    )
                                    onSendButtonClicked(newOrder, orderSummary)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.surface,
                                disabledContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(stringResource(R.string.send))
                        }
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
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
                    }


                }
            }
        }
    }


}