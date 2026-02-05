package com.example.buscapet.core.domain.use_case

import android.location.Location
import com.example.buscapet.data.local.PetsRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class CheckProximityUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    private val notifiedRoutes = mutableMapOf<String, Long>()

    suspend operator fun invoke(currentLocation: Location): String? {
        if (currentUserId == null) return null

        val reports = petsRepository.getPetListByReporter(currentUserId)
        val routes = reports.groupBy { it.description }

        routes.forEach { (description, pets) ->
            if (description == null) return@forEach

            val now = System.currentTimeMillis()
            val lastNotificationTime = notifiedRoutes[description]

            // Debounce notifications for the same route type for 1 hour
            if (lastNotificationTime != null && (now - lastNotificationTime) < NOTIFICATION_DEBOUNCE_MS) {
                return@forEach
            }

            pets.forEach { pet ->
                if (pet.latitude != null && pet.longitude != null) {
                    val distance = FloatArray(1)
                    Location.distanceBetween(
                        currentLocation.latitude, currentLocation.longitude,
                        pet.latitude, pet.longitude,
                        distance
                    )

                    if (distance[0] < PROXIMITY_THRESHOLD_METERS) {
                        notifiedRoutes[description] = now
                        return description // Return the pet type for the notification
                    }
                }
            }
        }

        return null
    }

    companion object {
        private const val PROXIMITY_THRESHOLD_METERS = 100
        private const val NOTIFICATION_DEBOUNCE_MS = 3600_000 // 1 hour
    }
}