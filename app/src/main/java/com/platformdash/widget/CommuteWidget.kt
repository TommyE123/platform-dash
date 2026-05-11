package com.platformdash.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.LocalSize
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
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
import com.platformdash.data.MockTrainRepository
import com.platformdash.domain.Departure
import com.platformdash.domain.DepartureStatus
import com.platformdash.domain.Departures
import com.platformdash.domain.Route
import com.platformdash.settings.ThemeMode
import com.platformdash.settings.ThemePreferences

class CommuteWidget : GlanceAppWidget() {
    private val repository = MockTrainRepository()
    override val sizeMode: SizeMode = SizeMode.Exact

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

private val WidgetBorderOuterWidth = 6.dp
private val WidgetBorderInnerWidth = 3.dp

@Composable
private fun CommuteWidgetContent(
    departures: Departures,
    mode: ThemeMode,
) {
    val scale = widgetScaleConfig(LocalSize.current.height)
    val topServices = departures.services.take(scale.maxTrains)
    val colors = widgetColors(mode = mode)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(colors.borderOuter)
            .padding(WidgetBorderOuterWidth)
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(colors.borderInner)
                .padding(WidgetBorderInnerWidth)
        ) {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(colors.background)
                    .padding(12.dp)
            ) {
                Text(
                    text = "${departures.route.normalizedOrigin} -> ${departures.route.normalizedDestination}",
                    style = TextStyle(
                        fontSize = scale.headerFontSize,
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
                            cancelledColor = colors.cancelled,
                            textColor = colors.text,
                            timeFontSize = scale.timeFontSize,
                        )
                        Spacer(modifier = GlanceModifier.height(6.dp))
                    }
                }
            }
        }
    }
}


private data class WidgetColors(
    val background: ColorProvider,
    val text: ColorProvider,
    val borderOuter: ColorProvider,
    val borderInner: ColorProvider,
    val cancelled: ColorProvider,
)

private fun widgetColors(mode: ThemeMode): WidgetColors {
    return when (mode) {
        ThemeMode.LIGHT -> WidgetColors(
            background = ColorProvider(Color.White),
            text = ColorProvider(Color(0xFF071D49)),
            borderOuter = ColorProvider(Color(0xFF071D49)),
            borderInner = ColorProvider(Color(0xFFE60000)),
            cancelled = ColorProvider(Color(0xFFE60000)),
        )

        ThemeMode.DARK -> WidgetColors(
            background = ColorProvider(Color(0xFF071D49)),
            text = ColorProvider(Color(0xFFFFFFFF)),
            borderOuter = ColorProvider(Color(0xFFFFFFFF)),
            borderInner = ColorProvider(Color(0xFFE60000)),
            cancelled = ColorProvider(Color(0xFFE60000)),
        )
    }
}

private data class WidgetScaleConfig(
    val headerFontSize: androidx.compose.ui.unit.TextUnit,
    val timeFontSize: androidx.compose.ui.unit.TextUnit,
    val maxTrains: Int,
)

private fun widgetScaleConfig(height: Dp): WidgetScaleConfig {
    return when {
        height < 100.dp -> WidgetScaleConfig(
            headerFontSize = 12.sp,
            timeFontSize = 14.sp,
            maxTrains = 3,
        )

        height <= 200.dp -> WidgetScaleConfig(
            headerFontSize = 14.sp,
            timeFontSize = 18.sp,
            maxTrains = 3,
        )

        else -> WidgetScaleConfig(
            headerFontSize = 18.sp,
            timeFontSize = 24.sp,
            maxTrains = 5,
        )
    }
}

@Composable
private fun DepartureRow(
    service: Departure,
    cancelledColor: ColorProvider,
    textColor: ColorProvider,
    timeFontSize: androidx.compose.ui.unit.TextUnit,
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
                fontSize = timeFontSize,
                fontWeight = FontWeight.Medium,
                color = cancelledColor,
            )
        )
    } else {
        Text(
            text = lineText,
            style = TextStyle(
                fontSize = timeFontSize,
                fontWeight = FontWeight.Medium,
                color = textColor,
            )
        )
    }
}
