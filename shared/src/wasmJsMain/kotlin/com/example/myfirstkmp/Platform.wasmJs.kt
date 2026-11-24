package com.example.myfirstkmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

@Composable
actual fun RunPlatformCode() {
    LaunchedEffect(Unit) {
        while (true) {
            document.title = "This is a marquee"
            delay(1000.milliseconds)
            document.title = "For democracy"
            delay(1000.milliseconds)
        }
    }
}