package com.sharmadhiraj.coolwordclock.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sharmadhiraj.coolwordclock.ClockViewModel
import com.sharmadhiraj.coolwordclock.chime.Chime
import com.sharmadhiraj.coolwordclock.clock.minuteTicks
import java.time.LocalTime

@Composable
fun ClockApp(viewModel: ClockViewModel = viewModel()) {
    val settings = viewModel.settings.collectAsStateWithLifecycle().value ?: return
    val context = LocalContext.current
    val time by remember { minuteTicks(context) }.collectAsStateWithLifecycle(LocalTime.now())
    var showSettings by rememberSaveable { mutableStateOf(false) }

    if (settings.chime) ChimeOnFiveMinuteMarks(time)

    val accent = settings.accent.color
    MaterialTheme(colorScheme = darkColorScheme(primary = accent, onPrimary = Color.Black)) {
        ClockScreen(time = time, settings = settings, onOpenSettings = { showSettings = true })
        if (showSettings) {
            SettingsSheet(
                settings = settings,
                onChange = viewModel::update,
                onDismiss = { showSettings = false },
            )
        }
    }
}

@Composable
private fun ChimeOnFiveMinuteMarks(time: LocalTime) {
    val context = LocalContext.current
    val chime = remember { Chime(context) }
    DisposableEffect(chime) { onDispose { chime.close() } }

    var lastChimed by remember { mutableStateOf(time.withSecond(0).withNano(0)) }
    val minute = time.withSecond(0).withNano(0)
    LaunchedEffect(minute) {
        if (minute.minute % 5 == 0 && minute != lastChimed) chime.play()
        lastChimed = minute
    }
}
