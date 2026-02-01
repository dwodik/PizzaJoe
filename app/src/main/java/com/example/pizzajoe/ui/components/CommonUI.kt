package com.example.pizzajoe.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.pizzajoe.R

// Компонуемый, который отображает отформатированную [цену], которая будет отформатирована и отображена на экране
@Composable
fun FormattedPriceLabel(
    subtotal: String,
    modifier: Modifier
) {
    Text(
        text = stringResource(R.string.subtotal_price, subtotal),
        modifier = modifier,
        fontSize = 18.sp
        //style = MaterialTheme.typography.headlineSmall
    )

}