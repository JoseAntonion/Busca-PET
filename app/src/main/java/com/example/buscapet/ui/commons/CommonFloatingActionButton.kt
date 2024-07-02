package com.example.buscapet.ui.commons

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buscapet.R
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun CommonFloatingActionButton(
    onClick: () -> Unit = {}
) {
    BuscaPetTheme {
        FloatingActionButton(
            modifier = Modifier
                .size(90.dp),
            onClick = { onClick() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.rounded_campaign_24),
                    contentDescription = "Localized description",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    fontSize = 12.sp,
                    text = stringResource(R.string.report_button_text),
                    letterSpacing = 0.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLight() {
    CommonFloatingActionButton()
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewDark() {
    CommonFloatingActionButton()
}