package com.platformdash.widget

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
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

class CommuteWidget : GlanceAppWidget() {
    private val repository = MockTrainRepository()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val departures = repository.getDepartures(Route.DefaultCommute)
        provideContent {
            CommuteWidgetContent(departures = departures)
        }
    }
}

@Composable
private fun CommuteWidgetContent(departures: Departures) {
    val topServices = departures.services.take(3)
    val cancelledColor = ColorProvider(MaterialTheme.colorScheme.error)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text(
            text = "${departures.route.normalizedOrigin} -> ${departures.route.normalizedDestination}",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        )

        Spacer(modifier = GlanceModifier.height(8.dp))

        if (topServices.isEmpty()) {
            Text(
                text = "No departures available",
                style = TextStyle(fontSize = 14.sp)
            )
        } else {
            topServices.forEach { service ->
                DepartureRow(
                    service = service,
                    cancelledColor = cancelledColor,
                )
                Spacer(modifier = GlanceModifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun DepartureRow(
    service: Departure,
    cancelledColor: ColorProvider,
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
            )
        )
    }
}
