package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalc.ui.theme.TipCalcTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalcTheme {
                TipCalculator()
            }
        }
    }
}

@Composable
fun TipCalculator() {

    var orderAmount by remember { mutableStateOf("") }
    var dishesCount by remember { mutableStateOf("") }
    var tipPercent by remember { mutableFloatStateOf(15f) }

    val discountPercent = when (dishesCount.toIntOrNull() ?: 0) {
        in 1..2 -> 3
        in 3..5 -> 5
        in 6..10 -> 7
        in 11..Int.MAX_VALUE -> 10
        else -> 0
    }

    // Получаем сумму заказа
    val amount = orderAmount.replace(',', '.').toDoubleOrNull() ?: 0.0

    // Сумма скидки
    val discountAmount = amount * discountPercent / 100.0

    // Сумма после скидки
    val amountAfterDiscount = amount - discountAmount

    // Сумма чаевых
    val tipAmount = amountAfterDiscount * tipPercent / 100.0

    // Итоговая сумма
    val totalAmount = amountAfterDiscount + tipAmount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Калькулятор чаевых",
            fontSize = 26.sp
        )
        Spacer(modifier = Modifier.height(30.dp))

        // Сумма заказа
        Text(
            text = "Сумма заказа",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = orderAmount,
            onValueChange = { orderAmount = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Введите сумму заказа") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Количество блюд
        Text(
            text = "Количество блюд",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = dishesCount,
            onValueChange = { dishesCount  = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Введите количество блюд") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Слайдер чаевых
        Text(
            text = "Процент чаевых: ${tipPercent.toInt()}%",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Slider(
            value = tipPercent,
            onValueChange = { tipPercent = it },
            valueRange = 0f..25f,
            steps = 24,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0%")
            Text("25%")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Скидка за количество блюд",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val discounts = listOf(3, 5, 7, 10)

            discounts.forEach { discount ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = discountPercent == discount,
                        onClick = null
                    )

                    Text(
                        text = "Скидка $discount%",
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(25.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Результат расчёта",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        ResultRow(
            title = "Сумма заказа:",
            value = "%.2f".format(amount)
        )

        ResultRow(
            title = "Скидка (${discountPercent}%):",
            value = "- %.2f".format(discountAmount)
        )

        ResultRow(
            title = "После скидки:",
            value = "%.2f".format(amountAfterDiscount)
        )

        ResultRow(
            title = "Чаевые (${tipPercent.toInt()}%):",
            value = "%.2f".format(tipAmount)
        )

        Spacer(modifier = Modifier.height(15.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(15.dp))

        ResultRow(
            title = "ИТОГО К ОПЛАТЕ:",
            value = "%.2f".format(totalAmount),
            isTotal = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    TipCalcTheme {
        TipCalculator()
    }
}


@Composable
fun ResultRow(
    title: String,
    value: String,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal)
                androidx.compose.ui.text.font.FontWeight.Bold
            else
                androidx.compose.ui.text.font.FontWeight.Normal
        )

        Text(
            text = value,
            fontSize = if (isTotal) 20.sp else 16.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}
