package com.example.buscapet.core.presentation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
fun ReportAlertDialog(
    modifier: Modifier = Modifier,
    onReportLostClick: () -> Unit = {},
    onLostMyOwnClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card {
            Row(
                modifier = modifier
                    .height(IntrinsicSize.Min)
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        //.fillMaxSize()
                        .clickable {
                            onDismiss()
                            onReportLostClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .height(200.dp)
                                .width(200.dp),
                            painter = painterResource(id = R.drawable.foundapet),
                            contentDescription = "dummy"
                        )
                        Text(
                            text = "Reportar mascota",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 1.dp
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        //.fillMaxSize()
                        .clickable {
                            onDismiss()
                            onLostMyOwnClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .height(200.dp)
                                .width(200.dp),
                            painter = painterResource(id = R.drawable.lostmyownpet),
                            contentDescription = "dummy"
                        )
                        Text(
                            text = "Perdi mi mascota",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewAlertDialog() {
    BuscaPetTheme {
        ReportAlertDialog()
    }
}