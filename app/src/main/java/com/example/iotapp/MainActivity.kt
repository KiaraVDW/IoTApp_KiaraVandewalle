package com.example.iotapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.iotapp.ui.screens.OverviewScreen
import com.example.iotapp.ui.theme.IoTAppTheme
import com.example.iotapp.viewmodel.LampViewModel

class MainActivity : ComponentActivity() {
    private val lampViewModel: LampViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IoTAppTheme {
                OverviewScreen(
                    viewModel = lampViewModel,
                    onOpenLamp = { lampId ->
                        val intent = Intent(this, LampDetailActivity::class.java)
                        intent.putExtra(LampDetailActivity.EXTRA_LAMP_ID, lampId)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
