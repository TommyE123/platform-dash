package com.platformdash.widget

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.platformdash.R
import com.platformdash.data.MockTrainRepository
import com.platformdash.domain.Departure
import com.platformdash.domain.DepartureStatus
import com.platformdash.domain.Departures
import com.platformdash.domain.Route
import com.platformdash.settings.ThemeMode
import com.platformdash.settings.ThemePreferences

class CommuteWidget : GlanceAppWidget() {
    private val repository = MockTrainRepository()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val departures = repository.getDepartures(Route.DefaultCommute)
        val mode = ThemePreferences.getThemeMode(context)
        provideContent {
            CommuteWidgetContent(
                departures = departures,
                mode = mode,
            )
        }
    }
}

@Composable
private fun CommuteWidgetContent(
    departures: Departures,
    mode: ThemeMode,
) {
    val topServices = departures.services.take(3)
    val cancelledColor = ColorProvider(MaterialTheme.colorScheme.error)
    val colors = widgetColors(mode = mode)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(colors.background)
            .padding(12.dp)
    ) {
        Text(
            text = "${departures.route.normalizedOrigin} -> ${departures.route.normalizedDestination}",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.text,
            )
        )

        Spacer(modifier = GlanceModifier.height(8.dp))

        if (topServices.isEmpty()) {
            Text(
                text = "No departures available",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = colors.text,
                )
            )
        } else {
            topServices.forEach { service ->
                DepartureRow(
                    service = service,
                    cancelledColor = cancelledColor,
                    textColor = colors.text,
                )
                Spacer(modifier = GlanceModifier.height(6.dp))
            }
        }
    }
}


private data class WidgetColors(
    val background: ColorProvider,
    val text: ColorProvider,
)

private fun widgetColors(mode: ThemeMode): WidgetColors {
    return when (mode) {
        ThemeMode.SYSTEM -> WidgetColors(
            background = ColorProvider(R.color.widget_background),
            text = ColorProvider(R.color.widget_text_primary),
        )

        ThemeMode.LIGHT -> WidgetColors(
            background = ColorProvider(Color.White),
            text = ColorProvider(Color(0xFF111111)),
        )

        ThemeMode.DARK -> WidgetColors(
            background = ColorProvider(Color(0xFF121212)),
            text = ColorProvider(Color(0xFFEAEAEA)),
        )
    }
}
@Composable
private fun DepartureRow(
    service: Departure,
    cancelledColor: ColorProvider,
    textColor: ColorProvider,
) {
    val statusText = when (service.status) {
        DepartureStatus.ON_TIME -> "On time"
        DepartureStatus.DELAYED -> "Delayed"
        DepartureStatus.CANCELLED -> "Cancelled"
    }

    val expectedTime = service.expectedDepartureIso.substring(11, 16)
    val lineText = "$expectedTime  P${service.platform ?: "-"}  $statusText"

    if (service.status == DepartureStatus.CANCELLED) {
        Text(
            text = lineText,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = cancelledColor,
            )
        )
    } else {
        Text(
            text = lineText,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
            )
        )
    }
}
