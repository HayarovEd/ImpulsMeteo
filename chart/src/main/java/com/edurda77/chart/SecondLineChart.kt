package com.edurda77.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edurda77.domain.model.ElementHistory
import com.edurda77.domain.utils.calculateInterval
import com.edurda77.domain.utils.formatDateTimeChart
import com.edurda77.domain.utils.formatted
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.uikit.asUiTextParam
import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.time.ExperimentalTime

private const val COUNT_STEPS = 5

@Composable
fun SecondLineChart(
    modifier: Modifier = Modifier,
    infos: List<ElementHistory>,
    unit: String,
    chartColor: Color,
    textColor: Color,
    maxValue: String,
    minValue: String,
    fonsSize: TextUnit = 10.sp,
) {
    val intervalAxisXLabel = calculateInterval(infos.size)
    val spacing = 100f
    val transparentGraphColor = remember {
        chartColor.copy(alpha = 0.5f)
    }
    var offsetX by remember {
        mutableFloatStateOf(0f)
    }

    val startUpperValue = infos.maxOfOrNull { it.value } ?: 0.0
    val startLowerValue = infos.minOfOrNull { it.value } ?: 0.0
    val upperValue =
        if (startLowerValue == startUpperValue) startLowerValue * (1 + COUNT_STEPS / 2) else startUpperValue
    val lowerValue =
        if (startLowerValue == startUpperValue) startLowerValue * (1 - COUNT_STEPS / 2) else startLowerValue
    val textStyle = LocalTextStyle.current.copy(
        fontSize = fonsSize
    )
    val measurer = rememberTextMeasurer()
    val xLabelTextLayoutResults = infos.map {
        measurer.measure(
            text = formatDateTimeChart(it.time),
            style = textStyle.copy(textAlign = TextAlign.Center)
        )
    }
    val priceStep = (upperValue - lowerValue) / COUNT_STEPS
    val yLabels = (0..COUNT_STEPS).map {
        formatted(
            value = (lowerValue + priceStep * it),
            unit = unit
        )
    }
    val yLabelTextLayoutResults = yLabels.map {
        measurer.measure(
            text = it,
            style = textStyle
        )
    }
    Canvas(
        modifier = modifier
            .pointerInput(key1 = Unit) {
                detectDragGestures(
                    onDrag = { change, _ ->
                        offsetX = change.position.x
                    },
                    onDragEnd = {
                        offsetX = 0f
                    }
                )
            }
    ) {
        val spacePerHour = (size.width - spacing) / infos.size
        (0 until xLabelTextLayoutResults.size - 1 step intervalAxisXLabel).forEach { index ->
            drawText(
                textLayoutResult = xLabelTextLayoutResults[index],
                topLeft = Offset(
                    x = spacing + index * spacePerHour - xLabelTextLayoutResults[index].size.width / 2,
                    y = this.size.height - 50
                ),
                color = textColor
            )
        }

        (0..COUNT_STEPS).forEach { i ->
            drawText(
                textLayoutResult = yLabelTextLayoutResults[i],
                topLeft = Offset(
                    x = 0f,
                    y = size.height - spacing - fonsSize.value - i * (size.height - spacing) / COUNT_STEPS,
                ),
                color = textColor
            )
            drawLine(
                color = textColor,
                start = Offset(
                    x = spacing,
                    y = size.height - spacing - i * (size.height - spacing) / COUNT_STEPS
                ),
                end = Offset(
                    x = size.width,
                    y = size.height - spacing - i * (size.height - spacing) / COUNT_STEPS
                ),
            )
        }
        if (offsetX >= spacing && offsetX <= size.width) {
            drawLine(
                color = textColor,
                strokeWidth = 3f,
                start = Offset(x = offsetX, y = size.height - spacing),
                end = Offset(x = offsetX, y = 0f),
            )
        }
        var lastX = 0f
        val strokePath = Path().apply {
            val height = size.height
            for (i in infos.indices) {
                val info = infos[i]
                val nextInfo = infos.getOrNull(i + 1) ?: infos.last()
                val leftRatio = (info.value - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.value - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (1f - spacing / height) * (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 =
                    height - spacing - (1f - spacing / height) * (rightRatio * height).toFloat()
                if (i == 0) {
                    moveTo(x1, y1)
                }
                lastX = (x1 + x2) / 2f
                quadraticTo(
                    x1, y1, lastX, (y1 + y2) / 2f
                )
            }
        }
        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )
        drawPath(
            path = strokePath,
            color = chartColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
        drawText(
            textLayoutResult = measurer.measure(
                text = "$maxValue ${
                    formatted(
                        value = upperValue,
                        unit = unit
                    )
                }\n$minValue ${
                    formatted(
                        value = lowerValue,
                        unit = unit
                    )
                }",
                style = textStyle.copy(textAlign = TextAlign.Center)
            ),
            topLeft = Offset(
                x = size.width * 0.7f,
                y = 0f,
            ),
            color = textColor
        )
        for (i in infos.indices) {
            val info = infos[i]
            val x = spacing + i * spacePerHour
            if (offsetX >= (x - spacePerHour / 2) && offsetX < (x + spacePerHour / 2)) {
                if (offsetX >= spacing && offsetX <= size.width) {
                    drawText(
                        textLayoutResult = measurer.measure(
                            text = "${formatDateTimeChart(info.time)}\n${
                                formatted(
                                    value = info.value,
                                    unit = unit
                                )
                            }",
                            style = textStyle.copy(textAlign = TextAlign.Center)
                        ),
                        topLeft = Offset(
                            x = size.width / 2,
                            y = 0f,
                        ),
                        color = textColor
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalTime::class)
@Preview(widthDp = 1000)
@Composable
private fun SecondLineChartPreview() {
    ImpulsMeteoTheme {
        val coinHistoryRandomized = remember {
            (1..100).map {
                ElementHistory(
                    value = (Random.nextFloat() - 0.5) * 20.0,
                    time = Clock.System.now()
                        .plus(1 * it, DateTimeUnit.MINUTE, TimeZone.currentSystemDefault())
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                )
            }
        }
        SecondLineChart(
            modifier = Modifier
                .width(700.dp)
                .height(700.dp)
                .background(Color.White),
            infos = coinHistoryRandomized,
            unit = 2.asUiTextParam(),
            chartColor = MaterialTheme.colorScheme.outlineVariant,
            textColor = MaterialTheme.colorScheme.onBackground,
            maxValue = stringResource(R.string.max_value),
            minValue = stringResource(R.string.min_value)
        )
    }
}