package com.esutor.twentyfourhoursberlin.managers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.net.toUri

private const val GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps"

object ExternalMapNavigator {

    /**
     * Opens Google Maps (or browser) to show the route overview to the target.
     * Uses the universal Directions API format.
     */
    fun navigateToGoogleMaps(context: Context, latitude: Double, longitude: Double) {
        val directionsUri = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude".toUri()

        val mapIntent = Intent(Intent.ACTION_VIEW, directionsUri).apply {
            addNavigationFlags(context)

            // Priority Logic: Lock to Google Maps app if it exists
            if (isGoogleMapsInstalled(context)) {
                setPackage(GOOGLE_MAPS_PACKAGE)
            }
        }

        context.startActivity(mapIntent)
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