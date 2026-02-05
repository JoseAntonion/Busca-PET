package com.example.buscapet.core.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.buscapet.R
import com.example.buscapet.core.domain.use_case.CheckProximityUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProximityAlertService : Service() {

    @Inject
    lateinit var checkProximityUseCase: CheckProximityUseCase

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    serviceScope.launch {
                        val nearbyPetType = checkProximityUseCase(location)
                        if (nearbyPetType != null) {
                            sendProximityNotification(nearbyPetType)
                        }
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return START_NOT_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun start() {
        if (hasLocationPermission()) {
            val notification = NotificationCompat.Builder(this, "proximity_alerts")
                .setContentTitle("Buscando mascotas cercanas")
                .setContentText("Monitoreando tu ubicación para alertarte sobre avistamientos.")
                .setSmallIcon(R.drawable.buscapet_icon)
                .setOngoing(true)
                .build()

            startForeground(NOTIFICATION_ID, notification)

            val locationRequest =
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL)
                    .setMinUpdateIntervalMillis(FASTEST_UPDATE_INTERVAL)
                    .build()

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            stopSelf()
        }
    }

    private fun stop() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun sendProximityNotification(petType: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "proximity_alerts")
            .setContentTitle("¡Alerta de Proximidad!")
            .setContentText("Estás cerca de una zona donde se reportó un $petType.")
            .setSmallIcon(R.drawable.buscapet_icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(PROXIMITY_NOTIFICATION_ID, notification)
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        private const val NOTIFICATION_ID = 1
        private const val PROXIMITY_NOTIFICATION_ID = 2
        private const val UPDATE_INTERVAL = 10000L // 10 seconds
        private const val FASTEST_UPDATE_INTERVAL = 5000L // 5 seconds
    }
}