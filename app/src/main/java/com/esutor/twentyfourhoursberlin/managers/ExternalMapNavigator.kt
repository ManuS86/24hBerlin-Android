package com.esutor.twentyfourhoursberlin.managers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.net.toUri

object ExternalMapNavigator {
    private const val GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps"

    // --- Public navigation actions ---
    fun navigateToGoogleMaps(context: Context, latitude: Double, longitude: Double) {
        val gmmIntentUri = "google.navigation:q=$latitude,$longitude".toUri()
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage(GOOGLE_MAPS_PACKAGE)
            addNavigationFlags(context)
        }

        if (isGoogleMapsInstalled(context)) {
            context.startActivity(mapIntent)
        } else {
            // Fallback to browser if Maps is not installed
            val webUri = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude".toUri()
            val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                addNavigationFlags(context)
            }
            context.startActivity(webIntent)
        }
    }

    // --- Internal helpers ---
    private fun Intent.addNavigationFlags(context: Context) {
        if (context !is Activity) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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