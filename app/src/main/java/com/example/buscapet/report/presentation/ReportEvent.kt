package com.example.buscapet.report.presentation

sealed class ReportEvent {
    data class PetNameChanged(val name: String) : ReportEvent()
    data class PetAgeChanged(val age: String) : ReportEvent()
    data class PetBreedChanged(val breed: String) : ReportEvent()
    data class PetDescriptionChanged(val desc: String) : ReportEvent()
    data object Submit : ReportEvent()
}