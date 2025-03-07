package com.example.a24hberlin.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    fun navigateToGoogleMaps(context: Context, latitude: Double, longitude: Double) {
        val gmmIntentUri = "google.navigation:q=$latitude,$longitude".toUri()
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (isGoogleMapsInstalled(context)) {
            context.startActivity(mapIntent)
        } else {
            val mapWebsiteUri =
                "https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude".toUri()
            val websiteIntent = Intent(Intent.ACTION_VIEW, mapWebsiteUri)
            context.startActivity(websiteIntent)
        }
    }

    private fun isGoogleMapsInstalled(context: Context): Boolean {
        val packageManager = context.packageManager
        return try {
            packageManager.getApplicationInfo("com.google.android.apps.maps", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}