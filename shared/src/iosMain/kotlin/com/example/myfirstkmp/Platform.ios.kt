package com.example.myfirstkmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIDevice
import kotlin.time.Duration.Companion.milliseconds

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@Composable
actual fun RunPlatformCode() {
    val window = UIApplication.sharedApplication.keyWindow?.rootViewController?.view
    LaunchedEffect(Unit) {
        while (true) {
            window?.backgroundColor = UIColor.cyanColor
            delay(1000.milliseconds)
            window?.backgroundColor = UIColor.yellowColor
            delay(1000.milliseconds)
        }
    }
}