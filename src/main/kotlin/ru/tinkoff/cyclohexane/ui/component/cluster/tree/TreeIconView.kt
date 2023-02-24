package ru.tinkoff.cyclohexane.ui.component.cluster.tree

import androidx.compose.material.Colors
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.tinkoff.cyclohexane.ui.common.AppTheme

@Composable
fun TreeIconView(model: TreeModel) =
    if (model.expandable) {
        val color = when (model.connectable) {
            true -> {
                if ((model as Connectable).isConnected()) {
                    Color.Green
                } else {
                    Color.Red
                }
            }

            false -> LocalContentColor.current
        }
        if ((model as Expandable).isExpanded()) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = color)
        } else {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = color)
        }
    } else Unit
