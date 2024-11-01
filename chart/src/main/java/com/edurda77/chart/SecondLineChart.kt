package com.edurda77.chart

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edurda77.domain.model.ElementHistory
import com.edurda77.domain.utils.formatDateTimeChart
import com.edurda77.domain.utils.formatDateTimeChart2

@Composable
fun StockSection(
    modifier: Modifier = Modifier,
    infos: List<ElementHistory>,
    graphColor: Color = Color.Black
) {
    val spacing = 100f
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }
    var offsetX by remember {
        mutableStateOf(0f)
    }

    var offsetTwice by remember {
        mutableFloatStateOf(0f)
    }
    val upperValue = infos.maxOfOrNull { it.value } ?: 0.0
    val lowerValue = infos.minOfOrNull { it.value } ?: 0.0
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 10.sp.toPx() }
        }
    }
    Canvas(
        modifier = modifier
            .pointerInput(key1 = Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        offsetX = change.position.x
                    },
                    onDragEnd = {
                        offsetX = 0f
                    }
                )
            }
    ) {
        val spacePerHour = (size.width - spacing) / infos.size
        (0 until infos.size - 1 step 20).forEach { i ->
            val info = infos[i]
            val hour = formatDateTimeChart(info.time)
            val date = formatDateTimeChart2(info.time)
            drawAxisXText(
                signatureX1 = hour,
                signatureX2 = date,
                textPaint = textPaint,
                spacing = spacing,
                draw = this,
                step = i,
                spacePerHour = spacePerHour
            )
        }
        val priceStep = (upperValue - lowerValue) / 5f
        (0..4).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    (lowerValue + priceStep * i).toString(),
                    100f,
                    size.height - spacing - i * size.height / 5f,
                    textPaint
                )
            }
            drawLine(
                color = Color.Black,
                start = Offset(x = 100f, y = size.height - spacing - i * size.height / 5f),
                end = Offset(x = size.width, y = size.height - spacing - i * size.height / 5f),
            )
        }
        if (offsetX >= spacing && offsetX <= size.width) {
            drawLine(
                color = Color.Black,
                strokeWidth = 2f,
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
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
        for (i in infos.indices) {
            val info = infos[i]
            val x = spacing + i * spacePerHour
            if (offsetX >= (x - spacePerHour / 2) && offsetX < (x + spacePerHour / 2)) {
                //Log.d("wert", info.price.toString())
                if (offsetX >= spacing && offsetX <= size.width) {

                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "Дата ${info.time}\n${info.value}",
                            size.width / 2,
                            spacing,
                            textPaint
                        )
                    }
                }
            }
        }
    }
}

fun drawAxisXText(
    signatureX1: String,
    signatureX2: String,
    textPaint: Paint,
    spacing: Float,
    draw: DrawScope,
    step: Int,
    spacePerHour: Float
) {
    draw.drawContext.canvas.nativeCanvas.apply {
        drawText(
            signatureX1,
            spacing + step * spacePerHour,
            draw.size.height - 50,
            textPaint
        )
        drawText(
            signatureX2,
            spacing + step * spacePerHour,
            draw.size.height - 25,
            textPaint
        )
    }
    draw.drawLine(
        color = Color.White,
        start = Offset(x = spacing + step * spacePerHour, y = draw.size.height - spacing),
        end = Offset(x = spacing + step * spacePerHour, y = 0f),
    )
}