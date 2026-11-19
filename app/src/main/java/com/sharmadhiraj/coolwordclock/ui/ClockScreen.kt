package com.sharmadhiraj.coolwordclock.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.sharmadhiraj.coolwordclock.R
import com.sharmadhiraj.coolwordclock.clock.Word
import com.sharmadhiraj.coolwordclock.clock.WordClock
import com.sharmadhiraj.coolwordclock.data.Settings
import java.time.LocalTime

private const val UNLIT_ALPHA = 0.13f
private const val NIGHT_ALPHA = 0.35f
private const val FADE_MILLIS = 800
private val wordStyle = TextStyle(fontWeight = FontWeight.Bold, letterSpacing = 0.04.em)
private val burnInOffsets = listOf(0 to 0, 3 to -2, -2 to 3, 2 to 2, -3 to -1, 1 to -3)

@Composable
fun ClockScreen(
    time: LocalTime,
    settings: Settings,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val litWords = WordClock.litWords(time)
    val sentence = WordClock.sentence(time)
    val openSettingsLabel = stringResource(R.string.open_settings)
    val dimmed = settings.nightDim && (time.hour >= 22 || time.hour < 7)
    val contentAlpha by animateFloatAsState(if (dimmed) NIGHT_ALPHA else 1f, tween(FADE_MILLIS))
    val (driftX, driftY) = burnInOffsets[(time.minute / 5) % burnInOffsets.size]
    val offsetX by animateDpAsState(driftX.dp, tween(FADE_MILLIS))
    val offsetY by animateDpAsState(driftY.dp, tween(FADE_MILLIS))

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onOpenSettings,
            )
            .clearAndSetSemantics {
                contentDescription = sentence.lowercase().replaceFirstChar { it.uppercase() }
                onClick(label = openSettingsLabel) { onOpenSettings(); true }
            },
        contentAlignment = Alignment.Center,
    ) {
        val grid = rememberGridMetrics(
            maxWidth = minOf(maxWidth * 0.84f, 560.dp),
            maxHeight = maxHeight * 0.8f,
        )
        val fontSize = grid.fontSize
        val fontSizeDp = with(LocalDensity.current) { fontSize.toDp() }

        Column(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToPx(), offsetY.roundToPx()) }
                .graphicsLayer { alpha = contentAlpha }
                .width(grid.width),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WordClock.rows.forEach { row ->
                WordRow(row, litWords, settings.accent.color, fontSize)
            }
            if (settings.minuteDots) {
                Spacer(Modifier.height(fontSizeDp * 0.9f))
                MinuteDots(WordClock.extraMinutes(time), settings.accent.color, fontSizeDp * 0.28f)
            }
        }
    }
}

@Composable
private fun WordRow(row: List<Word>, litWords: Set<Word>, accent: Color, fontSize: TextUnit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        row.forEach { word ->
            val color by animateColorAsState(
                targetValue = if (word in litWords) accent else accent.copy(alpha = UNLIT_ALPHA),
                animationSpec = tween(FADE_MILLIS),
            )
            Text(text = word.text, color = color, fontSize = fontSize, style = wordStyle, maxLines = 1)
        }
    }
}

@Composable
private fun MinuteDots(count: Int, accent: Color, dotSize: Dp) {
    Row(horizontalArrangement = Arrangement.spacedBy(dotSize * 1.4f)) {
        repeat(4) { index ->
            val color by animateColorAsState(
                targetValue = if (index < count) accent else accent.copy(alpha = UNLIT_ALPHA),
                animationSpec = tween(FADE_MILLIS),
            )
            Box(Modifier.size(dotSize).background(color, CircleShape))
        }
    }
}

private class GridMetrics(val fontSize: TextUnit, val width: Dp)

@Composable
private fun rememberGridMetrics(maxWidth: Dp, maxHeight: Dp): GridMetrics {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    return remember(maxWidth, maxHeight, density) {
        val referenceSize = 100.sp
        val style = wordStyle.copy(fontSize = referenceSize)
        val gap = measurer.measure(" ", style).size.width * 1.5f
        val lineHeight = measurer.measure("A", style).size.height.toFloat()
        val widestRow = WordClock.rows.maxOf { row ->
            row.sumOf { measurer.measure(it.text, style).size.width } + gap * (row.size - 1)
        }
        val rowCount = WordClock.rows.size + 1.5f
        with(density) {
            val scale = minOf(maxWidth.toPx() / widestRow, maxHeight.toPx() / (lineHeight * rowCount))
            GridMetrics(fontSize = referenceSize * scale, width = (widestRow * scale).toDp())
        }
    }
}
