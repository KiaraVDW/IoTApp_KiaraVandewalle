package com.example.iotapp.data.repository

import com.example.iotapp.data.api.RetrofitInstance
import com.example.iotapp.data.model.ConnectionSettings
import com.example.iotapp.data.model.HueLightUpdate
import com.example.iotapp.data.model.LampState
import kotlin.math.pow

class LampRepository {
    private val api = RetrofitInstance.api

    suspend fun getLights(settings: ConnectionSettings): List<LampState> {
        val url = "${baseUrl(settings)}/lights"
        return api.getLights(url).map { (id, light) ->
            LampState(
                id = id,
                name = light.name,
                on = light.state.on,
                brightness = light.state.brightness,
                colorName = colorNameFromXy(light.state.xy),
                xy = light.state.xy
            )
        }.sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
    }

    suspend fun setPower(settings: ConnectionSettings, lampId: String, on: Boolean) {
        val url = "${baseUrl(settings)}/lights/$lampId/state"
        api.updateLightState(url, HueLightUpdate(on = on))
    }

    suspend fun setBrightness(settings: ConnectionSettings, lampId: String, brightness: Int) {
        val url = "${baseUrl(settings)}/lights/$lampId/state"
        api.updateLightState(url, HueLightUpdate(brightness = brightness.coerceIn(1, 254)))
    }

    suspend fun setColor(settings: ConnectionSettings, lampId: String, red: Int, green: Int, blue: Int) {
        val url = "${baseUrl(settings)}/lights/$lampId/state"
        api.updateLightState(url, HueLightUpdate(xy = rgbToXy(red, green, blue), on = true))
    }

    private fun baseUrl(settings: ConnectionSettings): String {
        val ip = settings.ipAddress.removePrefix("http://").removePrefix("https://").trimEnd('/')
        return "http://$ip/api/${settings.username}"
    }

    fun rgbToXy(redValue: Int, greenValue: Int, blueValue: Int): List<Double> {
        var red = redValue.coerceIn(0, 255) / 255.0
        var green = greenValue.coerceIn(0, 255) / 255.0
        var blue = blueValue.coerceIn(0, 255) / 255.0

        red = if (red > 0.04045) ((red + 0.055) / 1.055).pow(2.4) else red / 12.92
        green = if (green > 0.04045) ((green + 0.055) / 1.055).pow(2.4) else green / 12.92
        blue = if (blue > 0.04045) ((blue + 0.055) / 1.055).pow(2.4) else blue / 12.92

        val xValue = red * 0.664511 + green * 0.154324 + blue * 0.162028
        val yValue = red * 0.283881 + green * 0.668433 + blue * 0.047685
        val zValue = red * 0.000088 + green * 0.072310 + blue * 0.986039
        val sum = xValue + yValue + zValue

        if (sum == 0.0) return listOf(0.0, 0.0)
        return listOf(xValue / sum, yValue / sum)
    }

    private fun colorNameFromXy(xy: List<Double>?): String {
        if (xy == null || xy.size < 2) return "Onbekend"
        val x = xy[0]
        val y = xy[1]
        return when {
            x > 0.55 && y < 0.36 -> "Rood"
            x > 0.35 && y > 0.45 -> "Groen"
            x < 0.25 && y < 0.25 -> "Blauw"
            else -> "Wit"
        }
    }
}
