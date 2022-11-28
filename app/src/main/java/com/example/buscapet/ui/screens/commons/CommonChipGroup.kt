package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.annotation.ColorRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.buscapet.R
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.accompanist.flowlayout.FlowRow

/**
 * items : list of items to be render
 * defaultSelectedItemIndex : to highlight item by default (Optional)
 * useFixedWidth : set true if you want to set fix width to item (Optional)
 * itemWidth : Provide item width if useFixedWidth is set to true (Optional)
 * cornerRadius : To make control as rounded (Optional)
 * color : Set color to control (Optional)
 * onItemSelection : Get selected item index
 */
@Composable
fun SegmentedButton(
    items: List<String>,
    defaultSelectedItemIndex: Int = 0,
    useFixedWidth: Boolean = true,
    itemWidth: Dp = 120.dp,
    cornerRadius: Int = 10,
    @ColorRes color: Int = R.color.teal_200,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedIndex = remember { mutableStateOf(defaultSelectedItemIndex) }

    Row(
        modifier = Modifier
    ) {
        items.forEachIndexed { index, item ->
            OutlinedButton(
                modifier = when (index) {
                    0 -> {
                        if (useFixedWidth) {
                            Modifier
                                .width(itemWidth)
                                .offset(0.dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        } else {
                            Modifier
                                .wrapContentSize()
                                .offset(0.dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        }
                    }
                    else -> {
                        if (useFixedWidth)
                            Modifier
                                .width(itemWidth)
                                .offset((-1 * index).dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        else Modifier
                            .wrapContentSize()
                            .offset((-1 * index).dp, 0.dp)
                            .zIndex(if (selectedIndex.value == index) 1f else 0f)
                    }
                },
                onClick = {
                    selectedIndex.value = index
                    onItemSelection(selectedIndex.value)
                },
                shape = when (index) {
                    /**
                     * left outer button
                     */
                    0 -> RoundedCornerShape(
                        topStartPercent = cornerRadius,
                        topEndPercent = 0,
                        bottomStartPercent = cornerRadius,
                        bottomEndPercent = 0
                    )
                    /**
                     * right outer button
                     */
                    items.size - 1 -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = cornerRadius,
                        bottomStartPercent = 0,
                        bottomEndPercent = cornerRadius
                    )
                    /**
                     * middle button
                     */
                    else -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 0,
                        bottomStartPercent = 0,
                        bottomEndPercent = 0
                    )
                },
                border = BorderStroke(
                    1.dp, if (selectedIndex.value == index) {
                        colorResource(id = color)
                    } else {
                        colorResource(id = color).copy(alpha = 0.75f)
                    }
                ),
                colors = if (selectedIndex.value == index) {
                    /**
                     * selected colors
                     */
                    ButtonDefaults.outlinedButtonColors()
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = colorResource(
                            id = color
                        )
                    )
                } else {
                    /**
                     * not selected colors
                     */
                    ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                },
            ) {
                Text(
                    text = item,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedIndex.value == index) {
                        Color.White
                    } else {
                        colorResource(id = color).copy(alpha = 0.9f)
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipGroup(
    items: List<String>
) {
    var chipState by remember { mutableStateOf("Pequeño") }

    BuscaPetTheme {
        FlowRow(
            mainAxisSpacing = 8.dp,
        ) {
            items.forEach { item ->
                SuggestionChip(
                    onClick = {
                        chipState = if (item != chipState) item else ""
                    },
                    label = { Text(
                        text = item,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (item == chipState)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onBackground
                    ) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (item == chipState)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.background
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        borderColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSegmentedButton() {
    val demoButtons = listOf("Pequeño", "Mediano", "Grande", "Gigante")
    ChipGroup(items = demoButtons)
}