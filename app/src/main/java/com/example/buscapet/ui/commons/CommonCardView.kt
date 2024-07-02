package com.example.buscapet.ui.commons

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buscapet.R
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun CommonCardView(
    modifier: Modifier = Modifier,
    title: String? = "",
    subtitle: String? = "",
    isEmpty: Boolean = false,
    onClick: () -> Unit = {}
) {
    BuscaPetTheme {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            onClick = { onClick() }
        ) {
            Row(
                modifier = modifier
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Center
            ) {
                if (isEmpty) {
                    Image(
                        modifier = modifier
                            .height(60.dp)
                            .width(60.dp)
                            .padding(14.dp, 0.dp, 0.dp, 0.dp),
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Card image",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.dummy_puppy),
                        contentDescription = "Card image"
                    )
//                    AsyncImage(
//                        model = "https://getdummyimage.com/300/300",
//                        contentDescription = "Profile photo",
//                        modifier = modifier
//                            .padding(0.dp)
//                    )
                }
                Spacer(modifier = Modifier.width(28.dp))
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    if (title?.isNotEmpty() == true) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (subtitle?.isNotEmpty() == true) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewCard() {
    CommonCardView(
        title = "Balto",
        subtitle = "Fecha ultima actualizacion: 01/01/2023",
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewCardEmpty() {
    CommonCardView(
        title = "Registrar nueva mascota",
        subtitle = "Crea una nueva mascota",
        isEmpty = true
    )
}