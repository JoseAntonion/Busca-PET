package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.buscapet.ui.theme.BuscaPetTheme

val breeds = listOf("Policial", "Shaushau", "Haspapi", "Pudul", "Chico Terri")

@Composable
fun CommonDropdownMenu(
    breeds: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var breedSelected by remember { mutableStateOf("Seleccione una raza") }

    BuscaPetTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            TextButton(onClick = { expanded = true }) {
                Text(text = breedSelected)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                breeds.forEach { breed ->
                    CommonDropdownMenuItem(
                        text = breed,
                        onClick = {
                            breedSelected = breed
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CommonDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
//    leadingIcon: ImageVector,
//    trailingIcon: @Composable () -> Unit
) {
    BuscaPetTheme {
        DropdownMenuItem(
            text = { Text(text) },
            onClick = { onClick() },
//        leadingIcon = {
//            Icon(leadingIcon, null)
//        },
//        trailingIcon = { trailingIcon() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonExposedDropdownMenuBox(
    hintText: String,
    breeds: List<String>,
    data: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val (currentData, onCurrentDataChange) = data
    //var selectedOptionText by remember { mutableStateOf("") }
// We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            //readOnly = true,
            value = currentData,
            onValueChange = { onCurrentDataChange(it) },
            label = { Text(hintText) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        // filter options based on text field value
        val filteringOptions = breeds.filter { it.contains(currentData, ignoreCase = true) }
        if (filteringOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                filteringOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onCurrentDataChange(selectionOption)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCommonExposedDropdownMenuBox() {
    val testData = remember { mutableStateOf("wea") }
    CommonExposedDropdownMenuBox(
        "Input with search",
        breeds,
        testData
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCommonDropdownMenuItem() {
    CommonDropdownMenuItem(
        text = "Item prueba 1"
    ) {}
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCommonDropdownMenu() {
    CommonDropdownMenu(breeds)
}