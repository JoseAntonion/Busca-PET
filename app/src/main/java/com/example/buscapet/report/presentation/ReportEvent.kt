package com.example.buscapet.report.presentation

import android.net.Uri

sealed class ReportEvent {
    data class OnImageChanged(val image: Uri) : ReportEvent()
    data class OnLocationRetrieved(val latitude: Double, val longitude: Double) : ReportEvent()
}