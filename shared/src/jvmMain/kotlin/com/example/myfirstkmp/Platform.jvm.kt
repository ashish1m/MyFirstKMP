package com.example.myfirstkmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.DialogWindow
import kotlinx.coroutines.delay
import javax.swing.JDialog
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import kotlin.time.Duration.Companion.milliseconds

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

@Composable
actual fun RunPlatformCode() {
    val title = remember { mutableStateOf("This is a marquee") }
    val dialogVisible = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) {
            title.value = "This is a marquee"
            delay(1000.milliseconds)
            title.value = "For democracy"
            delay(1000.milliseconds)
        }
    }
    if (dialogVisible.value) {
        DialogWindow(onCloseRequest = { dialogVisible.value = false }, title = title.value) {

        }
    }
}