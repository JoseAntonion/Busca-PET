package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.buscapet.R
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun CommonAlertDialog(
    dismissAction: () -> Unit,
    reportPet: () -> Unit,
    myPet: () -> Unit
) {
    BuscaPetTheme {
        Dialog(
            onDismissRequest = { dismissAction() }
        ) {
            Card(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_report),
//                        contentDescription = null, // decorative
//                        contentScale = ContentScale.Fit,
//                        colorFilter = ColorFilter.tint(
//                            color = MaterialTheme.colorScheme.onBackground
//                        ),
//                        modifier = Modifier
//                            .padding(top = 8.dp)
//                            .height(30.dp)
//                            .fillMaxWidth()
//                    )
//                    Text(
//                        text = "Selecciona una opción",
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp),
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onBackground
//                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable {
                                    dismissAction()
                                    reportPet()
                                }
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.Visibility,
                                    null,
                                    modifier = Modifier.size(60.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    "Reportar Mascota",
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clickable {
                                    dismissAction()
                                    myPet()
                                }
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.secondary)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.dog_barking_48px),
                                    null,
                                    modifier = Modifier.size(60.dp),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                                Text(
                                    "Perdi mi Mascota",
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun PreviewCommonAlertDialog() {
    CommonAlertDialog(
        dismissAction = {},
        reportPet = {},
        myPet = {}
    )
}