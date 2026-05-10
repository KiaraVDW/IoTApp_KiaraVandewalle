package com.example.iotapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.iotapp.ui.screens.LampDetailScreen
import com.example.iotapp.ui.theme.IoTAppTheme
import com.example.iotapp.viewmodel.LampViewModel

class LampDetailActivity : ComponentActivity() {
    private val lampViewModel: LampViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lampId = intent.getStringExtra(EXTRA_LAMP_ID) ?: "1"

        setContent {
            IoTAppTheme {
                LampDetailScreen(
                    viewModel = lampViewModel,
                    lampId = lampId,
                    onBack = { finish() }
                )
            }
        }
    }

    companion object {
        const val EXTRA_LAMP_ID = "lamp_id"
    }
}
