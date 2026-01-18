package com.example.buscapet.report.presentation

import android.net.Uri

sealed class ReportEvent {
    data class OnImageChanged(val image: Uri) : ReportEvent()
    data class OnLocationRetrieved(val latitude: Double, val longitude: Double) : ReportEvent()
    data class OnNameChanged(val name: String) : ReportEvent()
    data class OnDescriptionChanged(val description: String) : ReportEvent()
    data object Submit : ReportEvent()
}