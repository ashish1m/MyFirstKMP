package com.example.myfirstkmp

import android.health.connect.datatypes.units.Length
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun RunPlatformCode() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Toast.makeText(context, "This is a marquee", Toast.LENGTH_SHORT).show()
        delay(1000.milliseconds)
        Toast.makeText(context, "For democracy", Toast.LENGTH_SHORT).show()
        delay(1000.milliseconds)
    }
}