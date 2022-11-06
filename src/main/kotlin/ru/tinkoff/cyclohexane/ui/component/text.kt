package ru.tinkoff.cyclohexane.ui.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun Text10(text: String) {
    Text(fontSize = 10.sp, text = text)
}

@Composable
fun AppText(text: String) {
    Text10(text)
}
