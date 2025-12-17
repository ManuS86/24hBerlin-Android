package com.example.a24hberlin.managers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.net.toUri

object ExternalMapNavigator {
    private const val GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps"

    fun navigateToGoogleMaps(context: Context, latitude: Double, longitude: Double) {
        val gmmIntentUri = "google.navigation:q=$latitude,$longitude".toUri()
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage(GOOGLE_MAPS_PACKAGE)
        }

        if (isGoogleMapsInstalled(context)) {
            context.startActivity(mapIntent)
        } else {
            val webUri = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude".toUri()
            context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
        }
    }

    private fun isGoogleMapsInstalled(context: Context): Boolean {
        return try {
            context.packageManager.getPackageInfo(GOOGLE_MAPS_PACKAGE, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}