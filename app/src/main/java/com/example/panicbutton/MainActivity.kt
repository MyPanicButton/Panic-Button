package com.example.panicbutton

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.core.content.ContextCompat
import com.example.panicbutton.navigation.MainApp
import com.example.panicbutton.notification.createNotificationChannel
import com.example.panicbutton.ui.theme.PanicButtonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
                //menggunakan ActivityResultContracts utk meminta izin post notifikasi
            ) {}

            if (ContextCompat.checkSelfPermission(
                    this, "android.permission.POST_NOTIFICATIONS"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
            }
        }

        createNotificationChannel(this)
        setContent {
            val lockFont = Density(density = LocalDensity.current.density, fontScale = 1f)
            CompositionLocalProvider(LocalDensity provides lockFont) {
//                PanicButtonTheme {
                MainApp()
//                }
            }
        }
    }
}