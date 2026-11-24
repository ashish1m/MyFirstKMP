package com.example.myfirstkmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import web.dom.document
import web.navigator.navigator
import kotlin.time.Duration.Companion.milliseconds

class JsPlatform : Platform {
    private val userAgent = navigator.userAgent
    private val browserList = listOf("Chrome", "Firefox", "Safari", "Edge")

    override val name: String = userAgent.findAnyOf(browserList, ignoreCase = true)
        ?.let { (startIndex) -> userAgent.substring(startIndex).substringBefore(" ") }
        ?: "Unknown"
}

actual fun getPlatform(): Platform = JsPlatform()

@Composable
actual fun RunPlatformCode() {
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.browser.document.title = "This is a marquee"
            delay(1000.milliseconds)
            kotlinx.browser.document.title = "For democracy"
            delay(1000.milliseconds)
        }
    }
}