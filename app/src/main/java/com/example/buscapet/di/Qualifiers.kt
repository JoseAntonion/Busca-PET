package com.example.buscapet.di

//TODO: Buscar que wea es retention
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthApi
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DIo

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DMain